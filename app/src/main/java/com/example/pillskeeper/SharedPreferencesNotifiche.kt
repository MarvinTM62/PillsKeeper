package com.example.pillskeeper

import android.content.Context
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesNotifiche {

    fun saveNotifiche(context : Context, arrayList: ArrayList<Notifica>){
        val gson = Gson()
        var json = gson.toJson(arrayList)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var editor = sharedPreferences.edit()
        editor.putString("notifica_key", json)
        editor.apply()
    }

     fun loadNotifiche(context: Context): ArrayList<Notifica> {
         var arrayList = ArrayList<Notifica>()
         val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
         val emptyList = Gson().toJson(ArrayList<Notifica>())
         var json = sharedPreferences.getString("notifica_key", emptyList)
         val gson = Gson()
         val type = object : TypeToken<ArrayList<Notifica>>() {}.type
         arrayList = gson.fromJson(json, type)
         return arrayList
     }

}