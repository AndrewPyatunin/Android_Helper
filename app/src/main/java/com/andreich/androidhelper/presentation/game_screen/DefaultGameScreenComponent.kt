package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.core.componentScope
import com.andreich.androidhelper.presentation.game_result_screen.GameResultStore
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultGameScreenComponent @Inject constructor(
    componentContext: ComponentContext,
    storeFactory: GameStoreFactory,
    count: Int,
    private val onRestart: () -> Unit,
    private val showResult: (Int, List<Long>) -> Unit,
) : GameScreenComponent, ComponentContext by componentContext {


    private val coroutineScope = componentScope()
    private val store = storeFactory.create()

    init {
        coroutineScope.launch {
            store.accept(GameStore.Intent.PutCount(count))
            store.accept(GameStore.Intent.LoadNewQuestion)

            store.labels.collect {
                val state = model.value
                when (it) {
                    is GameStore.Label.Answer -> {
                        store.accept(GameStore.Intent.LoadQuestion(it.excludedIds))
                    }

                    GameStore.Label.LastAnswer -> {
                        showResult(state.rightAnswersCount, state.excludedIds)
                    }

                    GameStore.Label.NextAnswer -> {
                    }

                    GameStore.Label.QuestionIsReady -> {
                        store.accept(GameStore.Intent.StartTimer(30))
                    }
                }
            }
        }
        lifecycle.doOnDestroy {
            store.accept(GameStore.Intent.Clear)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<GameStore.State>
        get() = store.stateFlow

    override fun onAnswerClick(chosenAnswer: String, answer: String, excludedIds: List<Long>) {
        store.accept(GameStore.Intent.ChooseAnswer(chosenAnswer, answer, excludedIds))
    }
}