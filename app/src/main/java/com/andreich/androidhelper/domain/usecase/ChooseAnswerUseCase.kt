package com.andreich.androidhelper.domain.usecase

import com.andreich.androidhelper.domain.repo.Repository
import javax.inject.Inject

class ChooseAnswerUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(answerId: Int, questionId: Int): Boolean {
        return repository.chooseAnswer(answerId, questionId)
    }
}