package com.seif.eshraqaapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seif.eshraqaapp.data.models.MyDate

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

}