package com.example.mindfulminutes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Add OptIn annotation for experimental Material3 APIs
@OptIn(ExperimentalMaterial3Api::class)
class MoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen() {
    val context = LocalContext.current  // <-- Proper import
    var selectedMood by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mood Check-In") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("How are you feeling today?", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Simple 5-emoji mood selector
            val moods = listOf("😃", "🙂", "😐", "😔", "😢")
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                moods.forEachIndexed { index, emoji ->
                    Button(
                        onClick = { selectedMood = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMood == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(emoji)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                // TODO: Save mood with SharedPreferences / Firebase
            }) {
                Text("Save Mood")
            }
        }
    }
}
