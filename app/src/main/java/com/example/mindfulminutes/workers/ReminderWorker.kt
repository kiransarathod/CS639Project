package com.example.mindfulminutes.workers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mindfulminutes.R

private const val DEEP_LINK_URI = "mindful-minutes://mood_check_in"

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        const val CHANNEL_ID = "mindful_minutes_reminder_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {
        showReminderNotification()
        return Result.success()
    }

    private fun showReminderNotification() {
        // Create the deep link intent
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(DEEP_LINK_URI)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("Mindful Minutes")
            .setContentText("How are you feeling today? Take 30 seconds to check in.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // --- THE FIX ---
        // Add a permission check before attempting to post the notification.
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        }
        // If permission is not granted, do nothing. The worker finishes silently.
    }
}
