package com.andreich.androidhelper.data.entity

import androidx.room.TypeConverter
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomConverter {

    @TypeConverter
    fun fromStringToSubjectType(value: String?): SubjectType? {
        if (value == null) return null
        return Gson().fromJson(value, object : TypeToken<SubjectType>() {}.type)
    }

    @TypeConverter
    fun fromStringToSubType(value: String?): SubType? {
        if (value == null) return null
        return Gson().fromJson(value, object : TypeToken<SubType>() {}.type)
    }

    @TypeConverter
    fun fromSubTypeToString(value: SubType?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromSubjectTypeToString(value: SubjectType?): String {
        return Gson().toJson(value)
    }
}