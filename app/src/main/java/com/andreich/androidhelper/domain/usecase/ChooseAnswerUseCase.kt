package com.andreich.androidhelper.domain.usecase

import com.andreich.androidhelper.domain.repo.Repository
import javax.inject.Inject

class ChooseAnswerUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(chosenAnswer: String, answer: String): Boolean {
        return repository.chooseAnswer(chosenAnswer, answer)
    }
}