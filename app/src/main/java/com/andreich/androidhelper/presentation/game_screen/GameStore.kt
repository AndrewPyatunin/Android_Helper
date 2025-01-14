package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.domain.model.Question
import com.arkivanov.mvikotlin.core.store.Store

interface GameStore : Store<GameStore.Intent, GameStore.State, GameStore.Label> {

    data class State(
        val isLoading: Boolean,
        val question: Question?,
        val answers: List<AnswerCard>,
        val excludedIds: List<Long> = emptyList(),
        val rightAnswersCount: Int = 0,
        val count: Int = 0,
        val isClickable: Boolean = true,
        val remainTime: Int = 30
    )

    sealed interface Intent {

        data class PutCount(val count: Int) : Intent

        data class LoadQuestion(val excludedIds: List<Long>) : Intent

        data class LoadAnswers(val question: Question) : Intent

        data class ChooseAnswer(val chosenAnswer: String, val answer: String, val excludedIds: List<Long>) : Intent

        data class ExcludeUsedIds(val excludedIds: List<Long>) : Intent

        object LoadNewQuestion : Intent

        class StartTimer(val remainTime: Int) : Intent

        object Clear : Intent
    }

    sealed interface Label {

        object LastAnswer : Label

        class Answer(val excludedIds: List<Long>) : Label

        object NextAnswer : Label

        object QuestionIsReady : Label
    }
}