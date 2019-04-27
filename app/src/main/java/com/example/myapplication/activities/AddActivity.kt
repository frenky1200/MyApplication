package com.example.myapplication.activities

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

import com.example.myapplication.R
import com.example.myapplication.MyApp.Companion.c
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.interfaces.IMediable

import java.util.Objects

import kotlinx.android.synthetic.main.music_edit.*

class AddActivity : AppCompatActivity() {

    private var idInt: Int? = null
    private var media: Media? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (Objects.requireNonNull(prefs.getString("colors", ""))) {
            "Серый" -> setTheme(R.style.AppTheme)
            "Красный" -> setTheme(R.style.Red)
            "Зеленый" -> setTheme(R.style.Green)
            "Синий" -> setTheme(R.style.Blue)
            "Желтый" -> setTheme(R.style.Yellow)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_edit)
        buttonsave.setOnClickListener { onSaveClick() }

        idInt = intent.getIntExtra("id", -1)
        media = c.getMedia(idInt!!)
        readInf(media!!.mediaType<IMediable>(), media!!.id)
        editText3.setText(media!!.name)
        editText4.setText(media!!.tags)
        editText.setText(media!!.insideUri)
        editText2.setText(media!!.outsideUri)
        title = media!!.name
    }

    private fun <T : IMediable> readInf(media: T, id: Int) {
        editText5!!.setText(c.getMedia1(media, id).name)
    }

    private fun onSaveClick() {
        media!!.name = editText3.text.toString()
        media!!.tags = editText4.text.toString()
        media!!.insideUri = editText.text.toString()
        media!!.outsideUri = editText2.text.toString()
        c.updateBook(media)

        val m: IMediable = c.getMedia1(media!!.mediaType(), media!!.id)
        m.name = editText5.text.toString()
        c.updateMedia(m)
        finish()
    }
}
