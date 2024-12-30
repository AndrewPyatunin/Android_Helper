package com.andreich.androidhelper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andreich.androidhelper.data.entity.QuestionEntity
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM question WHERE id = :id")
    suspend fun getQuestion(id: Long): QuestionEntity

    @Query("SELECT * FROM question WHERE id NOT IN (:excludedIds) ORDER BY RANDOM() LIMIT 1")
    suspend fun getQuestionLimitation(excludedIds: List<Long>): QuestionEntity

    @Query("SELECT * FROM question WHERE :subType IS NULL AND subject = :subject OR subType = :subType")
    fun getAnswers(subject: SubjectType, subType: SubType?): Flow<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)
}