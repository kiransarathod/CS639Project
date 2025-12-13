package com.example.mindfulminutes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodTrendsScreen() {
    val moodsFlow = remember { MoodRepository.getMoods() }
    val allMoods by moodsFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Trends") },
            )
        }
    ) { padding ->
        if (allMoods.isEmpty()) {
            EmptyState(padding)
        } else {
            DashboardContent(
                padding = padding,
                allMoods = allMoods
            )
        }
    }
}

@Composable
private fun DashboardContent(
    padding: PaddingValues,
    allMoods: List<MoodLog>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SummaryDashboard(allMoods = allMoods)
        }

        if (allMoods.isNotEmpty()) {
            item {
                Text(
                    "Mood Frequency",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                MoodFrequencyChart(moods = allMoods)
            }
        }
    }
}

@Composable
private fun SummaryDashboard(allMoods: List<MoodLog>) {
    val summaryStats = remember(allMoods) {
        val mostCommon = allMoods.groupBy { it.label }
            .maxByOrNull { it.value.size }?.key ?: "N/A"

        val averageScore = if (allMoods.isNotEmpty()) {
            allMoods.map { it.score }.average()
        } else 0.0

        val streak = calculateStreak(allMoods)

        listOf(
            Summary("Total Check-ins", allMoods.size.toString(), Icons.Default.CalendarToday),
            Summary("Common Mood", mostCommon, Icons.Default.EmojiEmotions),
            Summary("Avg. Score", "%.1f / 5".format(averageScore), Icons.Default.Functions),
            Summary("Day Streak", "🔥 $streak", Icons.Default.BarChart)
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(summary = summaryStats[0], modifier = Modifier.weight(1f))
            SummaryCard(summary = summaryStats[1], modifier = Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(summary = summaryStats[2], modifier = Modifier.weight(1f))
            SummaryCard(summary = summaryStats[3], modifier = Modifier.weight(1f))
        }
    }
}

data class Summary(val label: String, val value: String, val icon: ImageVector)

@Composable
private fun SummaryCard(summary: Summary, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Icon(
                imageVector = summary.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = summary.value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = summary.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MoodFrequencyChart(moods: List<MoodLog>) {
    val moodCounts = remember(moods) {
        moods.groupBy { it.label }
             .mapValues { it.value.size }
             .entries.sortedByDescending { it.value }
    }
    val maxCount = moodCounts.maxOfOrNull { it.value } ?: 1

    Card {
        Column(Modifier.padding(16.dp)) {
            moodCounts.forEach { (label, count) ->
                val moodDetails = moods.first { it.label == label }
                val barFraction = count / maxCount.toFloat()

                val animatedFraction by animateFloatAsState(targetValue = barFraction, label = "barAnimation")

                BarRow(
                    emoji = moodDetails.emoji,
                    count = count,
                    fraction = animatedFraction
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun BarRow(emoji: String, count: Int, fraction: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 16.sp, modifier = Modifier.width(32.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Text(
            text = count.toString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.width(40.dp)
        )
    }
}

@Composable
private fun EmptyState(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.BarChart,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Your Mood Trends Await",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "As you log your moods, this screen will fill with insights about your emotional patterns.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// --- Helper Functions ---

// A more robust way to calculate day streaks, ignoring time zones.
private fun getDaySinceEpoch(date: Date, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Long {
    return (date.time + timeZone.getOffset(date.time)) / TimeUnit.DAYS.toMillis(1)
}

private fun calculateStreak(moods: List<MoodLog>): Int {
    if (moods.isEmpty()) return 0

    // Get a distinct, sorted list of days (since epoch) that have mood logs.
    val distinctDays = moods
        .mapNotNull { it.timestamp } // Filter out any logs with null timestamps
        .map { getDaySinceEpoch(it) }
        .distinct()
        .sortedDescending()

    if (distinctDays.isEmpty()) return 0

    // Get the current day since epoch in UTC.
    val currentDay = getDaySinceEpoch(Date())

    // Check if the most recent log was today or yesterday. If not, the streak is 0.
    if (distinctDays.first() < currentDay - 1) {
        return 0
    }

    var streak = 0
    var expectedDay = currentDay

    // If the most recent log was yesterday, start the check from yesterday.
    if (distinctDays.first() == currentDay - 1) {
        expectedDay = currentDay - 1
    }

    // Iterate through the distinct days and count the consecutive ones.
    for (day in distinctDays) {
        if (day == expectedDay) {
            streak++
            expectedDay--
        } else {
            break // The streak is broken.
        }
    }

    return streak
}
