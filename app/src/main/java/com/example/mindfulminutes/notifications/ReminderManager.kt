package com.example.mindfulminutes.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mindfulminutes.workers.ReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object ReminderManager {

    private const val REMINDER_WORK_TAG = "mindful_minutes_reminder_work"

    /**
     * Schedules a daily reminder at the specified hour and minute.
     */
    fun scheduleReminder(context: Context, hour: Int, minute: Int) {
        val workManager = WorkManager.getInstance(context)

        // Calculate the initial delay to get to the next scheduled time
        val now = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // If the scheduled time is in the past for today, schedule it for tomorrow
        if (now.after(scheduledTime)) {
            scheduledTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = scheduledTime.timeInMillis - now.timeInMillis

        val reminderRequest =
            PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE, // Use REPLACE to update the existing worker
            reminderRequest
        )
    }

    /**
     * Cancels the currently scheduled daily reminder.
     */
    fun cancelReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_TAG)
    }

    /**
     * Creates the notification channel required for Android 8.0+.
     */
    fun createNotificationChannel(context: Context) {
        val name = "Daily Mood Reminders"
        val descriptionText = "A reminder to check in with your mood."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(ReminderWorker.CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager:
            NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
