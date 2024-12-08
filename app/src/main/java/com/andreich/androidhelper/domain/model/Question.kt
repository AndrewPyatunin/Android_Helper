package com.andreich.androidhelper.domain.model

data class Question(
    val id: Int,
    val title: String,
    val subject: SubjectType,
    val answer: String,
    val subType: SubType? = null,
    val answerType: Boolean? = null,
)
