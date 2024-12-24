package com.andreich.androidhelper.domain.repo

import com.andreich.androidhelper.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getQuestion(id: Int): Question

    suspend fun insertQuestion(question: Question)

    suspend fun chooseAnswer(answerId: Int, questionId: Int): Boolean

    fun getQuestions(question: Question): Flow<List<Question>>
}