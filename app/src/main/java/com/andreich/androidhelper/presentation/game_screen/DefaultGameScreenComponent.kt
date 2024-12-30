package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.core.componentScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultGameScreenComponent @Inject constructor(
    componentContext: ComponentContext,
    private val storeFactory: GameStoreFactory
) : GameScreenComponent, ComponentContext by componentContext {

    private val coroutineScope = componentScope()
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    init {
        coroutineScope.launch {
            store.labels.collect {
                when(it) {
                    is GameStore.Label.Answer -> {
                        store.accept(GameStore.Intent.LoadQuestion(it.excludedIds))
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<GameStore.State>
        get() = store.stateFlow

    override fun onAnswerClick(questionId: Long, answerId: Long) {
        store.accept(GameStore.Intent.ChooseAnswer(questionId, answerId))
    }
}