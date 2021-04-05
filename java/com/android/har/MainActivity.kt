package com.android.har

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "MainActivity"

    private val REQUEST_STORAGE_PERMISSION = 100
    private val REQUEST_CAMERA_PERMISSION = 101
    private val REQUEST_PICK_VIDEO = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectLayout = findViewById<LinearLayout>(R.id.select_video)
        val liveRecordView = findViewById<ExtendedFloatingActionButton>(R.id.live_video_fab)

        selectLayout.setOnClickListener(this)
        liveRecordView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.select_video -> {
                if (isPermGranted(READ_EXTERNAL_STORAGE, REQUEST_STORAGE_PERMISSION)) {
                    Log.d(TAG, "Storage permission granted")
                    openFileView()
                }
            }
            R.id.live_video_fab -> {
                Log.d(TAG, "Camera permission granted")
                if (isPermGranted(CAMERA, REQUEST_CAMERA_PERMISSION)) {
                    recordVideo()
                }
            }
        }
    }

    private fun openFileView() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_PICK_VIDEO)
    }

    private fun recordVideo() {
        Log.d(TAG, "recordVideo: ")
    }

    private fun isPermGranted(perm: String, code: Int): Boolean {
        val result = ContextCompat.checkSelfPermission(this, perm)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(perm), code)
            false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()
                openFileView()
            }

            REQUEST_CAMERA_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                recordVideo()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_VIDEO) {
            val uri: Uri? = data?.data
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("path", uri.toString())
            startActivity(intent)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}