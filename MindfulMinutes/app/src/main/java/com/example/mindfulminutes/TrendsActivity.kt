package com.example.mindfulminutes

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    val sharedPref = context.getSharedPreferences("mood_data", Context.MODE_PRIVATE)
    val moods = listOf("😃", "🙂", "😐", "😔", "😢")
    val moodList = sharedPref.all.toSortedMap()

    Scaffold(topBar = { TopAppBar(title = { Text("Mood Trends") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Your Mood History", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(moodList.keys.toList()) { key ->
                    val value = sharedPref.getString(key, "2") ?: "2"
                    val parts = value.split("|")
                    val mood = moods.getOrElse(parts[0].toInt()) { "😐" }
                    val stress = parts.getOrNull(1) ?: ""
                    val notes = parts.getOrNull(2) ?: ""
                    Text("Mood: $mood, Stress: $stress, Notes: $notes")
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
