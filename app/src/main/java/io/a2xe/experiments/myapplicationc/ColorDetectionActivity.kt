package io.a2xe.experiments.myapplicationc

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_color_detection.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class ColorDetectionActivity : AppCompatActivity(), View.OnTouchListener,
        CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var cameraView: CameraBridgeViewBase
    private lateinit var rgba: Mat
    private lateinit var hsvColor: Scalar
    private lateinit var rgbColor: Scalar

    internal var x = -1.0
    internal var y = -1.0

    private val onCameraLoaded = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    cameraView!!.enableView()
                    cameraView!!.setOnTouchListener(this@ColorDetectionActivity)
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_detection)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        cameraView = findViewById(R.id.camera_surface) as CameraBridgeViewBase
        cameraView.visibility = SurfaceView.VISIBLE
        cameraView.setCvCameraViewListener(this)
    }

    public override fun onPause() {
        super.onPause()
        cameraView.disableView()
    }

    public override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, onCameraLoaded)
        } else {
            onCameraLoaded.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraView.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        rgba = Mat()
        rgbColor = Scalar(255.0)
        hsvColor = Scalar(255.0)
    }

    override fun onCameraViewStopped() {
        rgba.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        rgba = inputFrame.rgba()
        return rgba
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val cols = rgba.cols()
        val rows = rgba.rows()

        val yLow = cameraView.height.toDouble() * 0.2401961
        val yHigh = cameraView.height.toDouble() * 0.7696078

        val xScale = cols.toDouble() / cameraView.width.toDouble()
        val yScale = rows.toDouble() / (yHigh - yLow)

        x = event.x.toDouble()
        y = event.y.toDouble()

        y -= yLow

        x *= xScale
        y *= yScale

        if (x < 0 || y < 0 || x > cols || y > rows) return false

        touch_coordinates.text = "X: " + java.lang.Double.valueOf(x) + ", Y: " + java.lang.Double.valueOf(y)

        val touchedRect = Rect()

        touchedRect.x = x.toInt()
        touchedRect.y = y.toInt()

        touchedRect.width = 8
        touchedRect.height = 8

        val touchedRegionRgba = rgba.submat(touchedRect)

        val touchedRegionHsv = Mat()
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL)

        hsvColor = Core.sumElems(touchedRegionHsv)
        val pointCount = touchedRect.width * touchedRect.height
        for (i in hsvColor.`val`.indices)
            hsvColor.`val`[i] /= pointCount.toDouble()

        rgbColor = convertScalarHsv2Rgba(hsvColor)

        touch_color.text = "Color: #" + String.format("%02X", rgbColor.`val`[0].toInt()) +
                String.format("%02X", rgbColor.`val`[1].toInt()) +
                String.format("%02X", rgbColor.`val`[2].toInt())

        touch_color.setTextColor(Color.rgb(rgbColor.`val`[0].toInt(),
                rgbColor.`val`[1].toInt(),
                rgbColor.`val`[2].toInt()))
        touch_coordinates.setTextColor(Color.rgb(rgbColor.`val`[0].toInt(),
                rgbColor.`val`[1].toInt(),
                rgbColor.`val`[2].toInt()))

        return false
    }

    private fun convertScalarHsv2Rgba(hsvColor: Scalar): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)

        return Scalar(pointMatRgba.get(0, 0))
    }
}
