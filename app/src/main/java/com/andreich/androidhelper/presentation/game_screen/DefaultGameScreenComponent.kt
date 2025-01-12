package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.core.componentScope
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
    private val storeFactory: GameStoreFactory,
    count: Int,
    private val onRestart: () -> Unit,
    private val showResult: () -> Unit,
) : GameScreenComponent, ComponentContext by componentContext {


    private val coroutineScope = componentScope()
    private val store = storeFactory.create()

    init {
        coroutineScope.launch {
            store.accept(GameStore.Intent.PutCount(count))
            store.accept(GameStore.Intent.LoadNewQuestion)

            store.labels.collect {
                when (it) {
                    is GameStore.Label.Answer -> {
                        store.accept(GameStore.Intent.LoadQuestion(it.excludedIds))
                    }

                    GameStore.Label.LastAnswer -> {
                        showResult()
                    }

                    GameStore.Label.NextAnswer -> {
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