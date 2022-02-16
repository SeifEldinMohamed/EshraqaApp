package com.seif.eshraqaapp.data.repository

import androidx.lifecycle.LiveData
import com.seif.eshraqaapp.data.models.Azkar

interface Repository {
    suspend fun addZekr(zekr: Azkar)
    fun getAllData(): LiveData<List<Azkar>>
}