package com.seif.eshraqaapp.data.repository

import androidx.lifecycle.LiveData
import com.seif.eshraqaapp.data.EshrakaDatabaseDao
import com.seif.eshraqaapp.data.models.Azkar

class RepositoryImp(private val eshrakaDatabaseDao: EshrakaDatabaseDao) : Repository {
    override suspend fun addZekr(zekr: Azkar) {
        eshrakaDatabaseDao.addZekr(zekr)
    }

    override fun getAllData(): LiveData<List<Azkar>> {
      return  eshrakaDatabaseDao.getAllData()
    }
}