package io.a2xe.experiments.myapplicationc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat.setMediaController
import android.widget.MediaController
import android.widget.VideoView



class ReproduceVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproduce_video)

        val video = findViewById(R.id.local_video) as VideoView
        val mediaController = MediaController(this)
        mediaController.setAnchorView(video)

        video.setMediaController(mediaController)
        video.keepScreenOn = true
        video.setVideoPath("android.resource://" + packageName + "/" + R.raw.staying_in_lane)
        video.start()
        video.requestFocus()
    }
}
