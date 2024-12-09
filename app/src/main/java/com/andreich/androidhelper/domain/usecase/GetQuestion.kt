package com.andreich.androidhelper.domain.usecase

import com.andreich.androidhelper.domain.repo.Repository
import javax.inject.Inject

class GetQuestion @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: Int) = repository.getQuestion(id)
}