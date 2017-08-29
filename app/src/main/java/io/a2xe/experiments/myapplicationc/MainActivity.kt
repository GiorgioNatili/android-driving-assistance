package io.a2xe.experiments.myapplicationc

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import org.opencv.android.OpenCVLoader
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        val tv = findViewById(R.id.sample_text) as TextView
        tv.text = stringFromJNI()

        // Button to call OpenCV Camera Activity
        val cameraInit = findViewById(R.id.camera_init) as Button
        cameraInit.setOnClickListener {
           openCamera()
        }

        // Button to call Local Video Activity
        val localVideoInit = findViewById(R.id.local_video_init) as Button
        localVideoInit.setOnClickListener {
            openLocalVideo()
        }
    }

    private fun openLocalVideo() {

        val intent = Intent(applicationContext, ReproduceVideoActivity::class.java)
        startActivity(intent)
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_CAMERA)
    private fun openCamera() {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {

            val intent = Intent(applicationContext, OpenCVCamera::class.java)
            startActivity(intent)

        } else {

            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.access_camera_required),
                    REQUEST_PERMISSION_CAMERA,
                    Manifest.permission.CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {

        super.onActivityResult(requestCode, resultCode, intentData)
        when (requestCode) {

            REQUEST_PERMISSION_CAMERA -> {

                val intent = Intent(applicationContext, OpenCVCamera::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    /**
     * Static methods and values have to be stored in the companion object
     */
    companion object {

        private val TAG = "MainActivity"
        const internal val REQUEST_PERMISSION_CAMERA = 1003

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")

            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "OpenCV not loaded")
            } else {
                Log.d(TAG, "OpenCV loaded")
            }
        }
    }
}
