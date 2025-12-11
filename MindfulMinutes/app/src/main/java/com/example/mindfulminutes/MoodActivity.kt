package com.example.mindfulminutes

import android.content.Context
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

@OptIn(ExperimentalMaterial3Api::class)
class MoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MoodScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen() {
    val context = LocalContext.current
    val moods = listOf("😃", "🙂", "😐", "😔", "😢")

    var selectedMood by remember { mutableStateOf(-1) }
    var stress by remember { mutableStateOf(3f) }
    var notes by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Mood Check-In") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("How are you feeling today?", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Text("Stress Level: ${stress.toInt()}")
            Slider(value = stress, onValueChange = { stress = it }, valueRange = 1f..5f)
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                moods.forEachIndexed { index, emoji ->
                    Button(
                        onClick = { selectedMood = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMood == index)
                                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) { Text(emoji) }
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Reflection notes (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedMood != -1) {
                        val sharedPref = context.getSharedPreferences("mood_data", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        val timestamp = System.currentTimeMillis().toString()
                        editor.putString(timestamp, "$selectedMood|${stress.toInt()}|$notes")
                        editor.apply()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Mood") }
        }
    }
}
