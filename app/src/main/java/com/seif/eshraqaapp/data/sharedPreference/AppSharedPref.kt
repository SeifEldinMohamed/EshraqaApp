package com.seif.eshraqaapp.data.sharedPreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppSharedPref {
    companion object {
        private var  mSharedPref: SharedPreferences? = null
        fun init(context: Context) {
            if (mSharedPref == null)
                mSharedPref = context.getSharedPreferences("appSharedPref", Activity.MODE_PRIVATE)
        }
        fun readCheckFirstTime(key: String?, defValue: Boolean): Boolean {
            return mSharedPref!!.getBoolean(key, defValue)
        }
        fun writeCheckFirstTime(key:String?,value:Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }
        // quran
        fun readSaveCounter(key:String?, defValue:Int): Int{
            return mSharedPref!!.getInt(key, defValue)
        }
        fun updateSaveCounter(key:String?, value:Int){
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }
        fun readReadCounter(key:String?, defValue:Int): Int{
            return mSharedPref!!.getInt(key, defValue)
        }
        fun updateReadCounter(key:String?, value:Int){
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }
        fun readRevisionCounter(key:String?, defValue:Int): Int{

            return mSharedPref!!.getInt(key, defValue)
        }
        fun updateRevisionCounter(key:String?, value:Int){
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }

       // prayer
       fun readPrayerOnly(key:String?, defValue:Boolean): Boolean{

           return mSharedPref!!.getBoolean(key, defValue)
       }
        fun writePrayerOnly(key:String?, value:Boolean){
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }
        fun readPrayerAndQadaa(key:String?, defValue:Boolean): Boolean{

            return mSharedPref!!.getBoolean(key, defValue)
        }
        fun writePrayerAndQadaa(key:String?, value:Boolean){
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }
        fun readPrayerAndSonn(key:String?, defValue:Boolean): Boolean{

            return mSharedPref!!.getBoolean(key, defValue)
        }
        fun writePrayerAndSonn(key:String?, value:Boolean){
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }


//        fun readPersonalInfo(key: String?, defValue: String?): String? {
//            return mSharedPref!!.getString(key, defValue)
//        }
//        fun writePersonalInfo(key:String?,value:String?) {
//            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
//            prefsEditor.putString(key, value)
//            prefsEditor.apply()
//        }
    }
}