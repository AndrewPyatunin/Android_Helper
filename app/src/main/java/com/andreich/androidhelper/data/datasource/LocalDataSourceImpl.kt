package com.andreich.androidhelper.data.datasource

import com.andreich.androidhelper.data.database.QuestionDao
import com.andreich.androidhelper.data.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dao: QuestionDao
) : LocalDataSource {

    override suspend fun getAnswers(ids: List<Long>): List<QuestionEntity> {
        return dao.getAnswers(ids)
    }

    override suspend fun getQuestionWithLimitation(excludedIds: List<Long>): QuestionEntity? {
        return dao.getQuestionLimitation(excludedIds)
    }

    override suspend fun getQuestion(id: Long): QuestionEntity {
        return dao.getQuestion(id)
    }

    override suspend fun insertQuestion(question: QuestionEntity) = dao.insertQuestion(question)

    override fun getQuestionCount(): Flow<Int> = dao.countQuestions()
}