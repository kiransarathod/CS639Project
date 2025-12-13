package com.example.mindfulminutes

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

object MoodRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    private const val MOOD_COLLECTION = "moods"

    suspend fun addMood(mood: MoodLog) {
        db.collection(MOOD_COLLECTION).add(mood).await()
    }

    fun getMoods(): Flow<List<MoodLog>> {
        return db.collection(MOOD_COLLECTION)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot -> snapshot.toObjects(MoodLog::class.java) }
    }
}