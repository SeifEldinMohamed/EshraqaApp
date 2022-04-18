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
import com.seif.eshraqaapp.data.models.MyDate
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.data.repository.RepositoryImp
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QuranViewModel(application: Application) : AndroidViewModel(application) {
    private val eshrakaDatabaseDao = EshrakaDatabase.getInstance(application).myDao()
    private val repository = RepositoryImp(eshrakaDatabaseDao)
    val quran: LiveData<List<Quran>> = repository.getAllQuranData()
    private lateinit var weekDate: ArrayList<MyDate>
    private lateinit var shared: SharedPreferences

    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    var message = application.getString(R.string.first_week_quran_message)
    val vacationDaysNumber: LiveData<Int> = repository.getVacationDaysNumber()


    fun addQuran(quran: List<Quran>) {
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

    fun deleteAllQuran() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllQuran()
        }
    }

    fun isAppFirstTimeRun(context: Context): Boolean {

        shared = context.getSharedPreferences("isFirstTimeQuranDays", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {
            // days
            pref = context.getSharedPreferences("quranPrefs", Context.MODE_PRIVATE)
            edit = pref.edit()
            edit.putInt("nweek", 1)
            edit.apply()
            val editor = shared.edit()
            editor.putBoolean("check", false)
            editor.apply()
            return true
        }
        return false
    }


//    fun readIsFirstTimeToEnter(context: Context): Boolean {
//        shared = context.getSharedPreferences("isFirstTimeEnterQuran", Context.MODE_PRIVATE)
//        if (shared.getBoolean("check", true)) {
//            return true
//        }
//        return false
//    }
//
//    fun writeIsFirstTimeToEnter() {
//        val editor = shared.edit()
//        editor.putBoolean("check", false)
//        editor.apply()
//    }

    fun getSevenDaysData(
        numberOfDaysToSave: Int,
        numberOfDaysToRead: Int,
        numberOfDaysToRevision: Int
    ): List<Quran> {

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

        val quranHashMap: HashMap<String, String> = createQuranHashMap()
     //   val updateStatusDate = getUpdateStatusDate()

        return listOf(
            Quran(
                1,
                quranHashMap,
                dateList[0],
                dayNameList[0],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                2,
                quranHashMap,
                dateList[1],
                dayNameList[1],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                3,
                quranHashMap,
                dateList[2],
                dayNameList[2],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                4,
                quranHashMap,
                dateList[3],
                dayNameList[3],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                5,
                quranHashMap,
                dateList[4],
                dayNameList[4],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                6,
                quranHashMap,
                dateList[5],
                dayNameList[5],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                7,
                quranHashMap,
                dateList[6],
                dayNameList[6],
                mCalendar,
                0,
                message,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            )
        )
    }

    private fun createQuranHashMap(): HashMap<String, String> {
        val quranHashMap = HashMap<String, String>()
        quranHashMap["question1"] = ""
        quranHashMap["question2"] = ""
        quranHashMap["question3"] = ""
        return quranHashMap
    }

    fun getAllQuranWeekScore(): LiveData<List<Int>> {
        return repository.getAllQuranWeekScore()
    }

    fun getSumOfQuranWeekScore(scoreList: List<Int>): Int {
        var totalWeekScore = 0
        scoreList.forEach {
            totalWeekScore += it
        }
        return totalWeekScore
    }
    private fun getDayNameInArabic(dayName:String): String {
        var dayNameInArabic = ""
        when(dayName){
            "FRIDAY" -> dayNameInArabic = "الجمعة"
            "SATURDAY" -> dayNameInArabic = "السبت"
            "SUNDAY" -> dayNameInArabic = "الأحد"
            "MONDAY" -> dayNameInArabic = "الاثنين"
            "TUESDAY" -> dayNameInArabic = "الثلاثاء"
            "WEDNESDAY" -> dayNameInArabic = "الأربعاء"
            "THURSDAY" -> dayNameInArabic = "الخميس"
        }
        return dayNameInArabic
    }

    fun createNewWeekSchedule(
        lastQuranDay: Quran,
        newQuranHashMap: HashMap<String, String>,
        weeklyMessage: String,
        numberOfDaysToSave: Int,
        numberOfDaysToRead: Int,
        numberOfDaysToRevision: Int,
        mCalendar:Calendar
    ): List<Quran> {
        deleteAllQuran()

//        var date = LocalDate.of(lastQuranDay.currentYear,
//            lastQuranDay.currentMonth, lastQuranDay.currentDay)
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
//            daysOfWeek.add( getDayNameInArabic(date.dayOfWeek.toString()))
//        }
//        Log.d("test", weekDate.toString())
//        Log.d("test", daysOfWeek.toString())

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

        val hashMap = HashMap<String, String>()
        newQuranHashMap.forEach { (key, value) ->
            hashMap[key] = ""
        }
        val quranHashMap: HashMap<String, String> = hashMap

        // to handle "laa yogd" to appear in next
        AppSharedPref.updateSaveCounter("saveCounter", 7-numberOfDaysToSave)
        AppSharedPref.updateReadCounter("readCounter", 7-numberOfDaysToRead)
        AppSharedPref.updateRevisionCounter("revisionCounter", 7-numberOfDaysToRevision)


        return listOf(
            Quran(
                1,
                quranHashMap,
                dateList[0],
                dayNameList[0],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                2,
                quranHashMap,
                dateList[1],
                dayNameList[1],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                3,
                quranHashMap,
                dateList[2],
                dayNameList[2],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                4,
                quranHashMap,
                dateList[3],
                dayNameList[3],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                5,
                quranHashMap,
                dateList[4],
                dayNameList[4],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                6,
                quranHashMap,
                dateList[5],
                dayNameList[5],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            ),
            Quran(
                7,
                quranHashMap,
                dateList[6],
                dayNameList[6],
                mCalendar,
                0,
                weeklyMessage,
                true,
                numberOfDaysToSave,
                numberOfDaysToRead,
                numberOfDaysToRevision,
                false,
                false,
                false,
                false
            )
        )
    }
}