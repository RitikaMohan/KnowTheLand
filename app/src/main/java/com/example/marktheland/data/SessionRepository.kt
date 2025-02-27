package com.example.marktheland.data

import android.content.SharedPreferences
import android.util.Log
import com.example.marktheland.model.Session
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionRepository(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()
    private val key = "session_history"

    private val _sessions = MutableStateFlow<List<Session>>(emptyList()) // 🔥 Reactive List
    val sessions: StateFlow<List<Session>> = _sessions

    init {
        loadSessions()
    }

    private fun loadSessions() {
        val json = sharedPreferences.getString(key, "[]")
        val type = object : TypeToken<List<Session>>() {}.type
        _sessions.value = gson.fromJson(json, type) ?: emptyList()
    }

    fun saveSession(session: Session) {
        val updatedSessions = _sessions.value.toMutableList()
        updatedSessions.add(session)

        sharedPreferences.edit().putString(key, gson.toJson(updatedSessions)).apply()

        _sessions.value = updatedSessions // 🔥 Notify UI
        Log.d("SessionRepository", "Session saved: ${session.name}, Total: ${_sessions.value.size}")
    }
}

