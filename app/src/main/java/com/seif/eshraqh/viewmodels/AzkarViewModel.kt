package com.seif.eshraqh.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seif.eshraqh.R
import com.seif.eshraqh.data.EshrakaDatabase
import com.seif.eshraqh.data.models.Azkar
import com.seif.eshraqh.data.models.MyDate
import com.seif.eshraqh.data.repository.RepositoryImp
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
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    var message = application.getString(R.string.first_week_azkar_message)

    fun addZekr(azkar: Azkar) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("day", "entered : $azkar")
            repository.addZekr(azkar)
        }
    }


    fun updateZekr(azkar: Azkar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateZekr(azkar)
        }
    }

//    fun deleteAllAzkar() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteAllAzkar()
//        }
//    }

    fun isAppFirstTimeRun(context: Context) {
        shared = context.getSharedPreferences("isFirstTime", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {
            // days
            getSevenDaysData()
            pref = context.getSharedPreferences("settingPrefs", Context.MODE_PRIVATE)
            edit = pref.edit()
            edit.putInt("nweek", 1)
            edit.apply()
            // addZekr(azkar)
            val editor = shared.edit()
            editor.putBoolean("check", false)
            editor.apply()
        }
    }


    fun getSevenDaysData() {
        val mCalendar: Calendar = Calendar.getInstance()
        val dateList = ArrayList<String>()
        val dayNameList = ArrayList<String>()
        val mFormat = SimpleDateFormat("EEE,yyyy/MM/dd", Locale("ar"))

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

        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
        mCalendar: Calendar
    ) {
        val dateList = ArrayList<String>()
        val dayNameList = ArrayList<String>()
        val mFormat = SimpleDateFormat("EEE,yyyy/MM/dd", Locale("ar"))

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

        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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
            )
        )
        addZekr(
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