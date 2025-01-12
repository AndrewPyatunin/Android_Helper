package com.andreich.androidhelper.presentation.add_question

import com.andreich.androidhelper.presentation.AnswerType
import com.andreich.androidhelper.presentation.QuestionUi
import kotlinx.coroutines.flow.StateFlow

interface AddQuestionComponent {

    val model: StateFlow<AddQuestionStore.State>

    fun onSaveClick(question: QuestionUi)

    fun onAnswerChange(answer: String, answerType: AnswerType)

    fun onQuestionTitleChange(title: String)

    fun onTypeChange(type: String)

    fun onSubjectChange(subjectType: String)

    fun trySave(
        questionTitle: String,
        answer: String,
        wrongAnswer1: String,
        wrongAnswer2: String,
        wrongAnswer3: String
    )
}