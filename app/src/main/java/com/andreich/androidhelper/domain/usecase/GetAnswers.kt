package com.andreich.androidhelper.domain.usecase

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.repo.Repository
import javax.inject.Inject

class GetAnswers @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(question: Question) = repository.getQuestions(question)
}