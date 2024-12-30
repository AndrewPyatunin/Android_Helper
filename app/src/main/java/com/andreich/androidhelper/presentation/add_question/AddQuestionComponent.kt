package com.andreich.androidhelper.presentation.add_question

import com.andreich.androidhelper.presentation.QuestionUi
import kotlinx.coroutines.flow.StateFlow

interface AddQuestionComponent {

    val model: StateFlow<AddQuestionStore.State>

    fun onSaveClick(question: QuestionUi)

    fun onAnswerChange(answer: String)

    fun onQuestionTitleChange(title: String)

    fun onTypeChange(type: String)

    fun onSubjectChange(subjectType: String)
}