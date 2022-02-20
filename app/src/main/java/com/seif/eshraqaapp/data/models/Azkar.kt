package com.seif.eshraqaapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.HashMap

@Entity(tableName = "azkar_table")
data class Azkar(
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var azkar: HashMap<String, Boolean>,
    var checkDate: MyDate,
    var date: String,
    var day: String,
    var score: Int
)
