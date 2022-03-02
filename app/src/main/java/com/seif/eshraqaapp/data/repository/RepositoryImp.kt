package com.seif.eshraqaapp.data.repository

import androidx.lifecycle.LiveData
import com.seif.eshraqaapp.data.EshrakaDatabaseDao
import com.seif.eshraqaapp.data.models.Azkar
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

}