package com.example.marktheland.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.marktheland.data.ImageRepository
import com.example.marktheland.data.SessionRepository
import com.example.marktheland.model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: SessionRepository
) : ViewModel() {

    val sessions: StateFlow<List<Session>> = repository.sessions

    fun saveSession(context: Context, name: String, timestamp: String, bitmap: Bitmap?) {
        val imagePath = bitmap?.let { ImageRepository.saveImageToInternalStorage(context, it, "$timestamp.png") }
        val session = Session(timestamp, name, imagePath)

        repository.saveSession(session) // 🔥 UI will update automatically
        Log.d("SessionRepository", "Saved session: ${session.name}")
    }
}
