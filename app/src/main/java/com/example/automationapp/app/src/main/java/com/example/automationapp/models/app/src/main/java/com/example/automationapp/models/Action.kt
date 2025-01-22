kotlin
package com.example.automationapp.models

data class Action(
    val type: String,
    val x: Float,
    val y: Float,
    val packageName: String,
    val timestamp: Long
)
```

Now let's add the RecordingOverlay class.

Create: `app/src/main/java/com/example/automationapp/RecordingOverlay.kt`
1. Click "Add file" -> "Create new file"
2. Enter path: `app/src/main/java/com/example/automationapp/RecordingOverlay.kt`
3. Paste this content:

```kotlin
package com.example.automationapp

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

class RecordingOverlay(private val context: Context) {
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    fun show() {
        if (overlayView != null) return

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        overlayView = FrameLayout(context).apply {
            setBackgroundResource(android.R.color.transparent)
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        windowManager?.addView(overlayView, params)
    }

    fun hide() {
        overlayView?.let {
            windowManager?.removeView(it)
            overlayView = null
        }
    }
}
