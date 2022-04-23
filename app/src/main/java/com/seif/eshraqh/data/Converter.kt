package com.seif.eshraqh.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap

class Converter {

    @TypeConverter
    fun fromMapToGson(azkar: HashMap<String, Boolean>): String {
        return Gson().toJson(azkar)
    }

    @TypeConverter
    fun fromJsonToMap(azkar: String): HashMap<String, Boolean> {
        val listType = object : TypeToken<HashMap<String, Boolean>?>() {}.type
        return Gson().fromJson(azkar, listType)
    }

    @TypeConverter
    fun fromMapToGsonQuran(azkar: HashMap<String, String>): String {
        return Gson().toJson(azkar)
    }

    @TypeConverter
    fun fromJsonToMapQuran(azkar: String): HashMap<String, String> {
        val listType = object : TypeToken<HashMap<String, String>?>() {}.type
        return Gson().fromJson(azkar, listType)
    }

    // calendar
    @TypeConverter
    fun fromCalendarToGson(mCalendar: Calendar): String {
        return Gson().toJson(mCalendar)
    }

    @TypeConverter
    fun fromJsonToCalendar(mCalendar: String): Calendar {
        return Gson().fromJson(mCalendar, Calendar::class.java)
    }

}