package com.darshan.darshanshop

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.darshan.darshanshop.utils.CommonMethods

import java.util.*

open class DarshanShoppingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        CommonMethods.showLog("initialize","sp initialized")
    }

    companion object{
        lateinit var activityFromApp: AppCompatActivity
        lateinit var sharedPreferences: SharedPreferences

        open fun savePreferenceDataString(key: String?, value: String?) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.commit()
        }

        open fun savePreferenceDataInteger(key: String?, value: Int?) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt(key, value!!)
            editor.commit()
        }

        open fun savePreferenceDataBoolean(key: String?, value: Boolean?) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean(key, value!!)
            editor.commit()
        }

        open fun savePreferenceDataArray(
            key: String?,
            value: ArrayList<*>
        ) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(key, value.toString())
            editor.commit()
        }

        open fun clearPreferenceData() {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.commit()
        }
    }


}