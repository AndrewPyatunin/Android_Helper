package com.andreich.androidhelper.data.datasource

import com.andreich.androidhelper.data.database.QuestionDao
import com.andreich.androidhelper.data.entity.QuestionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dao: QuestionDao
) : LocalDataSource {

    override fun getAnswers(question: QuestionEntity): Flow<List<QuestionEntity>> {
        return dao.getAnswers(question.subject, question.subType)
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