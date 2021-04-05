package com.android.har

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {
    private val TAG = "VideoActivity"

    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.video_view)

        val extras = intent?.extras
        val path = extras?.getString("path")
        Log.d(TAG, "onCreate: $path")

        videoView.setVideoURI(Uri.parse(path))
        videoView.requestFocus()
        videoView.start()
    }
}