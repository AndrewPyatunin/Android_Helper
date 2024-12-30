package com.andreich.androidhelper.di

import android.content.Context
import com.andreich.androidhelper.MainActivity
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [DataModule::class, DomainModule::class, DatabaseModule::class, PresentationModule::class])
interface AppComponent {

    @Component.Factory
    interface ComponentFactory {

        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
}