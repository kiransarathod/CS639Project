package com.example.mindfulminutes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
class TrendsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TrendsScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("mood_data", ComponentActivity.MODE_PRIVATE)

    val moods = listOf("😃", "🙂", "😐", "😔", "😢")

    val moodList = sharedPref.all
        .toSortedMap()
        .map { (timestamp, rawValue) ->
            val parts = rawValue.toString().split("|")
            MoodEntry(
                timestamp = timestamp,
                mood = moods[parts[0].toInt()],
                stress = parts[1],
                notes = parts.getOrNull(2) ?: ""
            )
        }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mood Trends") }) }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            items(moodList) { item ->
                Column(Modifier.padding(8.dp)) {
                    Text("Mood: ${item.mood}", style = MaterialTheme.typography.bodyLarge)
                    Text("Stress: ${item.stress}")
                    if (item.notes.isNotBlank()) {
                        Text("Notes: ${item.notes}")
                    }
                    Divider(Modifier.padding(vertical = 12.dp))
                }
            }
        }
    }
}

data class MoodEntry(
    val timestamp: String,
    val mood: String,
    val stress: String,
    val notes: String
)
