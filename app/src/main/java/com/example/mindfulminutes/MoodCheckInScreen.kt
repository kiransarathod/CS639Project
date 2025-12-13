package com.example.mindfulminutes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mindfulminutes.data.MoodEntry
import com.example.mindfulminutes.data.local.MoodHistoryRepository
import com.example.mindfulminutes.model.Mood
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodCheckInScreen(navController: NavController) {
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var note by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val moods = remember {
        listOf(
            Mood("Joyful", "😄"),
            Mood("Content", "😊"),
            Mood("Neutral", "😐"),
            Mood("Anxious", "🤔"),
            Mood("Stressed", "😕"),
            Mood("Sad", "😔"),
            Mood("Irritated", "😠"),
            Mood("Tired", "😴")
        )
    }

    Scaffold(
        topBar = { MindfulAppBar(title = "Mood Check-In", canNavigateBack = true, navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "How are you feeling in this moment?",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Gently notice and select what feels true for you.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                LazyColumn {
                    items(moods) { mood ->
                        MoodRowItem(
                            mood = mood,
                            isSelected = selectedMood == mood,
                            onClick = { selectedMood = mood }
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Add a note (optional)...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    selectedMood?.let { mood ->
                        scope.launch {
                            val stressScore = getMoodScore(mood.label)
                            val noteText = note.takeIf { it.isNotBlank() } ?: ""

                            // Save to local and remote repositories
                            val localEntry = MoodEntry(moodEmoji = mood.emoji, moodLabel = mood.label, stressLevel = stressScore, note = noteText)
                            MoodHistoryRepository.saveMoodEntry(context, localEntry)
                            val remoteLog = MoodLog(label = mood.label, emoji = mood.emoji, score = stressScore, note = noteText.takeIf { it.isNotEmpty() })
                            MoodRepository.addMood(remoteLog)

                            // Use a more robust navigation command to guarantee return to home
                            navController.navigate("home") {
                                popUpTo("home") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                },
                enabled = selectedMood != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Save Mood")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun getMoodScore(label: String): Int {
    return when (label) {
        "Joyful" -> 5
        "Content" -> 4
        "Neutral" -> 3
        "Anxious", "Stressed" -> 2
        "Sad", "Irritated", "Tired" -> 1
        else -> 0
    }
}
