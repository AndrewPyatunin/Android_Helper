package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.usecase.ChooseAnswerUseCase
import com.andreich.androidhelper.domain.usecase.GetAnswersUseCase
import com.andreich.androidhelper.domain.usecase.GetQuestionWithLimitationsUseCase
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.andreich.androidhelper.presentation.game_screen.GameStore.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameStoreFactory @Inject constructor(
    storeFactory: StoreFactory,
    private val chooseAnswerUseCase: ChooseAnswerUseCase,
    private val getQuestionUseCase: GetQuestionWithLimitationsUseCase,
    private val getAnswersUseCase: GetAnswersUseCase
) {

    fun create(): GameStore = object : GameStore by store as GameStore {}

    private val store: Store<Intent, State, Label> =
        object : GameStore, Store<Intent, State, Label> by storeFactory.create(
            name = "GameStoreFactory",
            initialState = State(false, null, emptyList()),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl
        ) {}

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        //будет вызван при создании стора
        override fun invoke() {
            scope.launch {
                dispatch(Action.QuestionIsReady(getQuestionUseCase(emptyList())))
            }
        }

    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Message, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.AnswersIsReady -> {
                    dispatch(Message.AnswersIsReady(action.answers))
                }

                is Action.CheckAnswer -> {
                }

                is Action.LoadError -> {
                }

                is Action.QuestionIsReady -> {
                    dispatch(Message.QuestionIsReady(action.question))
                    scope.launch {
                        getAnswersUseCase(action.question).collect {
                            dispatch(Message.AnswersIsReady(it))
                        }
                    }

                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChooseAnswer -> {
                    scope.launch {
                        val result = chooseAnswerUseCase(intent.answerId, intent.questionId)
                        dispatch(Message.AnswerCorrectness(result))
                    }
                }

                is Intent.LoadAnswers -> {
                    dispatch(Message.LoadAnswers(intent.question))
                }

                is Intent.LoadQuestion -> {
                    dispatch(Message.LoadQuestion(intent.excludedIds))
                }
            }
        }
    }

    private sealed interface Action {

        class AnswersIsReady(val answers: List<Question>) : Action

        class QuestionIsReady(val question: Question) : Action

        class LoadError(val message: String) : Action

        class CheckAnswer(val check: Boolean) : Action
    }

    private sealed interface Message {

        class AnswersIsReady(val answers: List<Question>) : Message

        class QuestionIsReady(val question: Question) : Message

        class AnswerCorrectness(val correctness: Boolean) : Message

        class LoadError(val message: String) : Message

        class LoadQuestion(val excludedIds: List<Long>) : Message

        class LoadAnswers(val question: Question) : Message
    }

    private object ReducerImpl : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when (msg) {
                is Message.LoadAnswers -> {
                    copy(isLoading = true)
                }

                is Message.LoadQuestion -> {
                    copy(isLoading = true, excludedIds = msg.excludedIds)
                }

                is Message.AnswersIsReady -> {
                    copy(isLoading = false, answers = msg.answers)
                }

                is Message.LoadError -> {
                    copy(isLoading = false)
                }

                is Message.QuestionIsReady -> {
                    copy(isLoading = false, question = msg.question)
                }

                is Message.AnswerCorrectness -> {
                    copy(answerCorrectness = msg.correctness)
                }
            }
        }
    }
}