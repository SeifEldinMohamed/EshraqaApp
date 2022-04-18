package com.seif.eshraqaapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.seif.eshraqaapp.R
import com.seif.eshraqaapp.data.EshrakaDatabase
import com.seif.eshraqaapp.data.models.MyDate
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.repository.RepositoryImp
import com.seif.eshraqaapp.data.sharedPreference.AppSharedPref
import com.seif.eshraqaapp.ui.fragments.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PrayerViewModel(application: Application): AndroidViewModel(application) {
    private val eshrakaDatabaseDao = EshrakaDatabase.getInstance(application).myDao()
    private val repository = RepositoryImp(eshrakaDatabaseDao)
    val prayer: LiveData<List<Prayer>> = repository.getAllPrayerData()
    private lateinit var weekDate: ArrayList<MyDate>
    private lateinit var shared: SharedPreferences
    private lateinit var restOfWeekDate: ArrayList<MyDate>
    private lateinit var restOfWeekDaysName: ArrayList<String>

    // private lateinit var  scoreList:List<Int>
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var message = application.getString(R.string.first_week_prayer_message)
    val vacationDaysNumber: LiveData<Int> = repository.getPrayerVacationDaysNumber()

    fun addPrayer(prayer: List<Prayer>) {
        viewModelScope.launch(Dispatchers.IO) {
            prayer.forEach {
                repository.addPrayer(it)
            }
        }
    }

    fun updatePrayer(prayer: Prayer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePrayer(prayer)
        }
    }

    fun deleteAllPrayer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPrayer()
        }
    }
    fun getAllPrayerWeekScore(): LiveData<List<Int>> {
        return repository.getAllPrayerWeekScore()
    }

    fun getSumOfPrayerWeekScore(scoreList: List<Int>): Int {
        var totalWeekScore = 0
        scoreList.forEach {
            totalWeekScore += it
        }
        return totalWeekScore
    }

    fun isAppFirstTimeRun(context: Context): Boolean {

        shared = context.getSharedPreferences("isFirstTimePrayerDays", Context.MODE_PRIVATE)
        if (shared.getBoolean("checkPrayer", true)) {
            // days

            pref = context.getSharedPreferences("PrayerPrefs", Context.MODE_PRIVATE)
            edit = pref.edit()
            edit.putInt("nweek", 1)
            edit.apply()
            val editor = shared.edit()
            editor.putBoolean("checkPrayer", false)
            editor.apply()
            return true
        }
        return false
    }

//    private fun prepareDays() {
//        var date = LocalDate.now()
//        // initialize
//        restOfWeekDate = ArrayList()
//        restOfWeekDaysName = ArrayList()
//
//        val prayerDateSchedule:ArrayList<PrayerDate> = ArrayList()
//        for (j in 0..600) {
//            for (i in 0..6 step 1) {
//                date = date.plusDays(1L)
//                val day = date.dayOfMonth.toString()
//                val month = date.monthValue.toString()
//                val year = date.year.toString()
//                Log.d("test", "$day - $month - $year")
//
//                restOfWeekDate.add(MyDate(day, month, year))
//                restOfWeekDaysName.add(getDayNameInArabic(date.dayOfWeek.toString()))
//                prayerDateSchedule.add(PrayerDate(
//                    1,
//                    day,
//                    month,
//                    year,
//                    getDayNameInArabic(date.dayOfWeek.toString()),
//                ))
//
//            }
//        }
//        addPrayerSchedule(prayerDateSchedule)
//        Log.d("test", restOfWeekDate.toString())
//        Log.d("test", restOfWeekDaysName.toString())
//
//    }



    fun getSevenDaysPrayerData(sonnHashMap: HomeFragment.SonnHashMap): List<Prayer> {

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

        val quranOnlyHashMap: HashMap<String, Boolean> = createQuranOnlyHashMap()
        val qadaaHashMap: HashMap<String, Boolean> = createQadaaHashMap()
        val sonnHashMap: HashMap<String, Boolean> = sonnHashMap

        return listOf(
            Prayer(
                1,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[0],
                dayNameList[0],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),
            Prayer(
                2,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[1],
                dayNameList[1],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),
            Prayer(
                3,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[2],
                dayNameList[2],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),

            Prayer(
                4,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[3],
                dayNameList[3],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),
            Prayer(
                5,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[4],
                dayNameList[4],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),
            Prayer(
                6,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[5],
                dayNameList[5],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),
            Prayer(
                7,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                dateList[6],
                dayNameList[6],
                mCalendar,
                0,
                0,
                0,
                0,
                message,
                false,
            ),
        )
    }

    private fun createQuranOnlyHashMap(): HashMap<String, Boolean> {
        val quranOnlyHashMap = HashMap<String, Boolean>()
        quranOnlyHashMap["fagr"] = false
        quranOnlyHashMap["zuhr"] = false
        quranOnlyHashMap["asr"] = false
        quranOnlyHashMap["maghreb"] = false
        quranOnlyHashMap["esha"] = false
        return quranOnlyHashMap
    }

    private fun createQadaaHashMap(): HashMap<String, Boolean> {
        val qadaaHashMap = HashMap<String, Boolean>()
        // Qadaa
        qadaaHashMap["q_fagr"] = false
        qadaaHashMap["q_zuhr"] = false
        qadaaHashMap["q_asr"] = false
        qadaaHashMap["q_maghreb"] = false
        qadaaHashMap["q_esha"] = false
        return qadaaHashMap
    }

