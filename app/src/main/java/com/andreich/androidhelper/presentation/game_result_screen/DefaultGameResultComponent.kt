package com.andreich.androidhelper.presentation.game_result_screen

import com.andreich.androidhelper.core.componentScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultGameResultComponent @Inject constructor(
    storeFactory: GameResultStoreFactory,
    componentContext: ComponentContext,
    count: Int,
    countRightAnswers: Int,
    ids: List<Long>,
    onBackPressedCallback: () -> Unit
) : GameResultComponent, ComponentContext by componentContext {

    private val coroutineScope = componentScope()
    private val store = storeFactory.create()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<GameResultStore.State>
        get() = store.stateFlow

    init {
        coroutineScope.launch {
            store.accept(GameResultStore.Intent.SaveArgs(count, countRightAnswers, ids))
            store.accept(GameResultStore.Intent.LoadAnswers(ids))
        }
        val callback = BackCallback {
            onBackPressedCallback()
        }
        backHandler.register(callback)
        lifecycle.doOnDestroy {
            backHandler.unregister(callback)
        }
    }
}