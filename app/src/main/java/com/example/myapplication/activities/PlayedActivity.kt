package com.example.myapplication.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Music
import kotlinx.android.synthetic.main.activity_played.*

class PlayedActivity : AppCompatActivity() {
    lateinit var ms: MyService
    lateinit var meta: MediaMetadata
    val c : DBController = DBController(this)
    var bound = false
    private val conn = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            ms = (MyService().MyBinder()).service
            bound = true
            //Toast.makeText(this@PlayedActivity,"created",Toast.LENGTH_SHORT).show()
            ms.setAct(this@PlayedActivity)
            for(m in ms.mediaControllers){
                if(m.playbackState!=null)
                    if(m.playbackState.state==PlaybackState.STATE_PLAYING){
                        doIt(m.metadata)
                    }
                    else{
                        if(m.playbackState.state==PlaybackState.STATE_PAUSED)
                            doIt(m.metadata)
                    }
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_played)
        addnew.setOnClickListener{addClick()}
        imageButton3.setOnClickListener { prevSkipClick() }
        imageButton4.setOnClickListener { stopStartClick() }
        imageButton5.setOnClickListener { skipClick() }
    }

    override fun onResume() {
        super.onResume()
        bindService(Intent(this,MyService::class.java),conn,Context.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()
        unbindService(conn)
    }

    private fun reg(str: String):String{
        val s: Array<String> = str.split(' ').toTypedArray()
        var s1 = ""
        for(a in s){
            s1 = s1 + a.substring(0,1).toUpperCase()+a.substring(1).toLowerCase()+" "
        }
        return s1.trim()
    }

    private fun prevSkipClick(){
        for(m in ms.mediaControllers){
            if(m.playbackState!=null)
                if(m.playbackState.state==PlaybackState.STATE_PLAYING){
                    m.transportControls.skipToPrevious()
                }
        }
    }

    private fun stopStartClick(){
        for(m in ms.mediaControllers){
            if(m.playbackState!=null)
                if(m.playbackState.state==PlaybackState.STATE_PLAYING){
                    m.transportControls.pause()
                }
                else{
                    if(m.playbackState.state==PlaybackState.STATE_PAUSED)
                     m.transportControls.play()
                }
        }
    }

    private fun skipClick(){
        for(m in ms.mediaControllers){
            if(m.playbackState!=null)
            if(m.playbackState.state==PlaybackState.STATE_PLAYING){
                m.transportControls.skipToNext()
            }
        }
    }

    private fun addClick(){
        c.addAlbum(reg(artist),"Music")
        c.addMedia(reg(name), "Music", reg(artist))
    }
    lateinit var name:String
    lateinit var artist:String
    fun doIt(meta: MediaMetadata){
        name = meta.getString(MediaMetadata.METADATA_KEY_TITLE)
        artist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST)
        name = name.replace("\\([\\s\\S]*\\)|\\[[\\s\\S]*\\]|\\{[\\s\\S]*\\}".toRegex(),"")
        artist = artist.replace("\\([\\s\\S]*\\)|\\[[\\s\\S]*\\]|\\{[\\s\\S]*\\}".toRegex(),"")
        name = name.replace("[_+\"]".toRegex()," ").trim()
        artist = artist.replace("[_+\"]".toRegex()," ").trim()
        metatv.text = artist.plus(" - ").plus(name)
        val media =  c.findByName(name, artist)
        title = media.toString()
        if(media.name != "default") {
            var m: Music = c.getMedia1(media.mediaType(), media.id)
            texttv.text = m.name
        }else {
            Toast.makeText(this@PlayedActivity, "New Track?",Toast.LENGTH_SHORT).show()
            texttv.text = meta.getString(MediaMetadata.METADATA_KEY_WRITER)
        }
    }
}