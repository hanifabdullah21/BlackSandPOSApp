package com.singpaulee.blacksandapp.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var context : Context

    //Shared Preference Mode
    val PRIVATE_MODE = 0

    private val PREF_NAME = "BLACKSAND"

    val KEY_EMAIL = "MYEMAIL"
    val KEY_NAME = "MYNAME"
    val KEY_TOKEN = "MYNAME"

    private val KEY_IS_LOGGED_IN = "ISLOGIN"

    init {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit()
    }

    fun SharedPrefManager(context: Context){
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit()
    }

    fun savePrefBoolean(value: Boolean){
        editor.putBoolean(KEY_IS_LOGGED_IN, value)
        editor.commit()
    }

    fun savePrefString(value: String, key: String){
        editor.putString(key, value)
        editor.commit()
    }

    fun getEmail(): String? {
        return pref.getString(KEY_EMAIL, null)
    }

    fun getToken(): String? {
        return pref.getString(KEY_TOKEN, null)
    }

    fun getIsLogin(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}