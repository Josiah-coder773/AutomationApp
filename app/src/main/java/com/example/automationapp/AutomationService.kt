kotlin
package com.example.automationapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.graphics.Rect
import com.example.automationapp.models.Action

class AutomationService : AccessibilityService() {
    private val recordingManager by lazy { RecordingManager.getInstance(this) }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
            notificationTimeout = 100
        }
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!recordingManager.isRecording || event.eventType != AccessibilityEvent.TYPE_VIEW_CLICKED) {
            return
        }

        val source = event.source ?: return
        val bounds = Rect()
        source.getBoundsInScreen(bounds)

        recordingManager.addAction(
            Action(
                type = "tap",
                x = bounds.centerX().toFloat(),
                y = bounds.centerY().toFloat(),
                packageName = event.packageName?.toString() ?: "",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    override fun onInterrupt() {
        // Handle interruption
    }
}
