package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Album
import com.example.myapplication.data.interfaces.IMediable
import kotlinx.android.synthetic.main.music_edit.*
import java.sql.PreparedStatement
import java.util.prefs.Preferences

class Add : AppCompatActivity() {

    private lateinit var c : DBController
    private lateinit var adapter : ArrayAdapter<MainActivity.Nav>
    private lateinit var adapter2 : ArrayAdapter<Album>
    private lateinit var type : String
    private lateinit var album : String
    private lateinit var name : String
    private lateinit var albums : List<Album>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_edit)

        val intent = intent
        val data = intent.extras["android.intent.extra.INTENT"]
        val text = (data as Intent).clipData.getItemAt(0).text
        editText5.setText(text)

        buttonsave.setOnClickListener { addClick() }
        c = DBController(this)
        val types = MainActivity.Nav.values()

        val pref = getPreferences(MODE_PRIVATE)
        val p = pref.getInt("type", 1)
        val p2 = pref.getInt("name", 1)
        adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, types)
        spinner.adapter = adapter
        spinner.setSelection(p)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                albums = c.getAllAlbum(spinner.selectedItem.toString())
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

    private fun addClick(){

        type = spinner.selectedItem.toString()
        album = spinner2.selectedItem.toString()
        name  = editText3.text.toString()
        val id = c.addMedia(name, type, album)
        val m: IMediable
        val media = c.getMedia(id)
        media.tags = editText4.text.toString()
        c.updateBook(media)
        m = c.getMedia1(media.mediaType(), media.id)
        m.name = editText5.text.toString().trim()
        c.updateMedia(m)

        val pref = getPreferences(MODE_PRIVATE)
        val ed = pref.edit().putInt("type", spinner.selectedItemPosition).commit()
        val ed2 = pref.edit().putInt("name", spinner2.selectedItemPosition).commit()

        finish()
    }
}