package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.domain.model.Question
import com.arkivanov.mvikotlin.core.store.Store

interface GameStore : Store<GameStore.Intent, GameStore.State, GameStore.Label> {

    data class State(
        val isLoading: Boolean,
        val question: Question?,
        val answers: List<Question>,
        val excludedIds: List<Long> = emptyList(),
        val answerCorrectness: Boolean? = null
    )

    sealed interface Intent {

        data class LoadQuestion(val excludedIds: List<Long>) : Intent

        data class LoadAnswers(val question: Question) : Intent

        data class ChooseAnswer(val questionId: Long, val answerId: Long) : Intent
    }

    sealed interface Label {

        class Answer(val excludedIds: List<Long>) : Label
    }
}