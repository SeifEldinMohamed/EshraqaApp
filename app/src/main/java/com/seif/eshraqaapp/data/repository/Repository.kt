package com.seif.eshraqaapp.data.repository

import androidx.lifecycle.LiveData
import com.seif.eshraqaapp.data.models.Azkar

interface Repository {
    suspend fun addZekr(zekr: Azkar)
    fun getAllData(): LiveData<List<Azkar>>
    suspend fun updateZekr(zekr:Azkar)
    fun getAllWeekScore(): LiveData<List<Int>>
    suspend fun deleteAllAzkar()

}