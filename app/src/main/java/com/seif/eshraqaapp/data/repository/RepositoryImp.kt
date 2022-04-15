package com.seif.eshraqaapp.data.repository

import androidx.lifecycle.LiveData
import com.seif.eshraqaapp.data.EshrakaDatabaseDao
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.models.Quran

class RepositoryImp(private val eshrakaDatabaseDao: EshrakaDatabaseDao) : Repository {
    override suspend fun addZekr(zekr: Azkar) {
        eshrakaDatabaseDao.addZekr(zekr)
    }

    override fun getAllData(): LiveData<List<Azkar>> {
      return  eshrakaDatabaseDao.getAllData()
    }

    override suspend fun updateZekr(zekr: Azkar) {
        eshrakaDatabaseDao.updateZekr(zekr)
    }

    override  fun getAllWeekScore(): LiveData<List<Int>> {
       return eshrakaDatabaseDao.getAllWeekScore()
    }

    override suspend fun deleteAllAzkar() {
        eshrakaDatabaseDao.deleteAllAzkar()
    }

    // quran

    override suspend fun addQuran(quran: Quran) {
        eshrakaDatabaseDao.addQuran(quran)
    }

    override fun getAllQuranData(): LiveData<List<Quran>> {
        return eshrakaDatabaseDao.getAllQuranData()
    }

    override suspend fun updateQuran(quran: Quran) {
        eshrakaDatabaseDao.updateQuran(quran)
    }

    override fun getAllQuranWeekScore(): LiveData<List<Int>> {
        return  eshrakaDatabaseDao.getAllQuranWeekScore()
    }

    override suspend fun deleteAllQuran() {
        eshrakaDatabaseDao.deleteAllQuran()
    }

    override fun getVacationDaysNumber(): LiveData<Int> {
        return eshrakaDatabaseDao.getVacationDaysNumber()
    }

    // prayer
    override suspend fun addPrayer(prayer: Prayer) {
         eshrakaDatabaseDao.addPrayer(prayer)
    }

    override fun getAllPrayerData(): LiveData<List<Prayer>> {
        return eshrakaDatabaseDao.getAllPrayerData()
    }

    override suspend fun updatePrayer(prayer: Prayer) {
        eshrakaDatabaseDao.updatePrayer(prayer)
    }

    override fun getAllPrayerWeekScore(): LiveData<List<Int>> {
        return eshrakaDatabaseDao.getAllPrayerWeekScore()
    }

    override suspend fun deleteAllPrayer() {
        eshrakaDatabaseDao.deleteAllPrayer()
    }

    override fun getPrayerVacationDaysNumber(): LiveData<Int> {
        return eshrakaDatabaseDao.getPrayerVacationDaysNumber()
    }


}