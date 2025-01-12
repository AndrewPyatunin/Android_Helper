package com.andreich.androidhelper.presentation.home_screen

import com.andreich.androidhelper.core.componentScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultHomeComponent @Inject constructor(
    componentContext: ComponentContext,
    private val storeFactory: HomeStoreFactory,
    private val onAddQuestionClicked: () -> Unit,
    private val onStartAGameClicked: (Int) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HomeStore.State>
        get() = store.stateFlow

    override fun onAddQuestionClick() {
        onAddQuestionClicked()
    }

    override fun onStartAGameClick(count: Int) {
        onStartAGameClicked(count)
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when(it) {
                    HomeStore.Label.OpenAddQuestionScreen -> {
                        store.accept(HomeStore.Intent.AddQuestionClick)
                    }

                    is HomeStore.Label.OpenGameScreen -> {
                        store.accept(HomeStore.Intent.StartAGameClick)
                    }
                }
            }
        }
    }
}