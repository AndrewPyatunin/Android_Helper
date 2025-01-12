package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.domain.model.Question
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface GameScreenComponent {

    val model: StateFlow<GameStore.State>

    fun onAnswerClick(chosenAnswer: String, answer: String, excludedIds: List<Long>)

    @Serializable
    data class Model(
        val answerId: Int,
        val questionText: String,
        val answers: List<Question>
    )
}