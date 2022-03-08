package com.seif.eshraqaapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var shared: SharedPreferences
//    private lateinit var pref: SharedPreferences
//    private lateinit var edit: SharedPreferences.Editor

    fun isAppFirstTimeRun(context: Context):Boolean {
        shared = context.getSharedPreferences("isFirstTimeQuran", Context.MODE_PRIVATE)
        if (shared.getBoolean("check", true)) {

            return true
        }
        return false
    }
}