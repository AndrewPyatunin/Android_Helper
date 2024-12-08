package com.andreich.androidhelper.domain.repo

import com.andreich.androidhelper.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getQuestion(id: Int): Flow<Question?>

    suspend fun insertQuestion(question: Question)

    fun getQuestions(question: Question): Flow<List<Question>>
}