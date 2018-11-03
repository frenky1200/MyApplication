package com.example.myapplication.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.entity.Music
import com.example.myapplication.services.MyService
import kotlinx.android.synthetic.main.activity_played.*

class PlayedActivity : AppCompatActivity() {

    private lateinit var name:String
    private lateinit var artist:String
    private lateinit var ms: MyService
    val c : DBController = DBController(this)

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            ms = (MyService().MyBinder()).service
            //Toast.makeText(this@PlayedActivity,"created",Toast.LENGTH_SHORT).show()
            ms.setAct(this@PlayedActivity)
            for(m in ms.mediaControllers){
                if(m.playbackState!=null)
                    if(m.playbackState!!.state==PlaybackState.STATE_PLAYING){
                        doIt(m.metadata!!)
                    }
                    else{
                        if(m.playbackState!!.state==PlaybackState.STATE_PAUSED)
                            doIt(m.metadata!!)
                    }
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (prefs.getString("colors", "")) {
            "Серый" -> setTheme(R.style.AppTheme)
            "Красный" -> setTheme(R.style.Red)
            "Зеленый" -> setTheme(R.style.Green)
            "Синий" -> setTheme(R.style.Blue)
            "Желтый" -> setTheme(R.style.Yellow)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_played)
        addnew.setOnClickListener{addClick()}
        imageButton3.setOnClickListener { prevSkipClick() }
        imageButton4.setOnClickListener { stopStartClick() }
        imageButton5.setOnClickListener { skipClick() }
        imageButton6.setOnClickListener { brouse()}
    }

    override fun onResume() {
        super.onResume()
        bindService(Intent(this, MyService::class.java),conn,Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(conn)
    }

    private fun prevSkipClick(){
        for(m in ms.mediaControllers){
            if(m.playbackState!=null)
                if(m.playbackState!!.state==PlaybackState.STATE_PLAYING){
                    m.transportControls.skipToPrevious()
                }
        }
    }

    private fun stopStartClick(){
        for(m in ms.mediaControllers){
            if(m.playbackState!=null)
                if(m.playbackState!!.state==PlaybackState.STATE_PLAYING){
                    m.transportControls.pause()
                    imageButton4.setImageResource(android.R.drawable.ic_media_play)
                }
                else{
                    if(m.playbackState!!.state==PlaybackState.STATE_PAUSED)
                     m.transportControls.play()
                    imageButton4.setImageResource(android.R.drawable.ic_media_pause)
                }
        }
    }

    private fun skipClick(){
        for(m in ms.mediaControllers){
            if(m.playbackState!=null)
            if(m.playbackState!!.state==PlaybackState.STATE_PLAYING){
                m.transportControls.skipToNext()
            }
        }
    }

    private fun brouse(){
        val ii = Intent(this, BrowseActivity::class.java)
        ii.putExtra("search", "yandsearch?text=$artist - $name")
        startActivity(ii)
    }

    private fun addClick(){
        val a = c.addAlbum(artist,"Music")
        c.addMedia(name, "Music", a.toInt())
        unbindService(conn)
        bindService(Intent(this, MyService::class.java),conn,Context.BIND_AUTO_CREATE)
    }

    private fun reg(str: String):String{
        var s1 = str.replace("[_+\"]".toRegex()," ").trim()
        s1 = s1.replace("\\([\\s\\S]*\\)|\\[[\\s\\S]*\\]|\\{[\\s\\S]*\\}".toRegex(),"").trim()
        val s: Array<String> = s1.split(' ').toTypedArray()
        s1 = ""
        for(a in s){
            s1 = when (a.length) {
                1 -> s1 + a.substring(0, 1).toUpperCase() + " "
                0 -> s1
                else -> s1 +a.substring(0, 1).toUpperCase() + a.substring(1) + " "
            }
        }
        return s1.trim()
    }

    lateinit var media:Media

    fun doIt(meta: MediaMetadata){
        name = reg(meta.getString(MediaMetadata.METADATA_KEY_TITLE))
        artist = reg(meta.getString(MediaMetadata.METADATA_KEY_ARTIST))
        metatv.text = artist.plus(" - ").plus(name)
        media =  c.findByName(name, artist)
        title = artist.plus(" - ").plus(name)
        if(media.name != "default") {
            val m: Music = c.getMedia1(media.mediaType(), media.id)
            texttv.text = m.name
        }else {
            Toast.makeText(this@PlayedActivity, "New Track?",Toast.LENGTH_SHORT).show()
            texttv.text = meta.getString(MediaMetadata.METADATA_KEY_WRITER)
        }
    }
}