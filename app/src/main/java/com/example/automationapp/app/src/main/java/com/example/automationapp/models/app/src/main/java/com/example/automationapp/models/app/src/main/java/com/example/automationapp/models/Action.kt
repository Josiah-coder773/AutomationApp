kotlin
package com.example.automationapp.models

data class Action(
    val type: String,
    val x: Float,
    val y: Float,
    val packageName: String,
    val timestamp: Long
)
