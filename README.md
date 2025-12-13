# CS639 Project – Mindful Minutes

## Idea Proposal & Research
Mindful Minutes is a lightweight Android mental-wellness application designed to help users quickly check in with their emotions, manage daily stress, and practice short mindfulness exercises — all within a few minutes a day.

Team Members 
- Kiran Rathod
- Arun Garwan
- Kushwanth Reddy

✨ Key Features-✅ Implemented

* 🏠 Home Screen with 
	Intuitive navigation to all core features

* 😊 Mood Check-In
	Emoji-based mood selection
	Stress level rating
	Optional reflection notes

* 🌬 Guided Breathing Exercise
	Animated breathing cycles using Jetpack Compose
	Timed inhale and exhale prompts

* 📊 Trends Screen
	Screen structure and navigation implemented

🚧 Upcoming
Mood data persistence
/nMood history tracking
/nCharts and trends visualization
/nUI themes and visual enhancements

| Category         | Tools                                    |
| ---------------- | ---------------------------------------- |
| Platform         | Android                                  |
| Language         | Kotlin                                   |
| UI Framework     | Jetpack Compose                          |
| Architecture     | MVVM                                     |
| State Management | ViewModel + Compose State                |
| Storage          | SharedPreferences / Firebase *(planned)* |

🧩 Architecture Overview
The application follows a clean MVVM architecture:
* UI Layer
		Jetpack Compose screens responsible for rendering UI and handling user interactions.
* ViewModel Layer
		Manages UI state, business logic, and lifecycle awareness.
* Data Layer (Planned)
		Repository-based data handling for mood history and analytics.
This structure ensures scalability, testability, and maintainability.

🚀 Getting Started
Prerequisites
* Android Studio Hedgehog or newer
* Android SDK 24+

Installation
  - git clone git@github.com:kiransarathod/CS639Project.git

1.Open the project in Android Studio

2.Sync Gradle files

3.Run the app on an emulator or physical device

📌 Project Status
* Core UI and navigation: Completed
* Data persistence and analytics: In progress
* Final polish and optimization: Planned

