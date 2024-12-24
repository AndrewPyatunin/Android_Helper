package com.andreich.androidhelper

import android.app.Application
import com.andreich.androidhelper.di.DaggerAppComponent

class MyApp : Application() {
    val component by lazy { DaggerAppComponent.factory().create(this) }
}