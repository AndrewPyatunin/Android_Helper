package com.andreich.androidhelper.domain.repo

import com.andreich.androidhelper.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getQuestion(id: Long): Question

    suspend fun insertQuestion(question: Question)

    suspend fun chooseAnswer(chosenAnswer: String, answer: String): Boolean

    suspend fun getNewQuestionWithLimitations(excludedIds: List<Long>): Question?

    fun getQuestionCount(): Flow<Int>

    fun getQuestions(question: Question): Flow<List<Question>>
}