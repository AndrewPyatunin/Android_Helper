package com.andreich.androidhelper.presentation.game_result_screen

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.usecase.GetAnswersUseCase
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.andreich.androidhelper.presentation.game_result_screen.GameResultStore.*
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch

import javax.inject.Inject

class GameResultStoreFactory @Inject constructor(
    storeFactory: StoreFactory,
    private val getAnswersUseCase: GetAnswersUseCase
) {

    fun create() : GameResultStore = object : GameResultStore by store {}

    val store: GameResultStore = object : GameResultStore, Store<Intent, State, Label> by storeFactory.create(
        name = "GameResultStore",
        initialState = State(0, 0, emptyList()),
        bootstrapper = null,
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    object ReducerImpl : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when(msg) {
                is Message.AnswersIsReady -> {
                    copy(answers = msg.answers)
                }
                Message.LoadAnswers -> {
                    copy()
                }

                is Message.SaveArgs -> {
                    copy(count = msg.count, countRightAnswers = msg.rightAnswersCount, rightPercent = if (msg.count>0) (msg.rightAnswersCount*100).div(msg.count) else 0)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Message, Label>() {

        override fun executeAction(action: Action) {
            when(action) {
                is Action.AnswersIsReady -> {
                    dispatch(Message.AnswersIsReady(action.answers))
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when(intent) {
                is Intent.LoadAnswers -> {
                    scope.launch {
                        dispatch(Message.AnswersIsReady(getAnswersUseCase(intent.ids)))
                    }
                }
                is Intent.SaveArgs -> {
                    dispatch(Message.SaveArgs(intent.count, intent.rightAnswersCount, intent.ids))
                }
            }
        }

    }

    sealed interface Message {

        class AnswersIsReady(val answers: List<Question>) : Message

        class SaveArgs(val count: Int, val rightAnswersCount: Int, val ids: List<Long>) : Message

        object LoadAnswers : Message
    }

    sealed interface Action {

        class AnswersIsReady(val answers: List<Question>) : Action
    }
}