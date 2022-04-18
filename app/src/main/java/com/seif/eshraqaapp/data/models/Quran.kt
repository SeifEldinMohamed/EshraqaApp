package com.seif.eshraqaapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Entity
@Parcelize
data class Quran(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var quran: HashMap<String, String>,
    var date:String,
    var dayName: String,
    var mCalendar:Calendar,
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
