package com.example.whatsappclone

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.drawable.toIcon
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsappclone.databinding.ActivityStoryBinding
import com.example.whatsappclone.ui.StatusFragment


class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private var progressBar: ProgressBar? = null
    private var progressStatus = 1
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageBitmap: Bitmap? = intent.getParcelableExtra("imageBitmap")
        if (imageBitmap != null) {
            binding.imageView.setImageBitmap(imageBitmap)
        }
        progressBar = binding.progressBar
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                // Update the progress bar and display the
                //current value in the text view
                handler.post(Runnable {
                    progressBar!!.progress = progressStatus
                })
                try {
                    // Sleep for 100 milliseconds.
                    Thread.sleep(70)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            finish()
        }.start()
    }
}