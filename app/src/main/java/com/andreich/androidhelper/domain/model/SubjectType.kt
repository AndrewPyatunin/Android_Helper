package com.andreich.androidhelper.domain.model

sealed interface Type

sealed interface SubjectType : Type {

    object Kotlin : SubjectType

    object Java : SubjectType

    object AndroidSdk : SubjectType
}

sealed interface TypeAndroid {

}

sealed interface SubType : Type {

    data object Generic : SubType

    data object Collections : SubType

    data object Types : SubType

    data object Objects : SubType

    data object Functions : SubType

}