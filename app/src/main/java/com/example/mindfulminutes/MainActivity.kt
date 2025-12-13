package com.example.mindfulminutes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.mindfulminutes.ui.settings.SettingsScreen
import com.example.mindfulminutes.ui.theme.MindfulMinutesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindfulMinutesTheme {
                MindfulMinutesApp()
            }
        }
    }
}

@Composable
fun MindfulMinutesApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "mood_check_in",
            deepLinks = listOf(
                navDeepLink { uriPattern = "mindful-minutes://mood_check_in" }
            )
        ) {
            MoodCheckInScreen(navController = navController)
        }
        composable("breathing") {
            BreathingScreen(navController = navController)
        }
        composable("mood_trends") {
            MoodTrendsScreen()
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}
