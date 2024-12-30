package com.andreich.androidhelper.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType

@Entity("question")
data class QuestionEntity(
    @PrimaryKey
    val id: Long = -1,
    val title: String,
    val subject: SubjectType,
    val answer: String,
    val subType: SubType? = null,
    val answerType: Boolean? = null,
)
