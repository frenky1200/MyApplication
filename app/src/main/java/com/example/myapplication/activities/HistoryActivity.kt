package com.example.myapplication.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dsl.SomeActivity
import com.example.myapplication.R
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class HistoryActivity : AppCompatActivity() {

    private lateinit var q: View

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
        q = SomeActivity().setContentView(this)
        val listView = q.find<ListView>(SomeActivity.listView)
        val list = arrayListOf("ads", "ewq", "qwe")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list)
        listView.adapter = adapter
    }
}