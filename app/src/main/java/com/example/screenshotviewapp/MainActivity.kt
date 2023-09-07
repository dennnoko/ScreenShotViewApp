package com.example.screenshotviewapp

import android.content.Intent
import android.graphics.PixelFormat
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private lateinit var screenshotButton: Button
    private lateinit var mediaProjectionManager: MediaProjectionManager
    private lateinit var screenshotLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenshotButton = Button(this)
        screenshotButton.text = "Take Screenshot"
        screenshotButton.setOnClickListener { takeScreenshot() }

        // Initialize the MediaProjectionManager
        mediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        // Request permission to draw overlay (SYSTEM_ALERT_WINDOW) if not granted
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(intent)
        }

        // Initialize the screenshot launcher
        screenshotLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result, such as saving the screenshot
            }
        }

        // Add the screenshot button to the overlay view
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.CENTER
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(screenshotButton, params)
    }

    private fun takeScreenshot() {
        val screenshotIntent = mediaProjectionManager.createScreenCaptureIntent()
        screenshotLauncher.launch(screenshotIntent)
    }
}