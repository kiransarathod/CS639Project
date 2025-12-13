package com.example.mindfulminutes.ui.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.mindfulminutes.notifications.ReminderManager
import java.util.Calendar

private const val PREFS_NAME = "MindfulMinutesPrefs"
private const val KEY_REMINDER_ENABLED = "reminder_enabled"
private const val KEY_REMINDER_HOUR = "reminder_hour"
private const val KEY_REMINDER_MINUTE = "reminder_minute"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ReminderSettingsItem()
        }
    }
}

@Composable
private fun ReminderSettingsItem() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
            } else { true }
        )
    }

    var isReminderEnabled by remember { mutableStateOf(prefs.getBoolean(KEY_REMINDER_ENABLED, false)) }
    
    var reminderTime by remember {
        mutableStateOf(
            Pair(
                prefs.getInt(KEY_REMINDER_HOUR, 21),
                prefs.getInt(KEY_REMINDER_MINUTE, 0)
            )
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if (isGranted) {
                isReminderEnabled = true
                updateReminderState(context, true, reminderTime.first, reminderTime.second)
            }
        }
    )

    // Main toggle row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Daily Reminder", style = MaterialTheme.typography.titleMedium)
        }
        Switch(
            checked = isReminderEnabled && hasNotificationPermission,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        isReminderEnabled = true
                        updateReminderState(context, true, reminderTime.first, reminderTime.second)
                    }
                } else {
                    isReminderEnabled = false
                    updateReminderState(context, false, reminderTime.first, reminderTime.second)
                }
            }
        )
    }

    // Time picker row (only enabled if the main toggle is on)
    TimePickerRow(enabled = isReminderEnabled && hasNotificationPermission, time = reminderTime) { newHour, newMinute ->
        reminderTime = Pair(newHour, newMinute)
        updateReminderState(context, true, newHour, newMinute) // Re-schedule with new time
    }

    if (isReminderEnabled && !hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Text(
            text = "Notification permission is required to enable reminders.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun TimePickerRow(enabled: Boolean, time: Pair<Int, Int>, onTimeSelected: (Int, Int) -> Unit) {
    val context = LocalContext.current
    val formattedTime = String.format("%02d:%02d", time.first, time.second)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            onTimeSelected(hour, minute)
        },
        time.first,
        time.second,
        true // 24-hour format
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = { timePickerDialog.show() })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Reminder Time",
                style = MaterialTheme.typography.titleMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Text(
                text = "Receive a notification at this time daily.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            )
        }
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
        )
    }
}

private fun updateReminderState(context: Context, isEnabled: Boolean, hour: Int, minute: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    with(prefs.edit()) {
        putBoolean(KEY_REMINDER_ENABLED, isEnabled)
        putInt(KEY_REMINDER_HOUR, hour)
        putInt(KEY_REMINDER_MINUTE, minute)
        apply()
    }

    if (isEnabled) {
        ReminderManager.scheduleReminder(context, hour, minute)
    } else {
        ReminderManager.cancelReminder(context)
    }
}
