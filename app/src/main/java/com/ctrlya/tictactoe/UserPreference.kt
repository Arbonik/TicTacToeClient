package com.ctrlya.tictactoe

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager

class UserPreference(context: Context)
{
    private val TAG_AUTH: String = "TAG_AUTH"
    private val TAG_NAME: String = "TAG_NAME"

    private var preference = PreferenceManager.getDefaultSharedPreferences(context)

    var name: String? = null
        get()
        {
            if (field == null)
            {
                if (checkAuth())
                {
                    Log.d("preference1", getUserName())
                    field = getUserName()
                }
            }
            return field
        }

    fun authUserOnDevice(name: String)
    {
        preference.edit().apply {
            putBoolean(TAG_AUTH, true)
            putString(TAG_NAME, name)
            apply()
        }
    }

    fun checkAuth(): Boolean = preference.getBoolean(TAG_AUTH, false)

    private fun getUserName(): String =
        preference.getString(TAG_NAME, "").toString()

    fun loginOut()
    {
        name = null
        preference
            .edit()
            .clear()
            .putBoolean(TAG_AUTH, false)
            .apply()
    }
}