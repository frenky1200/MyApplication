package com.example.myapplication.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.api.MusixApi
import com.example.myapplication.controller.Controller
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.entity.Music
import com.example.myapplication.model.Body
import com.example.myapplication.model.Message
import com.example.myapplication.model.TrackSearch
import com.example.myapplication.modelLyr.LyricsGet
import com.example.myapplication.services.MyService
import kotlinx.android.synthetic.main.activity_played.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PlayedActivity : AppCompatActivity() {

    lateinit var name:String
    private lateinit var artist:String
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
        imageButton6.setOnClickListener { browse()}
    }

    override fun onResume() {
        super.onResume()
        bindService(Intent(this, MyService::class.java),conn,Context.BIND_AUTO_CREATE)
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
                    imageButton4.setImageResource(android.R.drawable.ic_media_play)
                }
                else{
                    if(m.playbackState.state==PlaybackState.STATE_PAUSED)
                     m.transportControls.play()
                    imageButton4.setImageResource(android.R.drawable.ic_media_pause)
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

    private fun brouse(){
        val ii = Intent(this, AddActivity::class.java)
        ii.putExtra("id", media.id)
        startActivity(ii)
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.musixmatch.com/search/$title"))
        startActivity(i)
    }

    private fun browse(){
        val musixApi = Controller.getApi()

        /* Пример вызова синхронного запроса. В главном потоке ТАБУ!
        try {
            Response response = umoriliApi.getData("bash", 50).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        val api = "8b9ac547ea42965dc98d4649b551de96"
        musixApi.getData(name, artist, 1, api ).enqueue(object : Callback<TrackSearch> {
            override fun onResponse(call: Call<TrackSearch>, response: Response<TrackSearch>) {
                var id = response.body().message.body.trackList.get(0).track.trackId
                musixApi.getLyric(id, api).enqueue(object : Callback<LyricsGet> {
                    override fun onResponse(call: Call<LyricsGet>, response: Response<LyricsGet>) {
                        texttv.text = response.body().message.body.lyrics.lyricsBody
                    }

                    override fun onFailure(call: Call<LyricsGet>?, t: Throwable?) {
                        Toast.makeText(this@PlayedActivity, "An error occurred during networking", Toast.LENGTH_SHORT).show()

                    }
                })
            }

            override fun onFailure(call: Call<TrackSearch>, t: Throwable) {
                Toast.makeText(this@PlayedActivity, "An error occurred during networking", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addClick(){
        c.addAlbum(reg(artist),"Music")
        c.addMedia(reg(name), "Music", reg(artist))
        unbindService(conn)
        bindService(Intent(this, MyService::class.java),conn,Context.BIND_AUTO_CREATE)
    }

    lateinit var media:Media

    fun doIt(meta: MediaMetadata){
        name = meta.getString(MediaMetadata.METADATA_KEY_TITLE)
        artist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST)
        name = name.replace("\\([\\s\\S]*\\)|\\[[\\s\\S]*\\]|\\{[\\s\\S]*\\}".toRegex(),"")
        artist = artist.replace("\\([\\s\\S]*\\)|\\[[\\s\\S]*\\]|\\{[\\s\\S]*\\}".toRegex(),"")
        name = name.replace("[_+\"]".toRegex()," ").trim()
        artist = artist.replace("[_+\"]".toRegex()," ").trim()
        metatv.text = artist.plus(" - ").plus(name)
        media =  c.findByName(name, artist)
        title = media.toString()
        if(media.name != "default") {
            val m: Music = c.getMedia1(media.mediaType(), media.id)
            texttv.text = m.name
        }else {
            Toast.makeText(this@PlayedActivity, "New Track?",Toast.LENGTH_SHORT).show()
            texttv.text = meta.getString(MediaMetadata.METADATA_KEY_WRITER)
        }
    }
}