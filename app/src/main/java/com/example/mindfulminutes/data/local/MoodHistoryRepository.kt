package com.example.mindfulminutes.data.local

import android.content.Context
import com.example.mindfulminutes.data.MoodEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json // <-- Added missing import

object MoodHistoryRepository {

    private const val PREFS_NAME = "MindfulMinutesPrefs"
    private const val KEY_MOOD_HISTORY = "mood_history_json"

    fun getMoodHistory(context: Context): List<MoodEntry> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_MOOD_HISTORY, null)

        return if (jsonString != null) {
            try {
                Json.decodeFromString<List<MoodEntry>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun saveMoodEntry(context: Context, newEntry: MoodEntry) {
        val currentHistory = getMoodHistory(context).toMutableList()
        currentHistory.add(0, newEntry) // Add new entry to the top

        val jsonString = Json.encodeToString(currentHistory)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(KEY_MOOD_HISTORY, jsonString)
            apply()
        }
    }
}
