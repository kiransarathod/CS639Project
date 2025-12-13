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
    LocalContext.current
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
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simple 5-emoji mood selector
            val moods = listOf("😃", "🙂", "😐", "😔", "😢")
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                moods.forEachIndexed { index, emoji ->
                    Button(
                        onClick = { selectedMood = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (selectedMood == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = emoji)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Old behavior: nothing persisted
                },
                enabled = selectedMood != -1
            ) {
                Text("Save Mood")
            }
        }
    }
}