//    private fun getDayNameInArabic(dayName:String): String {
//        var dayNameInArabic = ""
//        when(dayName){
//            "FRIDAY" -> dayNameInArabic = "الجمعة"
//            "SATURDAY" -> dayNameInArabic = "السبت"
//            "SUNDAY" -> dayNameInArabic = "الأحد"
//            "MONDAY" -> dayNameInArabic = "الاثنين"
//            "TUESDAY" -> dayNameInArabic = "الثلاثاء"
//            "WEDNESDAY" -> dayNameInArabic = "الأربعاء"
//            "THURSDAY" -> dayNameInArabic = "الخميس"
//        }
//        return dayNameInArabic
//    }


    fun createNewWeekSchedule(
        lastPrayerDay: Prayer,
        prayerOnlyHashMap: HashMap<String, Boolean>,
        prayerAndQadaaHashMap: HashMap<String, Boolean>,
        sonnHashMap: HashMap<String, Boolean>,
        weeklyMessage: String,
        mCalendar:Calendar
    ): List<Prayer> {
        deleteAllPrayer()

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


//        val readPrayerDaysNumber =  AppSharedPref.readPrayerDaysNumber("days_prayer",6)
//        AppSharedPref.writePrayerDaysNumber("days_prayer", readPrayerDaysNumber + 7)


        val hashMap1 = HashMap<String, Boolean>()
        prayerOnlyHashMap.forEach { (key, value) ->
            hashMap1[key] = false
        }
        val hashMap2 = HashMap<String, Boolean>()
        prayerAndQadaaHashMap.forEach { (key, value) ->
            hashMap2[key] = false
        }

        val hashMap3 = HashMap<String, Boolean>()
        sonnHashMap.forEach { (key, value) ->
            hashMap3[key] = false
        }

        return listOf(
            Prayer(
                1,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[0],
                dayNameList[0],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            ),
            Prayer(
                2,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[1],
                dayNameList[1],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            ),
            Prayer(
                3,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[2],
                dayNameList[2],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            ),
            Prayer(
                4,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[3],
                dayNameList[3],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            ),
            Prayer(
                5,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[4],
                dayNameList[4],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            ),
            Prayer(
                6,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[5],
                dayNameList[5],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            ),
            Prayer(
                7,
                hashMap1,
                hashMap2,
                hashMap3,
                dateList[6],
                dayNameList[6],
                mCalendar,
                0,
                0,
                0,
                0,
                weeklyMessage,
                false
            )
        )
    }
}


//        var date = LocalDate.of(lastPrayerDay.currentYear,
//            lastPrayerDay.currentMonth, lastPrayerDay.currentDay)
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
//            weekDate.add(MyDate(day, month, year))
//            daysOfWeek.add(getDayNameInArabic(date.dayOfWeek.toString()))
//        }
//        Log.d("test", weekDate.toString())
//        Log.d("test", daysOfWeek.toString())