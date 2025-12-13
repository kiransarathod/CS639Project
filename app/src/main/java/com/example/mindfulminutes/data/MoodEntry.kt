package com.example.mindfulminutes.data

import kotlinx.serialization.Serializable

@Serializable
data class MoodEntry(
    val moodEmoji: String,
    val moodLabel: String,
    val stressLevel: Int,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)
