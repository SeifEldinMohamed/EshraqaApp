package com.seif.eshraqaapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seif.eshraqaapp.data.EshrakaDatabase
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.repository.RepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AzkarViewModel(application: Application) : AndroidViewModel(application) {
    private val eshrakaDatabaseDao = EshrakaDatabase.getInstance(application).myDao()
    val repository = RepositoryImp(eshrakaDatabaseDao)
    val azkar: LiveData<List<Azkar>> = repository.getAllData()

    fun addZekr(zekr: Azkar){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addZekr(zekr)
        }
    }

}