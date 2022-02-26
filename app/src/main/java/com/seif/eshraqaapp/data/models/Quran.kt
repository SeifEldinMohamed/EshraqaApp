package com.seif.eshraqaapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quran(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var quran: HashMap<String, Boolean>,
    var currentDay:Int,
    var currentMonth:Int,
    var currentYear:Int,
    var dayName: String,
    var score: Int,
    var numberOfDaysToRead: Int,
    var weeklyUserMessage: String
)
