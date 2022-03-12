package com.seif.eshraqaapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Prayer(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val prayersHashMap: HashMap<String, Boolean>,
    val qadaaHashMap: HashMap<String, Boolean>,
    val sonnHashMap: HashMap<String, Boolean>,
    val currentDay:Int,
    val currentMonth:Int,
    val currentYear:Int,
    val dayName: String,
    val prayerScore: Int,
    val qadaaScore: Int,
    val sonnScore: Int,
    val totalScore:Int,
    val weeklyUserMessage: String,
    val isVacation: Boolean,
    ):Parcelable
