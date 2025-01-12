package com.andreich.androidhelper.presentation.add_question

import com.andreich.androidhelper.presentation.AnswerType
import com.andreich.androidhelper.presentation.QuestionUi
import com.arkivanov.mvikotlin.core.store.Store

interface AddQuestionStore :
    Store<AddQuestionStore.Intent, AddQuestionStore.State, AddQuestionStore.Label> {

    data class State(
        val questionTitle: String,
        val answer: String,
        val type: String,
        val subjectType: String,
        val wrongAnswer1: String,
        val wrongAnswer2: String,
        val wrongAnswer3: String,
    )

    sealed interface Intent {

        class SaveQuestion(val question: QuestionUi) : Intent

        class ChangeQuestionTitle(val title: String) : Intent

        class ChangeAnswer(val answer: String, val answerType: AnswerType) : Intent

        class ChangeType(val type: String) : Intent

        class ChangeSubject(val subjectType: String) : Intent

        class TryToSave(
            val questionTitle: String,
            val answer: String,
            val wrongAnswer1: String,
            val wrongAnswer2: String,
            val wrongAnswer3: String
        ) : Intent
    }

    sealed interface Label {

        object SaveSuccessful : Label
    }
}