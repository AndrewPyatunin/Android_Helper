package com.andreich.androidhelper.domain.model

import kotlinx.serialization.Serializable


@Serializable
enum class SubjectType {
    Kotlin, Java, AndroidSdk
}
@Serializable
enum class SubType {
    Generic, Collections, Types, Objects, Functions
}