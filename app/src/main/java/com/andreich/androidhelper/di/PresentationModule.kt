package com.andreich.androidhelper.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @AppScope
    @Provides
    fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()
}