package com.seif.eshraqaapp.data.sharedPreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppSharedPref {
    companion object {
        private var mSharedPref: SharedPreferences? = null
        fun init(context: Context) {
            if (mSharedPref == null)
                mSharedPref = context.getSharedPreferences("appSharedPref", Activity.MODE_PRIVATE)
        }

        // quran
        fun readSaveCounter(key: String?, defValue: Int): Int {
            return mSharedPref!!.getInt(key, defValue)
        }

        fun updateSaveCounter(key: String?, value: Int) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }

        fun readReadCounter(key: String?, defValue: Int): Int {
            return mSharedPref!!.getInt(key, defValue)
        }

        fun updateReadCounter(key: String?, value: Int) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }

        fun readRevisionCounter(key: String?, defValue: Int): Int {

            return mSharedPref!!.getInt(key, defValue)
        }

        fun updateRevisionCounter(key: String?, value: Int) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }

        // prayer
        fun readPrayerOnly(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writePrayerOnly(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readPrayerAndQadaa(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writePrayerAndQadaa(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readPrayerAndSonn(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writePrayerAndSonn(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontFagr(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontFagr(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontZuhr(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontZuhr(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontMaghreb(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontMaghreb(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontEsha(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontEsha(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontWetr(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontWetr(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontDoha(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontDoha(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSontKeyam(key: String?, defValue: Boolean): Boolean {

            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSontKeyam(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readQadaaPeriod(key: String?, defValue: Int): Int {

            return mSharedPref!!.getInt(key, defValue)
        }

        fun writeQadaaPeriod(key: String?, value: Int) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }

        fun readSoundChoice(key: String?, defValue: Boolean): Boolean {
            return mSharedPref!!.getBoolean(key, defValue)
        }

        fun writeSoundChoice(key: String?, value: Boolean) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putBoolean(key, value)
            prefsEditor.apply()
        }

        fun readSebhaScore(key: String?, defValue: Long): Long {

            return mSharedPref!!.getLong(key, defValue)
        }

        fun writeSebhaScore(key: String?, value: Long) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putLong(key, value)
            prefsEditor.apply()
        }

        fun readSebhaMessage(key: String?, defValue: Int): Int {

            return mSharedPref!!.getInt(key, defValue)
        }

        fun writeSebhaMessage(key: String?, value: Int) {
            val prefsEditor: SharedPreferences.Editor = mSharedPref!!.edit()
            prefsEditor.putInt(key, value)
            prefsEditor.apply()
        }
    }
}