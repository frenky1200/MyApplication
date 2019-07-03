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
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MyApp.Companion.c
import com.example.myapplication.R
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.entity.Music
import com.example.myapplication.services.MyService
import kotlinx.android.synthetic.main.activity_played.*
import org.jetbrains.anko.share
import org.jetbrains.anko.startActivity
import java.net.URLEncoder

class PlayedActivity : AppCompatActivity() {

    private lateinit var name:String
    private lateinit var artist:String
    private lateinit var ms: MyService
    private lateinit var media:Media

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            ms = MyService().MyBinder().service
            ms.setAct(this@PlayedActivity)
            for(m in ms.getMediaControllers()){
                if(m.playbackState!=null)
                    if(m.playbackState!!.state==PlaybackState.STATE_PLAYING || m.playbackState!!.state==PlaybackState.STATE_PAUSED){
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
            "Пурпурный" -> setTheme(R.style.Purple)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_played)
        addnew.setOnClickListener{ addClick() }
        imageButton3.setOnClickListener { prevSkipClick() }
        imageButton4.setOnClickListener { stopStartClick() }
        imageButton5.setOnClickListener { skipClick() }
        imageButton6.setOnClickListener { brouse() }
    }

    override fun onResume() {
        super.onResume()
        bindService( Intent(this, MyService::class.java ), conn, Context.BIND_AUTO_CREATE )
    }

    override fun onPause() {
        unbindService( conn )
        ms.setAct(null)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_readmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_search -> {
                startActivity<BrowseActivity>("search" to "yandsearch?text="+ URLEncoder.encode("${c.getAlbum(media.album)} ${media.name}", "UTF-8"))
            }
            R.id.action_update -> {
                startActivity<AddActivity>("id" to media.id)
            }
            R.id.action_share -> {
                share(media.outsideUri)
            }
            else -> {
                stopStartClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prevSkipClick(){
        for(m in ms.getMediaControllers()){
            if(m.playbackState!=null)
                if(m.playbackState!!.state==PlaybackState.STATE_PLAYING){
                    m.transportControls.skipToPrevious()
                }
        }
    }

    private fun stopStartClick(){
        for(m in ms.getMediaControllers()){
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
        for(m in ms.getMediaControllers()){
            if(m.playbackState!=null)
            if(m.playbackState!!.state==PlaybackState.STATE_PLAYING){
                m.transportControls.skipToNext()
            }
        }
    }

    private fun brouse(){
        startActivity<BrowseActivity>("search" to "yandsearch?text="+ URLEncoder.encode("$artist $name", "UTF-8"))
    }

    private fun addClick(){
        val albumId = c.addAlbum(artist,"Music")
        c.addMedia(name, "Music", albumId.toInt())
        //unbindService(conn)
        //bindService(Intent(this, MyService::class.java),conn,Context.BIND_AUTO_CREATE)
    }

    private fun reg(str: String):String{
        val s = str.replace("[_+\"]".toRegex()," ")
                            .replace("\\([\\s\\S]*\\)|\\[[\\s\\S]*\\]|\\{[\\s\\S]*\\}".toRegex(),"")
                            .split(' ').toTypedArray()
        var s1 = ""
        for(a in s){
            if (a.isNotEmpty()){
                s1 = s1 + a.capitalize() + " "
            }
        }
        return s1.trim()
    }

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