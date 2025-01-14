package com.andreich.androidhelper.presentation.game_result_screen

import com.andreich.androidhelper.domain.model.Question
import com.arkivanov.mvikotlin.core.store.Store
import com.andreich.androidhelper.presentation.game_result_screen.GameResultStore.*

interface GameResultStore : Store<Intent, State, Label> {

    data class State(
        val count: Int,
        val countRightAnswers: Int,
        val answersIds: List<Long>,
        val rightPercent: Int = 0,
        val answers: List<Question> = emptyList()
    )

    sealed interface Intent {

        class SaveArgs(val count: Int, val rightAnswersCount: Int, val ids: List<Long>) : Intent

        class LoadAnswers(val ids: List<Long>) : Intent
    }

    sealed interface Label {

    }
}