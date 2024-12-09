package com.andreich.androidhelper.di

import android.content.Context
import com.andreich.androidhelper.data.database.QuestionDao
import com.andreich.androidhelper.data.database.QuestionDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @AppScope
    @Provides
    fun provideQuestionDatabase(context: Context): QuestionDatabase = QuestionDatabase.getInstance(context)

    @Provides
    fun provideQuestionDao(database: QuestionDatabase): QuestionDao = database.questionDao()
}