package com.seif.eshraqaapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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
            // days
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
        val mCalendar: Calendar = Calendar.getInstance()

        val dateList = ArrayList<String>()
        val dayNameList= ArrayList<String>()
        val mFormat: SimpleDateFormat = SimpleDateFormat("EEE,yyyy/MM/dd", Locale("ar"))

        for (i in 0..6) {
            // Add name of day and date to array
            val dateAndDayList: List<String> = mFormat.format(mCalendar.time).split(",")
            dateList.add(dateAndDayList[1])
            dayNameList.add(dateAndDayList[0])
            // Move next day
            mCalendar.add(Calendar.DAY_OF_MONTH, 1)
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
                dateList[0],
                dayNameList[0],
                0,
                message,
                mCalendar
            ),
            Azkar(
                2,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[1],
                dayNameList[1],
                0,
                message,
                mCalendar
            ),
            Azkar(
                3,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[2],
                dayNameList[2],
                0,
                message,
                mCalendar
            ),
            Azkar(
                4,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[3],
                dayNameList[3],
                0,
                message,
                mCalendar
            ),
            Azkar(
                5,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[4],
                dayNameList[4],
                0,
                message,
                mCalendar
            ),
            Azkar(
                6,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[5],
                dayNameList[5],
                0,
                message,
                mCalendar
            ),
            Azkar(
                7,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[6],
                dayNameList[6],
                0,
                message,
                mCalendar
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
        weeklyMessage: String,
        mCalendar:Calendar
    ): List<Azkar> {
        deleteAllAzkar()

//        var date = LocalDate.of(
//            lastAzkarDay.currentYear,
//            lastAzkarDay.currentMonth,
//            lastAzkarDay.currentDay
//        )
//
//        val weekDate = ArrayList<MyDate>()
//        val daysOfWeek = ArrayList<String>()
//
//        for (i in 0..6 step 1) {
//            date = date.plusDays(1L)
//            val day = date.dayOfMonth.toString()
//            val month = date.monthValue.toString()
//            val year = date.year.toString()
//            Log.d("test","$day - $month - $year")
//
//            weekDate.add( MyDate(day, month, year))
//            daysOfWeek.add(getDayNameInArabic(date.dayOfWeek.toString()))
//        }
//        Log.d("test", weekDate.toString())
//        Log.d("test", daysOfWeek.toString())

//        weekDate = ArrayList<MyDate>()
//        val daysOfWeek = ArrayList<String>()
//        val month = lastAzkarDay.currentMonth -1
//        for (i in 1..7 step 1) {
//            val currentDate = GregorianCalendar()
//            currentDate.set( lastAzkarDay.currentYear, month, lastAzkarDay.currentDay)
//            currentDate.add(Calendar.DATE, i)
//            val day = currentDate.get(Calendar.DAY_OF_MONTH).toString()
//            val month = currentDate.get(Calendar.MONTH) + 1
//            val year = currentDate.get(Calendar.YEAR).toString()
//
//            weekDate.add(MyDate(day, month.toString(), year))
//            daysOfWeek.add(SimpleDateFormat("EEEE", Locale("ar")).format(currentDate.time))
//        }

        val dateList = ArrayList<String>()
        val dayNameList= ArrayList<String>()
        val mFormat: SimpleDateFormat = SimpleDateFormat("EEE,yyyy/MM/dd", Locale("ar"))

        for (i in 0..6) {
            // Add name of day and date to array
            val dateAndDayList: List<String> = mFormat.format(mCalendar.time).split(",")
            dateList.add(dateAndDayList[1])
            dayNameList.add(dateAndDayList[0])
            // Move next day
            mCalendar.add(Calendar.DAY_OF_MONTH, 1)
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
                dateList[0],
                dayNameList[0],
                0,
                weeklyMessage,
                mCalendar
            ),
            Azkar(
                2,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[1],
                dayNameList[1],
                0,
                weeklyMessage,
                mCalendar
            ),
            Azkar(
                3,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[2],
                dayNameList[2],
                0,
                weeklyMessage,
                mCalendar
            ),
            Azkar(
                4,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[3],
                dayNameList[3],
                0,
                weeklyMessage,
                mCalendar
            ),
            Azkar(
                5,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[4],
                dayNameList[4],
                0,
                weeklyMessage,
                mCalendar
            ),
            Azkar(
                6,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[5],
                dayNameList[5],
                0,
                weeklyMessage,
                mCalendar
            ),
            Azkar(
                7,
                azkarHashMap,
                updateStatusDate.day,
                updateStatusDate.month,
                updateStatusDate.year,
                dateList[6],
                dayNameList[6],
                0,
                weeklyMessage,
                mCalendar
            )
        )
    }
}