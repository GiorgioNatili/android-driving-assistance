package io.a2xe.experiments.myapplicationc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        val tv = findViewById(R.id.sample_text) as TextView
        tv.text = stringFromJNI()

        // Button to call OpenCV Camera Activity
        val cameraInit = findViewById(R.id.cameraInit) as Button
        cameraInit.setOnClickListener {

            val intent = Intent(applicationContext, OpenCVCamera::class.java)
            startActivity(intent)
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        private val TAG = "MainActivity"

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
