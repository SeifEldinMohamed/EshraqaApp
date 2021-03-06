package com.seif.eshraqh.data.repository

import androidx.lifecycle.LiveData
import com.seif.eshraqh.data.models.Azkar
import com.seif.eshraqh.data.models.Prayer
import com.seif.eshraqh.data.models.Quran

interface Repository {
    suspend fun addZekr(zekr: Azkar)
    fun getAllData(): LiveData<List<Azkar>>
    suspend fun updateZekr(zekr: Azkar)
    fun getAllWeekScore(): LiveData<List<Int>>
    suspend fun deleteAllAzkar()

    // quran
    suspend fun addQuran(quran: Quran)
    fun getAllQuranData(): LiveData<List<Quran>>
    suspend fun updateQuran(quran: Quran)
    fun getAllQuranWeekScore(): LiveData<List<Int>>
    suspend fun deleteAllQuran()
    fun getVacationDaysNumber(): LiveData<Int>

    // prayer
    suspend fun addPrayer(prayer: Prayer)
    fun getAllPrayerData(): LiveData<List<Prayer>>
    suspend fun updatePrayer(prayer: Prayer)
    fun getAllPrayerWeekScore(): LiveData<List<Int>>
    suspend fun deleteAllPrayer()
    fun getPrayerVacationDaysNumber(): LiveData<Int>
}