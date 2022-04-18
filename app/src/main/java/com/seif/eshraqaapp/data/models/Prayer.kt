package com.seif.eshraqaapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Entity
@Parcelize
data class Prayer(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val prayersHashMap: HashMap<String, Boolean>,
    val qadaaHashMap: HashMap<String, Boolean>,
    val sonnHashMap: HashMap<String, Boolean>,
    val date:String,
    val dayName: String,
    val mCalendar:Calendar,
    val prayerScore: Int,
    val qadaaScore: Int,
    val sonnScore: Int,
    val totalScore:Int,
    val weeklyUserMessage: String,
    val isVacation: Boolean,
    ):Parcelable
