package com.example.myapplication.activities

//import com.example.myapplication.dsl.ExampleActivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MyApp.Companion.c
import com.example.myapplication.R
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.interfaces.IMediable
import kotlinx.android.synthetic.main.music_present.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import java.net.URL

class ReadActivity : AppCompatActivity() {

    private var s = ""
    internal lateinit var media: Media

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

        //val a = ExampleActivity().setContentView(this)
        media = c.getMedia(intent.getIntExtra("id", 1))
        imageView2.visibility = View.INVISIBLE
        videoView.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_readmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_search -> {
                startActivity<BrowseActivity>("search" to "yandsearch?text=${c.getAlbum(media.album)} ${media.name}")
            }
            R.id.action_update -> {
                startActivity<AddActivity>("id" to media.id)
            }
            R.id.action_share -> {
                share(media.outsideUri)
            }
            else -> {
                onPlayClick()
            }
        }
        return super.onOptionsItemSelected(item)
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
        s = c.getMedia1(media.mediaType<IMediable>(), media.id).name
        when (media.type) {
            "Music" -> {
                val textView2 = TextView(this)
                textView2.text = s
                textView2.textAlignment = View.TEXT_ALIGNMENT_CENTER
                scrollView2.addView(textView2)
            }
            "Excerption" -> {
                val textView2 = TextView(this)
                textView2.text = s
                scrollView2.addView(textView2)
            }
            "Image" -> {
                //DownloadImageTask(imageView2).execute(media.insideUri)
                GlobalScope.launch {
                    val b = withContext(Dispatchers.Default) {
                        BitmapFactory.decodeStream(URL(media.outsideUri)
                                .openStream())
                    }
                    runOnUiThread{
                        imageView2.visibility = View.VISIBLE
                        imageView2.setImageBitmap(b)
                    }
                }
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

    private fun onPlayClick() {

        val type: String = when (media.type) {
            "Music" -> "audio/*"
            "Image" -> "image/*"
            "Film" -> "video/*"
            "Excerption" -> "text/*"
            else -> "image/*"
        }

        media.insideUri?.play(type)?:media.outsideUri?.play(type)


        /*if ((media.insideUri == null || media.insideUri == "") && (media.outsideUri == "" || media.outsideUri == null)) {
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
        }*/
    }

    private fun String.play(type: String):Boolean {
        if (this != "") {
            val playAudioIntent = Intent(Intent.ACTION_VIEW)
            playAudioIntent.setDataAndType(Uri.parse(this), type)
            startActivity(playAudioIntent)
            return true
        }
        Toast.makeText(this@ReadActivity, "Вы не сохранили источник", Toast.LENGTH_SHORT).show()
        return false
    }
}
