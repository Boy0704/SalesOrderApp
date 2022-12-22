package com.apps.salesorder.data.preferences

import android.content.Context
import android.content.SharedPreferences

private const val prefName = "SalesOrder.pref"

class Preferences(context: Context) {
    private var sharedPref: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor

    init {
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String?) {
        editor.putString(key, value)
            .apply()
    }

    fun delete() {
        editor.clear()
        editor.commit()
    }

    fun remove(key: String){
        editor.remove(key)
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }
}