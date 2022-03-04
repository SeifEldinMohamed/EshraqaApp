package com.seif.eshraqaapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlin.collections.HashMap

@Entity(tableName = "azkar_table")
@Parcelize
data class Azkar(
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var azkar: HashMap<String, Boolean>,
    var checkDay:String,
    var checkMonth: String,
    var checkYear: String,
    var date: String,
    var currentDay:Int,
    var currentMonth:Int,
    var currentYear:Int,
    var dayName: String,
    var score: Int,
    var weeklyUserMessage: String
): Parcelable
