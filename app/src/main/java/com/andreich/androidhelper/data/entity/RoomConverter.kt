package com.andreich.androidhelper.data.entity

import androidx.room.TypeConverter
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.google.gson.Gson
import com.google.gson.InstanceCreator
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.serializer
import java.lang.reflect.Type

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