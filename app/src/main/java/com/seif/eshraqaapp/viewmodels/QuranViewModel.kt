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
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.data.repository.RepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QuranViewModel(application: Application): AndroidViewModel(application) {
    private val eshrakaDatabaseDao = EshrakaDatabase.getInstance(application).myDao()
    private val repository = RepositoryImp(eshrakaDatabaseDao)
    val quran: LiveData<List<Quran>> = repository.getAllQuranData()
    private lateinit var weekDate : ArrayList<MyDate>
    private lateinit var shared: SharedPreferences
    // private lateinit var  scoreList:List<Int>
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    var message = application.getString(R.string.first_week_quran_message)

    fun addQuran(quran: List<Quran>){
        viewModelScope.launch(Dispatchers.IO) {
            quran.forEach {
                repository.addQuran(it)
            }
        }
    }
    fun updateQuran(quran: Quran) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateQuran(quran)
        }
    }
    fun deleteAllQuran(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllQuran()
        }
    }
    fun isAppFirstTimeRun(context: Context):Boolean {
        shared = context.getSharedPreferences("isFirstTimeQuranDays", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {
            pref = context.getSharedPreferences("quranPrefs", Context.MODE_PRIVATE)
            edit = pref.edit()
            edit.putInt("nweek",1)
            edit.apply()
            val editor = shared.edit()
            editor.putBoolean("check", false)
            editor.apply()
            return true
        }
        return false
    }
    fun readIsFirstTimeToEnter(context: Context):Boolean {
        shared = context.getSharedPreferences("isFirstTimeEnterQuran", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {
            return true
        }
        return false
    }
    fun writeIsFirstTimeToEnter(){
        val editor = shared.edit()
        editor.putBoolean("check", false)
        editor.apply()
    }
    fun getSevenDaysData(numberOfDaysToRead:Int): List<Quran> {

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

        val quranHashMap: HashMap<String, Boolean> = createQuranHashMap()
        val updateStatusDate = getUpdateStatusDate()

        return listOf(
            Quran(
                1,
                quranHashMap,
                weekDate[0].day.toInt(),
                weekDate[0].month.toInt(),
                weekDate[0].year.toInt(),
                daysOfWeek[0],
                0,
                numberOfDaysToRead,
                message
            ),
            Quran(
                2,
                quranHashMap,
                weekDate[1].day.toInt(),
                weekDate[1].month.toInt(),
                weekDate[1].year.toInt(),
                daysOfWeek[1],
                0,
                numberOfDaysToRead,
                message
            ),
            Quran(
                3,
                quranHashMap,
                weekDate[2].day.toInt(),
                weekDate[2].month.toInt(),
                weekDate[2].year.toInt(),
                daysOfWeek[2],
                0,
                numberOfDaysToRead,
                message
            ),
            Quran(
                4,
                quranHashMap,
                weekDate[3].day.toInt(),
                weekDate[3].month.toInt(),
                weekDate[3].year.toInt(),
                daysOfWeek[3],
                0,
                numberOfDaysToRead,
                message
            ),
            Quran(
                5,
                quranHashMap,
                weekDate[4].day.toInt(),
                weekDate[4].month.toInt(),
                weekDate[4].year.toInt(),
                daysOfWeek[4],
                0,
                numberOfDaysToRead,
                message
            ),
            Quran(
                6,
                quranHashMap,
                weekDate[5].day.toInt(),
                weekDate[5].month.toInt(),
                weekDate[5].year.toInt(),
                daysOfWeek[5],
                0,
                numberOfDaysToRead,
                message
            ),
            Quran(
                7,
                quranHashMap,
                weekDate[6].day.toInt(),
                weekDate[6].month.toInt(),
                weekDate[6].year.toInt(),
                daysOfWeek[6],
                0,
                numberOfDaysToRead,
                message
            )
        )
    }

    private fun createQuranHashMap(): HashMap<String, Boolean> {
        val azkarHashMap = HashMap<String, Boolean>()
        azkarHashMap["question1"] = false
        azkarHashMap["question2"] = false
        azkarHashMap["question3"] = false
        return azkarHashMap
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

    fun createNewWeekSchedule(lastQuranDay: Azkar, newQuranHashMap:HashMap<String, Boolean>, weeklyMessage:String):List<Azkar> {
        deleteAllQuran()
        val currentDate = Calendar.getInstance()
        currentDate.set(
            lastQuranDay.currentYear,
            lastQuranDay.currentMonth,
            lastQuranDay.currentDay + 1
        )
        val weekDate = ArrayList<MyDate>()
        val daysOfWeek = ArrayList<String>()

        for (i in 0..6 step 1) {
            val day = currentDate.get(Calendar.DAY_OF_MONTH).toString()
            val month = currentDate.get(Calendar.MONTH).toString()
            val year = currentDate.get(Calendar.YEAR).toString()
            weekDate.add(MyDate(day, month, year))
            daysOfWeek.add(SimpleDateFormat("EEEE", Locale("ar")).format(currentDate.time))
            currentDate.add(Calendar.DATE, 1)
            Log.d("day", weekDate.toString() + daysOfWeek.toString() + currentDate.toString())
        }
        val hashMap = HashMap<String, Boolean>()
        newQuranHashMap.forEach { (key, value) ->
            hashMap[key] = false
        }
        val azkarHashMap: HashMap<String, Boolean> = hashMap
        val updateStatusDate =
            MyDate(lastQuranDay.checkDay, lastQuranDay.checkMonth, lastQuranDay.checkYear)

        return listOf(

        )
    }
}