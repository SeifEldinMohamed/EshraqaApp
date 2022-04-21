package com.seif.eshraqaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seif.eshraqaapp.data.models.Azkar
import com.seif.eshraqaapp.data.models.Prayer
import com.seif.eshraqaapp.data.models.Quran

const val DATABASE_NAME = "eshraka database"

@Database(entities = [Azkar::class, Quran::class, Prayer::class], version = 18, exportSchema = true)
@TypeConverters(Converter::class)
abstract class EshrakaDatabase : RoomDatabase() {
    abstract fun myDao(): EshrakaDatabaseDao

    companion object {
        @Volatile
        private var instance: EshrakaDatabase? = null

        fun getInstance(context: Context): EshrakaDatabase {

            return if (instance != null) {
                instance!!
            } else {
                synchronized(this) { // to prevent any thread to deal with it until this thread unlock it
                    Room.databaseBuilder(
                        context,
                        EshrakaDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration() // If you don’t want to provide migrations and you specifically want your database to be cleared when you upgrade the version
                        .build()
                }
            }
        }
    }
}