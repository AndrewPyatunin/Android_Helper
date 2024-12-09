package com.andreich.androidhelper.di

import com.andreich.androidhelper.data.datasource.LocalDataSource
import com.andreich.androidhelper.data.datasource.LocalDataSourceImpl
import com.andreich.androidhelper.data.entity.QuestionEntity
import com.andreich.androidhelper.data.mapper.EntityMapper
import com.andreich.androidhelper.data.mapper.ModelMapper
import com.andreich.androidhelper.data.mapper.QuestionEntityToQuestionMapper
import com.andreich.androidhelper.data.mapper.QuestionToQuestionEntityMapper
import com.andreich.androidhelper.domain.model.Question
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @AppScope
    @Binds
    fun bindLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource

    @AppScope
    @Binds
    fun bindQuestionEntityMapper(impl: QuestionEntityToQuestionMapper): EntityMapper<QuestionEntity, Question>

    @AppScope
    @Binds
    fun bindQuestionModelMapper(impl: QuestionToQuestionEntityMapper): ModelMapper<Question, QuestionEntity>
}