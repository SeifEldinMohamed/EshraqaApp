package com.seif.eshraqaapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seif.eshraqaapp.data.models.Azkar

@Dao
interface EshrakaDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addZekr(zekr: Azkar)

    @Query("SELECT * FROM azkar_table")
    fun getAllData(): LiveData<List<Azkar>>

    @Update
    suspend fun updateZekr(zekr:Azkar)
}