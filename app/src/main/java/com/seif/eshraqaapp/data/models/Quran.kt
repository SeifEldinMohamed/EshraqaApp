package com.seif.eshraqaapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Quran(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var quran: HashMap<String, String>,
    var currentDay:Int,
    var currentMonth:Int,
    var currentYear:Int,
    var dayName: String,
    var score: Int,
    var weeklyUserMessage: String,
    var firstTimeToEnter:Boolean,
    var numberOfSaveDaysToWork: Int,
    var numberOfReadDaysToWork: Int,
    var numberOfRevisionDaysToWork: Int,
    var isVacation: Boolean,
    var isSaveCounterNotFoundUsed: Boolean,
    var isReadCounterNotFoundUsed: Boolean,
    var isRevisionCounterNotFoundUsed: Boolean,
): Parcelable
