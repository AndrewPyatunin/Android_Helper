package com.andreich.androidhelper.presentation

data class QuestionUi(
    val id: Long,
    val title: String,
    val subject: String,
    val answer: String,
    val subType: String = "",
)
