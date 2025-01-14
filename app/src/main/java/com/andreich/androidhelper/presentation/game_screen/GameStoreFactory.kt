package com.andreich.androidhelper.presentation.game_screen

import androidx.compose.ui.graphics.Color
import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.usecase.ChooseAnswerUseCase
import com.andreich.androidhelper.domain.usecase.GetQuestionWithLimitationsUseCase
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.andreich.androidhelper.presentation.game_screen.GameStore.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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
                        checkIsLast()
                    }
                }

                is Intent.LoadAnswers -> {
                    dispatch(Message.LoadAnswers(intent.question))
                }

                is Intent.LoadQuestion -> {
                    dispatch(Message.ExcludeUsedIds(intent.excludedIds))
                }

                is Intent.PutCount -> {
                    dispatch(Message.PutCount(intent.count))
                }

                is Intent.LoadNewQuestion -> {
                    scope.launch {
                        getQuestionUseCase(emptyList())?.let {
                            dispatch(Message.QuestionIsReady(it))
                            publish(Label.QuestionIsReady)
                        }
                    }
                }

                is Intent.Clear -> {
                    dispatch(Message.LastAnswer)
                }

                is Intent.ExcludeUsedIds -> {
                    dispatch(Message.ExcludeUsedIds(intent.excludedIds))
                }

                is Intent.StartTimer -> {
                    startATimer(intent.remainTime)
                }
            }
        }
        private fun CoroutineScope.loadQuestion(excludedIds: List<Long>) {
            launch {
                getQuestionUseCase(excludedIds)?.let {
                    dispatch(Message.QuestionIsReady(it))
                    publish(Label.QuestionIsReady)
                    dispatch(Message.Clickable(true))
                }
            }
        }

        private fun CoroutineScope.checkIsLast() {
            val currentState = state()
            if (currentState.excludedIds.size >= currentState.count) {
                publish(Label.LastAnswer)
            } else {
                loadQuestion(currentState.excludedIds)
            }
            dispatch(Message.ResetTimer)
        }

        private fun startATimer(initial: Int) {

            var a = initial
            val flow = flow {
                while (a>-1) {
                    emit(a--)
                    delay(1000)
                }
            }
            scope.launch {
                flow.collect {
                    if(it == 0) {
                        checkIsLast()
                    }
                    dispatch(Message.ChangeTime(it))
                }
            }
        }
    }


    private sealed interface Action {

        class QuestionIsReady(val question: Question) : Action
    }

    private sealed interface Message {

        class QuestionIsReady(val question: Question) : Message

        class Clickable(val isClickable: Boolean) : Message

        class LoadError(val message: String) : Message

        class ExcludeUsedIds(val excludedIds: List<Long>) : Message

        class LoadAnswers(val question: Question) : Message

        class PutCount(val count: Int) : Message

        class RightAnswer(val title: String) : Message

        class WrongAnswer(val title: String) : Message

        class ChangeTime(val remainTime: Int) : Message

        object LastAnswer : Message

        object ResetTimer : Message
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
                    copy(excludedIds = emptyList(), isClickable = true, question = null, answers = emptyList(), count = 0, rightAnswersCount = 0, remainTime = 30)
                }

                is Message.LoadAnswers -> {
                    copy(isLoading = true)
                }

                is Message.ExcludeUsedIds -> {
                    copy(isLoading = true, excludedIds = msg.excludedIds)
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

                is Message.ChangeTime -> {
                    copy(remainTime = msg.remainTime)
                }

                is Message.ResetTimer -> copy(remainTime = 30)
            }
        }
    }
}