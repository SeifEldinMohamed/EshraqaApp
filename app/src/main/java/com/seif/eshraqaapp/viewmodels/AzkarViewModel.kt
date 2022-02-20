package com.seif.eshraqaapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seif.eshraqaapp.data.EshrakaDatabase
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.models.MyDate
import com.seif.eshraqaapp.data.repository.RepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AzkarViewModel(application: Application) : AndroidViewModel(application) {
    private val eshrakaDatabaseDao = EshrakaDatabase.getInstance(application).myDao()
    val repository = RepositoryImp(eshrakaDatabaseDao)
    val azkar: LiveData<List<Azkar>> = repository.getAllData()
    private lateinit var shared: SharedPreferences


    fun addZekr(azkar: List<Azkar>) {
        viewModelScope.launch(Dispatchers.IO) {
            azkar.forEach {
                repository.addZekr(it)
            }
        }
    }

    fun isAppFirstTimeRun(context: Context) {
        shared = context.getSharedPreferences("isFirstTime", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {
            val azkar = getSevenDaysData()
            addZekr(azkar)
            val editor = shared.edit()
            editor.putBoolean("check", false)
            editor.apply()
        }
    }

    fun getSevenDaysData(): List<Azkar> {
        val currentDate = Calendar.getInstance()
        val weekDate = ArrayList<MyDate>()
        val daysOfWeek = ArrayList<String>()

        for (i in 0..6 step 1) {
            val day = currentDate.get(Calendar.DAY_OF_MONTH).toString()
            val month = currentDate.get(Calendar.MONTH) + 1
            val year = currentDate.get(Calendar.YEAR).toString()
            weekDate.add(MyDate(day, month.toString(), year))
            daysOfWeek.add(SimpleDateFormat("EEEE", Locale("ar")).format(currentDate.time))
            currentDate.add(Calendar.DATE, 1)
        }
        val azkarHashMap: HashMap<String, Boolean> = createAzkarHashMap()
        val updateStatusDate = getUpdateStatusDate()

        return listOf(
            Azkar(
                1,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[0].day} / " + "${weekDate[0].month} / " + weekDate[0].year,
                daysOfWeek[0],
                0
            ),
            Azkar(
                2,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[1].day} / " + "${weekDate[1].month} / " + weekDate[1].year,
                daysOfWeek[1],
                0
            ),
            Azkar(
                3,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[2].day} / " + "${weekDate[2].month} / " + weekDate[2].year,
                daysOfWeek[2],
                0
            ),
            Azkar(
                4,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[3].day} / " + "${weekDate[3].month} / " + weekDate[3].year,
                daysOfWeek[3],
                0
            ),
            Azkar(
                5,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[4].day} / " + "${weekDate[4].month} / " + weekDate[4].year,
                daysOfWeek[4],
                0
            ),
            Azkar(
                6,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[5].day} / " + "${weekDate[5].month} / " + weekDate[5].year,
                daysOfWeek[5],
                0
            ),
            Azkar(
                7,
                azkarHashMap,
                updateStatusDate,
                "${weekDate[6].day} / " + "${weekDate[6].month} / " + weekDate[6].year,
                daysOfWeek[6],
                0
            )
        )
    }

    private fun getUpdateStatusDate(): MyDate {
        val currentDate = Calendar.getInstance()
        // update date after 30 days from start date of the schedule
        currentDate.add(Calendar.DATE, 30)
        val month = currentDate.get(Calendar.MONTH) + 1
        return MyDate(
            currentDate.get(Calendar.DAY_OF_MONTH).toString(),
            month.toString(),
            currentDate.get(Calendar.YEAR).toString()
        )
    }

    private fun createAzkarHashMap(): HashMap<String, Boolean> {
        val azkarHashMap = HashMap<String, Boolean>()
        azkarHashMap["أذكار الصباح"] = false
        azkarHashMap["أذكار النوم"] = false
        azkarHashMap["أذكار المساء"] = false
        azkarHashMap["أذكار بعد الصلاة"] = false
        return azkarHashMap
    }

}