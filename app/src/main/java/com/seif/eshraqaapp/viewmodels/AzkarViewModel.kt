package com.seif.eshraqaapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seif.eshraqaapp.R
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
    private lateinit var weekDate: ArrayList<MyDate>
    private lateinit var shared: SharedPreferences

    // private lateinit var  scoreList:List<Int>
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    var message = application.getString(R.string.first_week_azkar_message)

    fun addZekr(azkar: List<Azkar>) {
        viewModelScope.launch(Dispatchers.IO) {
            azkar.forEach {
                repository.addZekr(it)
            }
        }
    }

    fun updateZekr(azkar: Azkar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateZekr(azkar)
        }
    }

    fun deleteAllAzkar() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAzkar()
        }
    }

    fun isAppFirstTimeRun(context: Context) {
        shared = context.getSharedPreferences("isFirstTime", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {
            val azkar = getSevenDaysData()
            pref = context.getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
            edit = pref.edit()
            edit.putInt("nweek", 1)
            edit.apply()
            addZekr(azkar)
            val editor = shared.edit()
            editor.putBoolean("check", false)
            editor.apply()
        }
    }

    fun getSevenDaysData(): List<Azkar> {

        val currentDate = Calendar.getInstance()
        weekDate = ArrayList<MyDate>()
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
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[0].day} / " + "${weekDate[0].month} / " + weekDate[0].year,
                weekDate[0].day.toInt(),
                weekDate[0].month.toInt(),
                weekDate[0].year.toInt(),
                daysOfWeek[0],
                0,
                message
            ),
            Azkar(
                2,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[1].day} / " + "${weekDate[1].month} / " + weekDate[1].year,
                weekDate[1].day.toInt(),
                weekDate[1].month.toInt(),
                weekDate[1].year.toInt(),
                daysOfWeek[1],
                0,
                message
            ),
            Azkar(
                3,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[2].day} / " + "${weekDate[2].month} / " + weekDate[2].year,
                weekDate[2].day.toInt(),
                weekDate[2].month.toInt(),
                weekDate[2].year.toInt(),
                daysOfWeek[2],
                0,
                message
            ),
            Azkar(
                4,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[3].day} / " + "${weekDate[3].month} / " + weekDate[3].year,
                weekDate[3].day.toInt(),
                weekDate[3].month.toInt(),
                weekDate[3].year.toInt(),
                daysOfWeek[3],
                0,
                message
            ),
            Azkar(
                5,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[4].day} / " + "${weekDate[4].month} / " + weekDate[4].year,
                weekDate[4].day.toInt(),
                weekDate[4].month.toInt(),
                weekDate[4].year.toInt(),
                daysOfWeek[4],
                0,
                message
            ),
            Azkar(
                6,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[5].day} / " + "${weekDate[5].month} / " + weekDate[5].year,
                weekDate[5].day.toInt(),
                weekDate[5].month.toInt(),
                weekDate[5].year.toInt(),
                daysOfWeek[5],
                0,
                message
            ),
            Azkar(
                7,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[6].day} / " + "${weekDate[6].month} / " + weekDate[6].year,
                weekDate[6].day.toInt(),
                weekDate[6].month.toInt(),
                weekDate[6].year.toInt(),
                daysOfWeek[6],
                0,
                message
            )
        )
    }

    fun getAllWeekScore(): LiveData<List<Int>> {
        return repository.getAllWeekScore()
    }

    fun getSumOfWeekScore(scoreList: List<Int>): Int {
        var totalWeekScore = 0
        scoreList.forEach {
            totalWeekScore += it
        }
        return totalWeekScore
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

    fun createNewWeekSchedule(
        lastAzkarDay: Azkar,
        newAzkarHashMap: HashMap<String, Boolean>,
        weeklyMessage: String
    ): List<Azkar> {
        deleteAllAzkar()

        val currentDate = Calendar.getInstance()
        currentDate.set(
            lastAzkarDay.currentYear,
            lastAzkarDay.currentMonth,
            lastAzkarDay.currentDay + 1
        )
        val weekDate = ArrayList<MyDate>()
        val daysOfWeek = ArrayList<String>()
        currentDate.add(Calendar.MONTH, -1)

        for (i in 0..6 step 1) {
            val day = currentDate.get(Calendar.DAY_OF_MONTH).toString()
            val month = currentDate.get(Calendar.MONTH) + 1
            val year = currentDate.get(Calendar.YEAR).toString()
            Log.d("debug", "day: $day")
            Log.d("debug", "month: $month")
            Log.d("debug", "year: $year")
            Log.d("debug", currentDate.time.toString())
            weekDate.add(MyDate(day, month.toString(), year))
            daysOfWeek.add(SimpleDateFormat("EEEE", Locale("ar")).format(currentDate.time))
            Log.d("debug", daysOfWeek[i])
            currentDate.add(Calendar.DATE, 1)
            // Log.d("day", weekDate.toString() + daysOfWeek.toString() + currentDate.toString())
        }
        val hashMap = HashMap<String, Boolean>()
        newAzkarHashMap.forEach { (key, value) ->
            hashMap[key] = false
        }
        val azkarHashMap: HashMap<String, Boolean> = hashMap
        val updateStatusDate =
            MyDate(lastAzkarDay.checkDay, lastAzkarDay.checkMonth, lastAzkarDay.checkYear)

        return listOf(
            Azkar(
                1,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[0].day} / " + "${weekDate[0].month} / " + weekDate[0].year,
                weekDate[0].day.toInt(),
                weekDate[0].month.toInt(),
                weekDate[0].year.toInt(),
                daysOfWeek[0],
                0,
                weeklyMessage
            ),
            Azkar(
                2,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[1].day} / " + "${weekDate[1].month} / " + weekDate[1].year,
                weekDate[1].day.toInt(),
                weekDate[1].month.toInt(),
                weekDate[1].year.toInt(),
                daysOfWeek[1],
                0,
                weeklyMessage
            ),
            Azkar(
                3,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[2].day} / " + "${weekDate[2].month} / " + weekDate[2].year,
                weekDate[2].day.toInt(),
                weekDate[2].month.toInt(),
                weekDate[2].year.toInt(),
                daysOfWeek[2],
                0,
                weeklyMessage
            ),
            Azkar(
                4,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[3].day} / " + "${weekDate[3].month} / " + weekDate[3].year,
                weekDate[3].day.toInt(),
                weekDate[3].month.toInt(),
                weekDate[3].year.toInt(),
                daysOfWeek[3],
                0,
                weeklyMessage
            ),
            Azkar(
                5,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[4].day} / " + "${weekDate[4].month} / " + weekDate[4].year,
                weekDate[4].day.toInt(),
                weekDate[4].month.toInt(),
                weekDate[4].year.toInt(),
                daysOfWeek[4],
                0,
                weeklyMessage
            ),
            Azkar(
                6,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[5].day} / " + "${weekDate[5].month} / " + weekDate[5].year,
                weekDate[5].day.toInt(),
                weekDate[5].month.toInt(),
                weekDate[5].year.toInt(),
                daysOfWeek[5],
                0,
                weeklyMessage
            ),
            Azkar(
                7,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                "${weekDate[6].day} / " + "${weekDate[6].month} / " + weekDate[6].year,
                weekDate[6].day.toInt(),
                weekDate[6].month.toInt(),
                weekDate[6].year.toInt(),
                daysOfWeek[6],
                0,
                weeklyMessage
            )
        )
    }
}