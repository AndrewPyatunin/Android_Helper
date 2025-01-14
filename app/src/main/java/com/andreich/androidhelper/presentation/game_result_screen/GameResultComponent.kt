package com.andreich.androidhelper.presentation.game_result_screen

import kotlinx.coroutines.flow.StateFlow

interface GameResultComponent {

    val model: StateFlow<GameResultStore.State>
}