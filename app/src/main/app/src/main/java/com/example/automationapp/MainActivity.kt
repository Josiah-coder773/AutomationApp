kotlin
package com.example.automationapp

import android.os.Bundle
import android.provider.Settings
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.view.LayoutInflater
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var btnStartRecording: Button
    private lateinit var btnSchedule: Button
    private lateinit var recordingManager: RecordingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartRecording = findViewById(R.id.btnStartRecording)
        btnSchedule = findViewById(R.id.btnSchedule)
        
        recordingManager = RecordingManager.getInstance(this)
        setupUI()
        checkPermissions()
    }

    private fun setupUI() {
        btnStartRecording.setOnClickListener {
            if (!recordingManager.isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
        }

        btnSchedule.setOnClickListener {
            schedulePlayback()
        }
    }

    private fun startRecording() {
        if (!checkPermissions()) {
            Toast.makeText(this, "Please grant required permissions", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            recordingManager.startRecording()
            btnStartRecording.text = "Stop Recording"
        }
    }

    private fun stopRecording() {
        lifecycleScope.launch {
            recordingManager.stopRecording()
            btnStartRecording.text = "Start Recording"
        }
    }

    private fun schedulePlayback() {
        Toast.makeText(this, "Scheduling feature coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun checkPermissions(): Boolean {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            return false
        }

        if (!isAccessibilityServiceEnabled()) {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            return false
        }

        return true
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityServiceString = "${packageName}/${AutomationService::class.java.canonicalName}"
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.split(":").contains(accessibilityServiceString)
    }
}
