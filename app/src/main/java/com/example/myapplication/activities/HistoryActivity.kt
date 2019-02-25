package com.example.myapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MyApp.Companion.hist
import com.example.myapplication.MyApp.Companion.prefs
import com.example.myapplication.R
import com.example.myapplication.data.entity.History
import com.example.myapplication.dsl.HistoryDsl
import org.jetbrains.anko.find
import org.jetbrains.anko.setContentView

class HistoryActivity : AppCompatActivity() {

    private lateinit var q: View

    override fun onCreate(savedInstanceState: Bundle?) {

        when (prefs.getString("colors", "")) {
            "Серый" -> setTheme(R.style.AppTheme)
            "Красный" -> setTheme(R.style.Red)
            "Зеленый" -> setTheme(R.style.Green)
            "Синий" -> setTheme(R.style.Blue)
            "Желтый" -> setTheme(R.style.Yellow)
        }
        super.onCreate(savedInstanceState)
        q = HistoryDsl().setContentView(this)
        val listView = q.find<ListView>(HistoryDsl.listView)
        val list = hist.getAll()
        val adapter = ArrayAdapter<History>(this, android.R.layout.simple_expandable_list_item_1, list)
        listView.adapter = adapter
    }
}