package com.example.mindfulminutes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindfulminutes.ui.theme.MindfulMinutesTheme

// OptIn annotation to use experimental Material3 APIs
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindfulMinutesTheme {
                HomeScreen(
                    onMoodClick = {
                        startActivity(Intent(this, MoodActivity::class.java))
                    },
                    onBreathingClick = {
                        startActivity(Intent(this, BreathingActivity::class.java))
                    },
                    onTrendsClick = {
                        startActivity(Intent(this, TrendsActivity::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMoodClick: () -> Unit,
    onBreathingClick: () -> Unit,
    onTrendsClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mindful Minutes") }) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onMoodClick, modifier = Modifier.fillMaxWidth()) {
                Text("Mood Check-In")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBreathingClick, modifier = Modifier.fillMaxWidth()) {
                Text("1-Minute Breathing")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onTrendsClick, modifier = Modifier.fillMaxWidth()) {
                Text("Mood Trends")
            }
        }
    }
}
