package com.example.mindfulminutes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// OptIn annotation for experimental Material3 APIs
@OptIn(ExperimentalMaterial3Api::class)
class TrendsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrendsScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen() {
    // Example: Display simple mood trends (replace with real data later)
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("mood_data", ComponentActivity.MODE_PRIVATE)

    val moods = listOf("😃", "🙂", "😐", "😔", "😢")
    val moodList = sharedPref.all.toSortedMap()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mood Trends") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Your Mood This Week", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(moodList.keys.toList()) { key ->
                    val value = sharedPref.getString(key, "2") ?: "2"
                    Text("Entry: $key → ${moods[value.toInt()]}")
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
