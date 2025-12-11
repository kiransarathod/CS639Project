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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
class BreathingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BreathingScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingScreen() {
    var text by remember { mutableStateOf("Inhale…") }

    LaunchedEffect(Unit) {
        while (true) {
            text = "Inhale…"
            delay(4000)
            text = "Hold…"
            delay(2000)
            text = "Exhale…"
            delay(4000)
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Breathing Exercise") }) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, style = MaterialTheme.typography.headlineLarge)
        }
    }
}
