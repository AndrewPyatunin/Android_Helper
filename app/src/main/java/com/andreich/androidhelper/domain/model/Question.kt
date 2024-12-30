package com.andreich.androidhelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Long,
    val title: String,
    val subject: SubjectType,
    val answer: String,
    val subType: SubType? = null,
    val answerType: Boolean? = null,
)
