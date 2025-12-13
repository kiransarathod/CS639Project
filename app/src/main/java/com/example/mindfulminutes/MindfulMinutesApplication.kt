package com.example.mindfulminutes

import android.app.Application
import com.example.mindfulminutes.notifications.ReminderManager

class MindfulMinutesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Create the notification channel as soon as the app starts
        ReminderManager.createNotificationChannel(this)
    }
}
