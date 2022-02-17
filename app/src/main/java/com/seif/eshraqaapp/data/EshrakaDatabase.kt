package com.seif.eshraqaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seif.eshraqaapp.data.models.Azkar

const val DATABASE_NAME = "eshraka database"
@Database(entities = [Azkar::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class EshrakaDatabase: RoomDatabase() {
    abstract fun myDao(): EshrakaDatabaseDao
    companion object{
        @Volatile
       private var instance : EshrakaDatabase? = null

        fun getInstance(context: Context): EshrakaDatabase {
            instance?.let {
                return it
            }?:
            synchronized(this) { // to prevent any thread to deal with it until this thread unlock it
                return Room.databaseBuilder(
                    context,
                    EshrakaDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
        }
    }
}