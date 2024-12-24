package com.andreich.androidhelper.domain.usecase

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.repo.Repository
import javax.inject.Inject

class InsertQuestionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(question: Question) = repository.insertQuestion(question)
}