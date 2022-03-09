package com.seif.eshraqaapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Prayer(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val prayers: HashMap<String, Boolean>,
    val prayersAndQadaa: HashMap<String, Boolean>,
    val prayersAndSonn: HashMap<String, Boolean>,
    val currentDay:Int,
    val currentMonth:Int,
    val currentYear:Int,
    val dayName: String,
    val score: Int,
    val weeklyUserMessage: String,
    val isVacation: Boolean,
    )
