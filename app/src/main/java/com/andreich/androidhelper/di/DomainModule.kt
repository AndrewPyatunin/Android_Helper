package com.andreich.androidhelper.di

import com.andreich.androidhelper.data.repo.RepositoryImpl
import com.andreich.androidhelper.domain.repo.Repository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @AppScope
    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository
}