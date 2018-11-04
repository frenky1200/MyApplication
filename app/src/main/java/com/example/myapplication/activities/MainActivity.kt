package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

import android.view.Menu
import android.view.MenuItem

import com.example.myapplication.R
import com.example.myapplication.fragments.CollectionFragment
import com.example.myapplication.fragments.FindFragment
import com.example.myapplication.services.MyService
import com.google.android.material.navigation.NavigationView

import com.example.myapplication.R.id.drawer_layout

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_main)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        if (!MyService.isNotificationAccessEnabled(this)) {
            AlertDialog.Builder(this)
                    .setPositiveButton(
                            android.R.string.ok
                    ) { dialogInterface, i ->
                        val action: String = if (Build.VERSION.SDK_INT >= 22) {
                            Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
                        } else {
                            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
                        }
                        startActivity(Intent(action))
                    }
                    .show()
        } else {
            startService(Intent(this, MyService::class.java))
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val fr: Fragment
        fr = when (id) {
            R.id.action_settings -> {
                SettingActivity.SettingsFragment()
            }
            R.id.action_quit -> {
                val pref = getSharedPreferences("activities.LoginActivity", Context.MODE_PRIVATE)
                pref.getBoolean("authorization", false)
                pref.edit().putBoolean("authorization", false).apply()
                LoginActivity.logout(this)
                return true
            }
            else -> {
                FindFragment()
            }
        }
        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fr)
                .addToBackStack("a")
                .commit()
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        val drawer = findViewById<DrawerLayout>(drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        val fr: Fragment
        when (id) {
            R.id.music -> {
                collectionType = Nav.Music
                fr = CollectionFragment()
            }
            R.id.anime -> {
                collectionType = Nav.Anime
                fr = CollectionFragment()
            }
            R.id.excerption -> {
                collectionType = Nav.Excerption
                fr = CollectionFragment()
            }
            R.id.film -> {
                collectionType = Nav.Film
                fr = CollectionFragment()
            }
            R.id.image -> {
                collectionType = Nav.Image
                fr = CollectionFragment()
            }
            R.id.setting -> {
                fr = SettingActivity.SettingsFragment()
            }
            R.id.find -> {
                fr = FindFragment()
            }
            R.id.nav_send -> {
                startActivity(Intent(this, Add::class.java))
                return true
            }
            R.id.browser -> {
                startActivity(Intent(this, BrowseActivity::class.java))
                return true
            }
            else -> {
                fr = FindFragment()
            }
        }

        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fr)
                .addToBackStack("a")
                .commit()
        return true
    }

    enum class Nav constructor(private val mResourceId: Int) {

        Music(R.string.music),
        Book(R.string.book),
        Film(R.string.film),
        Excerption(R.string.excerption),
        Anime(R.string.anime),
        Image(R.string.image);

        fun value(App: Context): String {
            return App.getString(mResourceId)
        }

    }

    companion object {

        fun display(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        var collectionType: Enum<Nav> = Nav.Music
    }
}

