package com.example.marktheland.model

import java.io.Serializable

data class Session(
    val timestamp: String,
    val name: String,
    val imagePath: String? = null // Path to stored image
) : Serializable

