package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.domain.model.Question
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface GameScreenComponent {

    val model: StateFlow<Model>

    fun onAnswerClick(answerId: Int)

    @Serializable
    data class Model(
        val answerId: Int,
        val questionText: String,
        val answers: List<Question>
    )
}