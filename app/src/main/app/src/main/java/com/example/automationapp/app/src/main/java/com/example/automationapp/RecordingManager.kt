kotlin
package com.example.automationapp

import android.content.Context
import com.example.automationapp.models.Action

class RecordingManager private constructor(private val context: Context) {
    var isRecording = false
        private set

    private val actions = mutableListOf<Action>()
    private var overlay: RecordingOverlay? = null

    companion object {
        @Volatile
        private var instance: RecordingManager? = null

        fun getInstance(context: Context): RecordingManager {
            return instance ?: synchronized(this) {
                instance ?: RecordingManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun startRecording() {
        isRecording = true
        actions.clear()
        showOverlay()
    }

    fun stopRecording() {
        isRecording = false
        hideOverlay()
    }

    fun addAction(action: Action) {
        if (isRecording) {
            actions.add(action)
        }
    }

    private fun showOverlay() {
        overlay = RecordingOverlay(context).also { it.show() }
    }

    private fun hideOverlay() {
        overlay?.hide()
        overlay = null
    }

    fun getRecordedActions(): List<Action> {
        return actions.toList()
    }
}
