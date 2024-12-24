package com.andreich.androidhelper.domain.model

sealed interface SubjectType {

    object Kotlin : SubjectType

    object Java : SubjectType

    data class AndroidSdk(val subType: TypeAndroid) : SubjectType
}

sealed interface TypeAndroid {

}

sealed interface SubType {

    data object Generic : SubType

    data object Collections : SubType

    data object Types : SubType

    data object Objects : SubType

    data object Functions : SubType

}