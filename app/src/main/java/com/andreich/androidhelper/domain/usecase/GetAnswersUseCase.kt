package com.andreich.androidhelper.domain.usecase
import com.andreich.androidhelper.domain.repo.Repository
import javax.inject.Inject

class GetAnswersUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(ids: List<Long>) = repository.getAnswers(ids)
}