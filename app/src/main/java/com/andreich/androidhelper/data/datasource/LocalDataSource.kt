package com.andreich.androidhelper.data.datasource

import com.andreich.androidhelper.data.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getAnswers(ids: List<Long>): List<QuestionEntity>

    suspend fun getQuestionWithLimitation(excludedIds: List<Long>): QuestionEntity?

    suspend fun getQuestion(id: Long): QuestionEntity

    suspend fun insertQuestion(question: QuestionEntity)

    fun getQuestionCount(): Flow<Int>
}