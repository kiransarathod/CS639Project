package com.example.mindfulminutes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureCard(
                title = stringResource(id = R.string.mood_check_in_button),
                description = stringResource(id = R.string.mood_check_in_description),
                icon = Icons.Default.Favorite,
                onClick = { navController.navigate("mood_check_in") }
            )
            FeatureCard(
                title = stringResource(id = R.string.breathing_button),
                description = stringResource(id = R.string.breathing_description),
                icon = Icons.Default.Star,
                onClick = { navController.navigate("breathing") }
            )
            FeatureCard(
                title = stringResource(id = R.string.mood_trends_button),
                description = stringResource(id = R.string.mood_trends_description),
                icon = Icons.Default.Info,
                onClick = { navController.navigate("mood_trends") }
            )
            FeatureCard(
                title = "Settings",
                description = "Manage your preferences and reminders.",
                icon = Icons.Default.Settings,
                onClick = { navController.navigate("settings") }
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Mindful Minutes Logo",
                modifier = Modifier.height(120.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureCard(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(Modifier.size(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
