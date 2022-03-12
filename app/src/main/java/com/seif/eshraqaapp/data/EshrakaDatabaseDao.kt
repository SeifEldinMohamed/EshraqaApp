package com.seif.eshraqaapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.models.Quran

@Dao
interface EshrakaDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addZekr(zekr: Azkar)

    @Query("SELECT * FROM azkar_table")
    fun getAllData(): LiveData<List<Azkar>>

    @Update
    suspend fun updateZekr(zekr: Azkar)

    @Query("SELECT score FROM azkar_table")
    fun getAllWeekScore(): LiveData<List<Int>>

    @Query("DELETE FROM azkar_table")
    suspend fun deleteAllAzkar()

    // quran
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuran(quran: Quran)

    @Query("SELECT * FROM Quran")
    fun getAllQuranData(): LiveData<List<Quran>>

    @Update
    suspend fun updateQuran(quran: Quran)

    @Query("SELECT score FROM Quran WHERE isVacation == 0") // 0-> indicates false
    fun getAllQuranWeekScore(): LiveData<List<Int>>

    @Query("DELETE FROM Quran")
    suspend fun deleteAllQuran()

    @Query("SELECT sum(isVacation) from quran where isVacation == 1")
    fun getVacationDaysNumber(): LiveData<Int>

    // prayer
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPrayer(prayer: Prayer)

    @Query("SELECT * FROM Prayer")
    fun getAllPrayerData(): LiveData<List<Prayer>>

    @Update
    suspend fun updatePrayer(prayer: Prayer)

    @Query("SELECT totalScore FROM Prayer WHERE isVacation == 0") // 0-> indicates false
    fun getAllPrayerWeekScore(): LiveData<List<Int>>

    @Query("DELETE FROM Prayer")
    suspend fun deleteAllPrayer()

    @Query("SELECT sum(isVacation) from Prayer where isVacation == 1")
    fun getPrayerVacationDaysNumber(): LiveData<Int>

}