package com.example.mindfulminutes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingScreen(navController: NavController) {
    var isRunning by remember { mutableStateOf(false) }
    var breathingPhase by remember { mutableStateOf<BreathingPhase>(BreathingPhase.Instruction) }
    val coroutineScope = rememberCoroutineScope()
    val circleSize = remember { Animatable(100f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            coroutineScope.launch {
                // Total cycle time: 4s inhale + 4s hold + 6s exhale = 14s
                // 60s / 14s = ~4.2 cycles
                repeat(4) {
                    breathingPhase = BreathingPhase.Inhale
                    circleSize.animateTo(200f, animationSpec = tween(4000))
                    breathingPhase = BreathingPhase.Hold
                    delay(4000)
                    breathingPhase = BreathingPhase.Exhale
                    circleSize.animateTo(100f, animationSpec = tween(6000))
                }
                breathingPhase = BreathingPhase.Done
                isRunning = false
            }
        }
    }

    Scaffold(
        topBar = { MindfulAppBar(title = "1-Minute Breathing", canNavigateBack = true, navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (breathingPhase == BreathingPhase.Instruction) {
                Text(
                    text = stringResource(id = R.string.breathing_instruction),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                Button(onClick = { isRunning = true }) {
                    Text(stringResource(id = R.string.breathing_start_button))
                }
            } else if (breathingPhase == BreathingPhase.Done) {
                Text(
                    text = stringResource(id = R.string.breathing_complete),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Finish")
                }
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(250.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color(0xFF76C7C0),
                            radius = circleSize.value,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }
                    Text(
                        text = when (breathingPhase) {
                            BreathingPhase.Inhale -> stringResource(id = R.string.breathing_inhale)
                            BreathingPhase.Hold -> stringResource(id = R.string.breathing_hold)
                            else -> stringResource(id = R.string.breathing_exhale)
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

enum class BreathingPhase {
    Instruction,
    Inhale,
    Hold,
    Exhale,
    Done
}
