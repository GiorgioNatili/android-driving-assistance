package io.a2xe.experiments.myapplicationc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*

import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat


class OpenCVCamera : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private var cameraBridgeViewBase: CameraBridgeViewBase? = null

    private lateinit var previewRGBA: MenuItem
    private lateinit var itemPreviewGray: MenuItem
    private lateinit var previewCanny: MenuItem
    private lateinit var previewFeatures: MenuItem

    private var viewMode: Int = Int.MAX_VALUE

    private val baseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> cameraBridgeViewBase!!.enableView()
                else -> super.onManagerConnected(status)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_open_cvcamera)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setTitle(R.string.app_name)

        setSupportActionBar(toolbar)

        cameraBridgeViewBase = findViewById(R.id.camera_view) as CameraBridgeViewBase
        cameraBridgeViewBase!!.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase!!.setCvCameraViewListener(this)
    }

    public override fun onResume() {
        super.onResume()

        if (!OpenCVLoader.initDebug()) {

            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, baseLoaderCallback)
        } else {

            Log.d(TAG, "OpenCV library found inside package. Using it!")
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        Log.i(TAG, "called onCreateOptionsMenu")

        previewRGBA = menu.add(getString(R.string.preview_rgba))
        itemPreviewGray = menu.add(getString(R.string.gray_preview))
        previewCanny = menu.add(getString(R.string.preview_canny))
        previewFeatures = menu.add(getString(R.string.preview_features))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item)

        if (item === previewRGBA) {
            viewMode = VIEW_MODE_RGBA
        } else if (item === itemPreviewGray) {
            viewMode = VIEW_MODE_GRAY
        } else if (item === previewCanny) {
            viewMode = VIEW_MODE_CANNY
        } else if (item === previewFeatures) {
            viewMode = VIEW_MODE_FEATURES
        }

        return true
    }

    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    override fun onCameraViewStopped() {

    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {

//        val image = inputFrame.rgba()
//        val ret_mat = Mat()
//        Core.add(image, Scalar(40.0, 40.0, 40.0, 0.0), ret_mat) //change brightness of video frame


        return inputFrame.rgba()
    }

    companion object {

        private val TAG = "OpenCVCamera"

        private val VIEW_MODE_RGBA = 0
        private val VIEW_MODE_GRAY = 1
        private val VIEW_MODE_CANNY = 2
        private val VIEW_MODE_FEATURES = 5
    }
}
