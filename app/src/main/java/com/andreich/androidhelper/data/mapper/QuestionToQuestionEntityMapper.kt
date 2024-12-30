package com.andreich.androidhelper.data.mapper

import com.andreich.androidhelper.data.entity.QuestionEntity
import com.andreich.androidhelper.domain.model.Question
import javax.inject.Inject

class QuestionToQuestionEntityMapper @Inject constructor() : ModelMapper<Question, QuestionEntity> {

    override fun invoke(from: Question): QuestionEntity {
        return with(from) {
            QuestionEntity(id, title, subject, answer, subType, answerType)
        }
    }
}