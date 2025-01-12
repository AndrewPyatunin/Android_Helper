package com.andreich.androidhelper.presentation.home_screen

import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {

    val model: StateFlow<HomeStore.State>

    fun onAddQuestionClick()

    fun onStartAGameClick(count: Int)
}