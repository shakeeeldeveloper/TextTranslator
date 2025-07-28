package com.example.texttranslator.viewmodels

import android.content.Context
import com.example.texttranslator.model.HistoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("history_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "history_list"

    fun getHistory(): List<HistoryEntity> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<HistoryEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveHistory(newItem: HistoryEntity) {
        val current = getHistory().toMutableList()
        current.add(0, newItem) // prepend to list
        prefs.edit().putString(key, gson.toJson(current)).apply()
    }

    fun clearHistory() {
        prefs.edit().remove(key).apply()
    }
}
