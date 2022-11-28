package com.example.orderlunch.utils

import android.content.Context
import android.content.SharedPreferences

class LocalData(val context: Context) {

    private val APP_SETTINGS = "APP_SETTINGS"

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun account(): String? {
        return getSharedPreferences().getString("account",null)
    }

    fun account(string: String) {
        val editor = getSharedPreferences().edit()
        editor.putString("account", string)
        editor.apply()
    }
}