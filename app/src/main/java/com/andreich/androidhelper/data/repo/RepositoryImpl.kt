package com.andreich.androidhelper.data.repo

import com.andreich.androidhelper.data.datasource.LocalDataSource
import com.andreich.androidhelper.data.entity.QuestionEntity
import com.andreich.androidhelper.data.mapper.EntityMapper
import com.andreich.androidhelper.data.mapper.ModelMapper
import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource,
    private val questionEntityMapper: EntityMapper<QuestionEntity, Question>,
    private val questionMapper: ModelMapper<Question, QuestionEntity>
) : Repository {

    override suspend fun getQuestion(id: Long): Question {
        return questionEntityMapper(dataSource.getQuestion(id))
    }

    override suspend fun insertQuestion(question: Question) {
        dataSource.insertQuestion(questionMapper(question))
    }

    override suspend fun chooseAnswer(chosenAnswer: String, answer: String): Boolean {
        return chosenAnswer == answer
    }

    override suspend fun getNewQuestionWithLimitations(excludedIds: List<Long>): Question? {
        return dataSource.getQuestionWithLimitation(excludedIds)?.let { questionEntityMapper(it) }
    }

    override fun getQuestionCount(): Flow<Int> {
        return dataSource.getQuestionCount()
    }

    override fun getQuestions(question: Question): Flow<List<Question>> {
        return dataSource.getAnswers(questionMapper(question)).map {
            it.map { entity ->
                questionEntityMapper(entity)
            }
        }
    }
}