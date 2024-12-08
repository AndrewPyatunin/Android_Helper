package com.andreich.androidhelper.domain.model

data class Answer(
    val id: Int,
    val title: String,
    val subject: SubjectType,
    val subType: SubType? = null,
)
