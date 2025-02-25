package com.example.marktheland.data

import android.content.SharedPreferences
import com.example.marktheland.model.Session
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionRepository(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()
    private val key = "session_history"

    private val _sessions = MutableStateFlow<List<Session>>(loadSessions())
    val sessions: StateFlow<List<Session>> = _sessions.asStateFlow()

    private fun loadSessions(): List<Session> {
        val json = sharedPreferences.getString(key, "[]")
        val type = object : TypeToken<List<Session>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveSession(session: Session) {
        val sessions = _sessions.value.toMutableList()
        sessions.add(session)

        sharedPreferences.edit().putString(key, gson.toJson(sessions)).apply()
        _sessions.value = sessions // 🔥 Trigger UI update
    }
}
