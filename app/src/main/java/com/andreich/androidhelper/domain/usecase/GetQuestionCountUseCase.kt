package com.andreich.androidhelper.domain.usecase

import com.andreich.androidhelper.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionCountUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(): Flow<Int> =
        repository.getQuestionCount()
}