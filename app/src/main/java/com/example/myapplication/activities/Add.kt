package com.example.myapplication.activities

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.myapplication.R
import com.example.myapplication.adapters.NavAdapter
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Album
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.interfaces.IMediable
import kotlinx.android.synthetic.main.music_edit.*

class Add : AppCompatActivity() {

    private lateinit var c : DBController
    private lateinit var adapter : NavAdapter
    private lateinit var adapter2 : ArrayAdapter<Album>
    private lateinit var type : String
    private lateinit var album : Album
    private lateinit var name : String
    private lateinit var albums : List<Album>
    private lateinit var uri : Uri

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
        setContentView(R.layout.music_edit)

        c = DBController(this)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(3)
        val intent = intent

        /*when {
            intent.hasExtra("outsideUri") -> {
                editText2.setText(intent.getStringExtra("outsideUri"))
                buttonsave.setOnClickListener { addClick() }
            }
            intent.hasExtra("id") -> {
                val idInt = getIntent().getIntExtra("id", -1)
                val media = c.getMedia(idInt)
                readInf<IMediable>(media.mediaType(), media.id)
                editText3.setText(media.name)
                editText4.setText(media.tags)
                editText.setText(media.insideUri)
                editText2.setText(media.outsideUri)
                title = media.name
                buttonsave.setOnClickListener { onSaveClick(media) }
            }
            intent.data!=null -> {
                uri = intent.data
                editText.setText(uri.toString())
                buttonsave.setOnClickListener { addClick() }
            }
            intent.clipData!=null -> {
                val text = intent.clipData?.getItemAt(0)?.text?:""
                editText5.setText(text)
                buttonsave.setOnClickListener { addClick() }
            }
        }*/

        if (intent.hasExtra("outsideUri")) editText2.setText(intent.getStringExtra("outsideUri"))
        if (intent.data!=null){
            uri = intent.data!!
            editText.setText(uri.toString())
        }else {
            val text = intent.clipData?.getItemAt(0)?.text?:""
            editText5.setText(text)
        }

        buttonsave.setOnClickListener { addClick() }
        val types = MainActivity.Nav.values()

        val pref = getPreferences(MODE_PRIVATE)
        val p = pref.getInt("type", 1)
        val p2 = pref.getInt("name", 1)
        adapter = NavAdapter(this, R.layout.ada, types)
        spinner.adapter = adapter
        spinner.setSelection(p)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                albums = c.getAllAlbum((spinner.selectedItem as MainActivity.Nav).name)
                adapter2 = ArrayAdapter(this@Add, android.R.layout.simple_expandable_list_item_1, albums)
                spinner2.adapter = adapter2
                adapter2.notifyDataSetChanged()

                spinner2.setSelection(p2)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    private fun <T : IMediable> readInf(media: T, id: Int) {
        var media = media
        media = c.getMedia1(media, id)
        editText5.setText(media.name)
    }

    fun OnSaveClick(media: Media) {
        media.name = editText3.text.toString()
        media.tags = editText4.text.toString()
        media.insideUri = editText.text.toString()
        media.outsideUri = editText2.text.toString()
        c.updateBook(media)

        val m: IMediable = c.getMedia1(media.mediaType(), media.id)
        m.name = editText5.text.toString()
        c.updateMedia(m)
        finish()
    }

    private fun addClick(){

        type = (spinner.selectedItem as MainActivity.Nav).name
        album = spinner2.selectedItem as Album
        name  = editText3.text.toString()
        val id = c.addMedia(name, type, album.id)
        val m: IMediable
        val media = c.getMedia(id).apply {
            tags = editText4.text.toString()
            insideUri = editText.text.toString()
            outsideUri = editText2.text.toString()
        }
        c.updateBook(media)
        m = c.getMedia1(media.mediaType(), media.id)
        m.name = editText5.text.toString().trim()
        c.updateMedia(m)

        getPreferences(MODE_PRIVATE).apply {
            edit().putInt("type", spinner.selectedItemPosition).apply()
            edit().putInt("name", spinner2.selectedItemPosition).apply()
        }
        finish()
    }
}