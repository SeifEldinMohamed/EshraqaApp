package com.seif.eshraqaapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "azkar_table")
data class Azkar(
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var date: Date,
    var azkar: HashMap<String, Boolean>,
    var checkDate: Date,
    var score: Int
)
