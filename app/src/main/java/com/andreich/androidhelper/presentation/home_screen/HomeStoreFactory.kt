package com.andreich.androidhelper.presentation.home_screen

import com.andreich.androidhelper.domain.usecase.GetQuestionCountUseCase
import com.andreich.androidhelper.presentation.home_screen.HomeStore.Intent
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeStoreFactory @Inject constructor(
    val storeFactory: StoreFactory,
    private val getQuestionCountUseCase: GetQuestionCountUseCase
) {

    fun create(): Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> = object : Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> by storeFactory.create(
        name = "HomeStoreFactory",
        initialState = HomeStore.State(0),
        bootstrapper = BootstrapperImpl(),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getQuestionCountUseCase().collectLatest {
                    dispatch(Action.LoadQuestionCount(it))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<HomeStore.State, Message> {

        override fun HomeStore.State.reduce(msg: Message): HomeStore.State {
            return when (msg) {

                is Message.LoadQuestionCount -> {
                    copy(count = msg.count)
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, HomeStore.State, Message, HomeStore.Label>() {

        override fun executeAction(action: Action) {
            when(action) {
                is Action.LoadQuestionCount -> {
                    dispatch(Message.LoadQuestionCount(action.count))
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            val state = state()
            when (intent) {
                Intent.AddQuestionClick -> {
                    publish(HomeStore.Label.OpenAddQuestionScreen)
                }

                Intent.StartAGameClick -> {
                    publish(HomeStore.Label.OpenGameScreen(state.count))
                }
            }
        }

    }

    sealed interface Action {

        class LoadQuestionCount(val count: Int) : Action
    }

    sealed interface Message {

        class LoadQuestionCount(val count: Int) : Message
    }
}