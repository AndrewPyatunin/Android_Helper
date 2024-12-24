package com.andreich.androidhelper.data.database

import android.content.Context
import androidx.room.*
import com.andreich.androidhelper.data.entity.QuestionEntity
import com.andreich.androidhelper.data.entity.RoomConverter

@Database(entities = [QuestionEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [RoomConverter::class])
abstract class QuestionDatabase : RoomDatabase() {

    companion object {

        var instance: QuestionDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "questionDatabase.db"

        fun getInstance(context: Context): QuestionDatabase {
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                instance = Room.databaseBuilder(context, QuestionDatabase::class.java, DB_NAME).build()
                return instance as QuestionDatabase
            }
        }
    }

    abstract fun questionDao(): QuestionDao
}