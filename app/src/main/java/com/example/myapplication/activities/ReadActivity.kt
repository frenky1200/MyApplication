package com.example.myapplication.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView

import com.example.myapplication.R
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.interfaces.IMediable

import kotlinx.android.synthetic.main.music_present.*

class ReadActivity : AppCompatActivity() {

    private var c: DBController = DBController(this)
    internal var s = ""
    internal lateinit var media: Media

    private fun <T : IMediable> readInf(media: T, id: Int) {
        s = c.getMedia1(media, id).name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (prefs.getString("colors", "Зеленый")) {
            "Серый" -> setTheme(R.style.AppTheme)
            "Красный" -> setTheme(R.style.Red)
            "Зеленый" -> setTheme(R.style.Green)
            "Синий" -> setTheme(R.style.Blue)
            "Желтый" -> setTheme(R.style.Yellow)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_present)
        buttonupd.setOnClickListener { onClick() }
        buttonplay.setOnClickListener { onPlayClick() }
        buttonsearch.setOnClickListener { browse() }
        media = c.getMedia(
                intent.getIntExtra("id", 1))
        imageView2.visibility = View.INVISIBLE
        videoView.visibility = View.INVISIBLE
    }

    private class DownloadImageTask internal constructor(bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {
        internal var bmImage: Any = bmImage

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.d("DownloadImage", e.message)
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            (bmImage as ImageView).visibility = View.VISIBLE
            (bmImage as ImageView).setImageBitmap(result)
        }
    }

    private class DownloadVideoTask internal constructor(bmImage: VideoView) : AsyncTask<String, Void, Void>() {
        internal var videoView: Any = bmImage

        override fun doInBackground(vararg strings: String): Void? {
            (videoView as VideoView).setVideoURI(Uri.parse(strings[0]))
            return null
        }

        override fun onPostExecute(aVoid: Void) {

            (videoView as VideoView).requestFocus(0)
            (videoView as VideoView).visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        scrollView2.removeAllViews()
        videoView.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        title = media.name
        readInf(media.mediaType<IMediable>(), media.id)
        when (media.type) {
            "Music" -> {
                val textView2 = TextView(this)
                textView2.text = s
                textView2.textAlignment = View.TEXT_ALIGNMENT_CENTER
                textView2.setBackgroundResource(R.drawable.side_nav_bar)
                scrollView2.addView(textView2)
            }
            "Excerption" -> {
                val textView2 = TextView(this)
                textView2.text = s
                textView2.setBackgroundResource(R.drawable.side_nav_bar)
                scrollView2.addView(textView2)
            }
            "Image" -> {
                DownloadImageTask(imageView2)
                        .execute(media.insideUri)
            }
            "Film" -> {
                if (media.insideUri != null && media.insideUri != "")
                    DownloadVideoTask(videoView)
                            .execute(media.insideUri)
                videoView.setMediaController(MediaController(this))
            }//scrollView.addView(videoView);
            "Anime" -> {
                if (media.insideUri != null && media.insideUri != "")
                    DownloadVideoTask(videoView)
                            .execute(media.insideUri)
                videoView.setMediaController(MediaController(this))
            }//scrollView.addView(videoView);
        }

    }

    private fun browse() {
        val ii = Intent(this, BrowseActivity::class.java)
        ii.putExtra("search", "yandsearch?text=" + c.getAlbum(media.album) + " " + media.name)
        startActivity(ii)
    }

    private fun onPlayClick() {
        val type: String = when (media.type) {
            "Music" -> "audio/*"
            "Image" -> "image/*"
            "Film" -> "video/*"
            "Excerption" -> "text/*"
            else -> "image/*"
        }

        if ((media.insideUri == null || media.insideUri == "") && (media.outsideUri == "" || media.outsideUri == null)) {
            Toast.makeText(this, "Вы не сохранили источник", Toast.LENGTH_SHORT).show()
        } else {
            if (media.insideUri != null && media.insideUri != "") {
                val playAudioIntent = Intent(Intent.ACTION_VIEW)
                playAudioIntent.setDataAndType(Uri.parse(media.insideUri), type)
                startActivity(playAudioIntent)
            } else {
                val playAudioIntent = Intent(Intent.ACTION_VIEW)
                playAudioIntent.setDataAndType(Uri.parse(media.outsideUri), type)
                startActivity(playAudioIntent)
            }
        }
    }

    internal fun onClick() {
        val intent = Intent(this@ReadActivity, AddActivity::class.java)
        intent.putExtra("id", media.id)
        startActivity(intent)
    }
}
