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
import com.seif.eshraqaapp.data.models.Quran
import com.seif.eshraqaapp.data.repository.RepositoryImp
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
        val quranAndQadaaHashMap: HashMap<String, Boolean> = createQuranAndQadaaHashMap()
        val quranAndSonnHashMap: HashMap<String, Boolean> = createQuranAndSonnHashMap(sonnHashMap)

        return listOf(
            Prayer(
                1,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[0].day.toInt(),
                weekDate[0].month.toInt(),
                weekDate[0].year.toInt(),
                daysOfWeek[0],
                0,
                message,
                false,
            ),
            Prayer(
                2,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[1].day.toInt(),
                weekDate[1].month.toInt(),
                weekDate[1].year.toInt(),
                daysOfWeek[1],
                0,
                message,
                false,
            ),
            Prayer(
                3,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[2].day.toInt(),
                weekDate[2].month.toInt(),
                weekDate[2].year.toInt(),
                daysOfWeek[2],
                0,
                message,
                false,
            ),

            Prayer(
                4,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[3].day.toInt(),
                weekDate[3].month.toInt(),
                weekDate[3].year.toInt(),
                daysOfWeek[3],
                0,
                message,
                false,
            ),
            Prayer(
                5,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[4].day.toInt(),
                weekDate[4].month.toInt(),
                weekDate[4].year.toInt(),
                daysOfWeek[4],
                0,
                message,
                false,
            ),
            Prayer(
                6,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[5].day.toInt(),
                weekDate[5].month.toInt(),
                weekDate[5].year.toInt(),
                daysOfWeek[5],
                0,
                message,
                false,
            ),
            Prayer(
                7,
                quranOnlyHashMap,
                quranAndQadaaHashMap,
                quranAndSonnHashMap,
                weekDate[6].day.toInt(),
                weekDate[6].month.toInt(),
                weekDate[6].year.toInt(),
                daysOfWeek[6],
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

    private fun createQuranAndQadaaHashMap(): HashMap<String, Boolean> {
        val quranAndQadaaHashMap = HashMap<String, Boolean>()
        quranAndQadaaHashMap["fagr"] = false
        quranAndQadaaHashMap["zuhr"] = false
        quranAndQadaaHashMap["asr"] = false
        quranAndQadaaHashMap["maghreb"] = false
        quranAndQadaaHashMap["esha"] = false
        // Qadaa
        quranAndQadaaHashMap["q_fagr"] = false
        quranAndQadaaHashMap["q_zuhr"] = false
        quranAndQadaaHashMap["q_asr"] = false
        quranAndQadaaHashMap["q_maghreb"] = false
        quranAndQadaaHashMap["q_esha"] = false
        return quranAndQadaaHashMap
    }

    private fun createQuranAndSonnHashMap(sonnHashMap: HomeFragment.SonnHashMap): HashMap<String, Boolean> {
        val quranAndSonnHashMap = HashMap<String, Boolean>()
        quranAndSonnHashMap["fagr"] = false
        quranAndSonnHashMap["zuhr"] = false
        quranAndSonnHashMap["asr"] = false
        quranAndSonnHashMap["maghreb"] = false
        quranAndSonnHashMap["esha"] = false
        // sonn
        quranAndSonnHashMap.putAll(sonnHashMap)
        Log.d("prayer", quranAndSonnHashMap.toString())
        return quranAndSonnHashMap
    }
}