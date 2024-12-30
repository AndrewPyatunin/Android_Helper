package com.andreich.androidhelper.data.mapper

import com.andreich.androidhelper.data.entity.QuestionEntity
import com.andreich.androidhelper.domain.model.Question
import javax.inject.Inject

class QuestionEntityToQuestionMapper @Inject constructor() : EntityMapper<QuestionEntity, Question> {

    override fun invoke(from: QuestionEntity): Question {
        return with(from) {
            Question(
                id = id,
                title = title,
                subject = subject,
                answer = answer,
                subType = subType,
                answerType = answerType
            )
        }
    }
}