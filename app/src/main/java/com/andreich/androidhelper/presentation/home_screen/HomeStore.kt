package com.andreich.androidhelper.presentation.home_screen

import com.arkivanov.mvikotlin.core.store.Store

interface HomeStore : Store<HomeStore.Intent, HomeStore.State, HomeStore.Label> {

    data class State(
        val count: Int
    )

    sealed interface Intent {

        data object AddQuestionClick : Intent

        data object StartAGameClick : Intent
    }

    sealed interface Label {

        data object OpenAddQuestionScreen : Label

        data class OpenGameScreen(val count: Int) : Label
    }

}