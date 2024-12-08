package com.andreich.androidhelper.domain.model

sealed interface SubjectType {

    object Kotlin : SubjectType

    object Java : SubjectType

    data class AndroidSdk(val subType: TypeAndroid) : SubjectType
}

sealed interface TypeAndroid {

}

sealed interface SubType {

    object Generic : SubType

    object Collections : SubType

    object Types : SubType

    object Objects : SubType

    object Functions : SubType

}