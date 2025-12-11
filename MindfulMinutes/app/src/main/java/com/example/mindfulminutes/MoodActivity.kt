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
    var selectedMood by remember { mutableStateOf(-1)}
    var stress by remember { mutableStateOf(3f) }
    var notes by remember { mutableStateOf("") }


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
                Text("Stress level: ${stress.toInt()}", style = MaterialTheme.typography.bodyLarge)
                Slider(
                    value = stress,
                    onValueChange = { stress = it },
                    valueRange = 1f..5f,
                    modifier = Modifier.padding(top = 16.dp)
                )
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
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    label = { Text("Reflection notes (optional)") }
                )
                
                Button(onClick = {
                    if (selectedMood != -1) {
                        val sharedPref = context.getSharedPreferences(
                            "mood_data",
                            ComponentActivity.MODE_PRIVATE
                        )
                        val editor = sharedPref.edit()

                        val timestamp = System.currentTimeMillis().toString()
                        editor.putString(timestamp, selectedMood.toString())
                        editor.apply()
                    }
                }) {
                    Text("Save Mood")
                }
            }
        }
    }

