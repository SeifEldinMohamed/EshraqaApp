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
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.repository.RepositoryImp
import com.seif.eshraqaapp.ui.fragments.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PrayerViewModel(application: Application): AndroidViewModel(application) {
    private val eshrakaDatabaseDao = EshrakaDatabase.getInstance(application).myDao()
    private val repository = RepositoryImp(eshrakaDatabaseDao)
    val prayer: LiveData<List<Prayer>> = repository.getAllPrayerData()
    private lateinit var weekDate: ArrayList<MyDate>
    private lateinit var shared: SharedPreferences

    // private lateinit var  scoreList:List<Int>
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    var message = application.getString(R.string.first_week_prayer_message)
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
    fun getSevenDaysPrayerData(sonnHashMap: HomeFragment.SonnHashMap): List<Prayer> {

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

        val quranOnlyHashMap: HashMap<String, Boolean> = createQuranOnlyHashMap()
        val qadaaHashMap: HashMap<String, Boolean> = createQadaaHashMap()
        val sonnHashMap: HashMap<String, Boolean> = sonnHashMap

        return listOf(
            Prayer(
                1,
                quranOnlyHashMap,
                qadaaHashMap,
                sonnHashMap,
                weekDate[0].day.toInt(),
                weekDate[0].month.toInt(),
                weekDate[0].year.toInt(),
                daysOfWeek[0],
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
                weekDate[1].day.toInt(),
                weekDate[1].month.toInt(),
                weekDate[1].year.toInt(),
                daysOfWeek[1],
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
                weekDate[2].day.toInt(),
                weekDate[2].month.toInt(),
                weekDate[2].year.toInt(),
                daysOfWeek[2],
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
                weekDate[3].day.toInt(),
                weekDate[3].month.toInt(),
                weekDate[3].year.toInt(),
                daysOfWeek[3],
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
                weekDate[4].day.toInt(),
                weekDate[4].month.toInt(),
                weekDate[4].year.toInt(),
                daysOfWeek[4],
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
                weekDate[5].day.toInt(),
                weekDate[5].month.toInt(),
                weekDate[5].year.toInt(),
                daysOfWeek[5],
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
                weekDate[6].day.toInt(),
                weekDate[6].month.toInt(),
                weekDate[6].year.toInt(),
                daysOfWeek[6],
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
        lastPrayerDay: Prayer,
        prayerOnlyHashMap: HashMap<String, Boolean>,
        prayerAndQadaaHashMap: HashMap<String, Boolean>,
        sonnHashMap: HashMap<String, Boolean>,
        weeklyMessage: String
    ): List<Prayer> {
        deleteAllPrayer()

        var date = LocalDate.of(lastPrayerDay.currentYear,
            lastPrayerDay.currentMonth, lastPrayerDay.currentDay)
        date = date.plusDays(1L)

        val weekDate = ArrayList<MyDate>()
        val daysOfWeek = ArrayList<String>()

        for (i in 0..6 step 1) {
            val day = date.dayOfMonth.toString()
            val month = date.monthValue.toString()
            val year = date.year.toString()
            println("$day - $month - $year")

            weekDate.add(MyDate(day, month, year))
            daysOfWeek.add(getDayNameInArabic(date.dayOfWeek.toString()))
            date = date.plusDays(1L)
        }
        Log.d("test", weekDate.toString())
        Log.d("test", daysOfWeek.toString())

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
                weekDate[0].day.toInt(),
                weekDate[0].month.toInt(),
                weekDate[0].year.toInt(),
                daysOfWeek[0],
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
                weekDate[1].day.toInt(),
                weekDate[1].month.toInt(),
                weekDate[1].year.toInt(),
                daysOfWeek[1],
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
                weekDate[2].day.toInt(),
                weekDate[2].month.toInt(),
                weekDate[2].year.toInt(),
                daysOfWeek[2],
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
                weekDate[3].day.toInt(),
                weekDate[3].month.toInt(),
                weekDate[3].year.toInt(),
                daysOfWeek[3],
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
                weekDate[4].day.toInt(),
                weekDate[4].month.toInt(),
                weekDate[4].year.toInt(),
                daysOfWeek[4],
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
                weekDate[5].day.toInt(),
                weekDate[5].month.toInt(),
                weekDate[5].year.toInt(),
                daysOfWeek[5],
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
                weekDate[6].day.toInt(),
                weekDate[6].month.toInt(),
                weekDate[6].year.toInt(),
                daysOfWeek[6],
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