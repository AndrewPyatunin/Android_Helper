package com.andreich.androidhelper.domain.repo

import com.andreich.androidhelper.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getQuestion(id: Long): Question

    suspend fun insertQuestion(question: Question)

    suspend fun chooseAnswer(answerId: Long, questionId: Long): Boolean

    suspend fun getNewQuestionWithLimitations(excludedIds: List<Long>): Question

    fun getQuestions(question: Question): Flow<List<Question>>
}