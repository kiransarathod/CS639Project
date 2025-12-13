package com.example.mindfulminutes

import com.google.firebase.firestore.ServerTimestamp
import java.util.Calendar
import java.util.Date

/**
 * Represents a single mood check-in entry.
 * NOTE: Firestore requires a no-argument constructor and public fields.
 * Adding default values to the data class properties generates the required constructor.
 */
data class MoodLog(
    @ServerTimestamp val timestamp: Date? = null,
    val label: String = "",
    val emoji: String = "",
    val score: Int = 0,
    val note: String? = null
) {
    val dayOfYear: Int
        get() {
            val calendar = Calendar.getInstance().apply { time = timestamp ?: Date() }
            return calendar.get(Calendar.YEAR) * 365 + calendar.get(Calendar.DAY_OF_YEAR)
        }
}