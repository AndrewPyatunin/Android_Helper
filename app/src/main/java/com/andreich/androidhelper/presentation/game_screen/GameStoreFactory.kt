package com.andreich.androidhelper.presentation.game_screen

import androidx.compose.ui.graphics.Color
import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.usecase.ChooseAnswerUseCase
import com.andreich.androidhelper.domain.usecase.GetQuestionWithLimitationsUseCase
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.andreich.androidhelper.presentation.game_screen.GameStore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameStoreFactory @Inject constructor(
    storeFactory: StoreFactory,
    private val chooseAnswerUseCase: ChooseAnswerUseCase,
    private val getQuestionUseCase: GetQuestionWithLimitationsUseCase,
) {

    fun create(): GameStore = object : GameStore by store {}

    private val store: GameStore =
        object : GameStore, Store<Intent, State, Label> by storeFactory.create(
            name = "GameStoreFactory",
            initialState = State(false, null, emptyList()),
            reducer = ReducerImpl,
            bootstrapper = null,
            executorFactory = ::ExecutorImpl
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Message, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.AnswersIsReady -> {
                    dispatch(Message.AnswersIsReady(action.answers))
                }

                is Action.QuestionIsReady -> {
                    dispatch(Message.QuestionIsReady(action.question))
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChooseAnswer -> {
                    scope.launch {
                        dispatch(Message.Clickable(false))
                        val state = state()
                        publish(Label.Answer(intent.excludedIds))
                        chooseAnswerUseCase(intent.chosenAnswer, intent.answer).let {
                            when(it) {
                                true -> {
                                    dispatch(Message.RightAnswer(intent.chosenAnswer))
                                }
                                false -> {
                                    dispatch(Message.WrongAnswer(intent.chosenAnswer))
                                }
                            }
                        }
                        delay(500)
                        if (intent.excludedIds.size >= state.count) {
                            publish(Label.LastAnswer)
                            dispatch(Message.LastAnswer)
                        } else {
                            loadQuestion(state.excludedIds)
                        }
                    }
                }

                is Intent.LoadAnswers -> {
                    dispatch(Message.LoadAnswers(intent.question))
                }

                is Intent.LoadQuestion -> {
                    dispatch(Message.LoadQuestion(intent.excludedIds))
                }

                is Intent.PutCount -> {
                    dispatch(Message.PutCount(intent.count))
                }

                is Intent.LoadNewQuestion -> {
                    scope.launch {
                        getQuestionUseCase(emptyList())?.let {
                            dispatch(Message.QuestionIsReady(it))
                        }
                    }
                }

                is Intent.Clear -> {
                    dispatch(Message.LastAnswer)
                }
            }
        }
        private fun CoroutineScope.loadQuestion(excludedIds: List<Long>) {
            launch {
                getQuestionUseCase(excludedIds)?.let {
                    dispatch(Message.QuestionIsReady(it))
                    publish(Label.NextAnswer)
                    dispatch(Message.Clickable(true))
                }
            }
        }
    }


    private sealed interface Action {

        class AnswersIsReady(val answers: List<Question>) : Action

        class QuestionIsReady(val question: Question) : Action
    }

    private sealed interface Message {

        class AnswersIsReady(val answers: List<Question>) : Message

        class QuestionIsReady(val question: Question) : Message

        class Clickable(val isClickable: Boolean) : Message

        class LoadError(val message: String) : Message

        class LoadQuestion(val excludedIds: List<Long>) : Message

        class LoadAnswers(val question: Question) : Message

        class PutCount(val count: Int) : Message

        class RightAnswer(val title: String) : Message

        class WrongAnswer(val title: String) : Message

        object LastAnswer : Message
    }

    private object ReducerImpl : Reducer<State, Message> {

        private fun Question.mapToList(): List<AnswerCard> {
            return listOf(AnswerCard(answer), AnswerCard(wrongAnswer1), AnswerCard(wrongAnswer2), AnswerCard(wrongAnswer3)).shuffled()
        }

        override fun State.reduce(msg: Message): State {

            return when (msg) {

                is Message.RightAnswer -> {
                    copy(answers = answers.toMutableList().map {
                        if (it.title == msg.title) it.copy(color = Color.Green) else it
                    }, rightAnswersCount = rightAnswersCount + 1)
                }

                is Message.WrongAnswer -> {
                    copy(answers = answers.toMutableList().map {
                        if (it.title == msg.title) it.copy(color = Color.Red) else it
                    })
                }

                is Message.LastAnswer -> {
                    copy(excludedIds = emptyList(), isClickable = true, question = null, answers = emptyList(), count = 0)
                }

                is Message.LoadAnswers -> {
                    copy(isLoading = true)
                }

                is Message.LoadQuestion -> {
                    copy(isLoading = true, excludedIds = msg.excludedIds)
                }

                is Message.AnswersIsReady -> {
                    copy(isLoading = false)
                }

                is Message.LoadError -> {
                    copy(isLoading = false)
                }

                is Message.QuestionIsReady -> {
                    copy(isLoading = false, question = msg.question, answers = msg.question.mapToList(), excludedIds = excludedIds.toMutableList().apply { add(msg.question.id)})
                }

                is Message.Clickable -> {
                    copy(isClickable = msg.isClickable)
                }

                is Message.PutCount -> {
                    copy(count = msg.count)
                }
            }
        }
    }
}