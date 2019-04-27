package com.example.myapplication.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.MyApp.Companion.prefs
import com.example.myapplication.R
import com.example.myapplication.dsl.MainDsl
import com.example.myapplication.fragments.CollectionFragment
import com.example.myapplication.fragments.FindFragment
import com.example.myapplication.services.MyService
import com.google.android.material.navigation.NavigationView
import org.jetbrains.anko.*
import ru.profit_group.scorocode_sdk.Callbacks.CallbackLoginUser
import ru.profit_group.scorocode_sdk.Responses.user.ResponseLogin
import ru.profit_group.scorocode_sdk.scorocode_objects.User

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
lateinit var a: View
    override fun onCreate(savedInstanceState: Bundle?) {

        when (prefs.getString("colors", "Зеленый")) {
            "Серый" -> setTheme(R.style.AppTheme)
            "Красный" -> setTheme(R.style.Red)
            "Зеленый" -> setTheme(R.style.Green)
            "Синий" -> setTheme(R.style.Blue)
            "Желтый" -> setTheme(R.style.Yellow)
        }

        super.onCreate(savedInstanceState)
        a = MainDsl().setContentView(this)
        //setContentView(R.layout.activity_main)
        val navigationView =
                a.find<NavigationView>(MainDsl.nav_view)

        navigationView.setNavigationItemSelectedListener(this)

        val login = prefs.getString("login", "")
        val pass = prefs.getString("pass", "")
        User().login(login, pass, object : CallbackLoginUser {
            override fun onLoginSucceed(responseLogin: ResponseLogin) {
                toast("Зашел ${responseLogin.result.userInfo.id}")
            }

            override fun onLoginFailed(errorCode: String, errorMessage: String) {
                startActivityForResult<LoginActivity>(1)
            }
        })

        //if (!prefs.getBoolean("authorization", true)) {
         //   startActivityForResult<LoginActivity>(1)
        //}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            1 -> {  }
            2 -> { finish() }
        }
    }

    override fun onStart() {
        super.onStart()

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        if (!MyService.isNotificationAccessEnabled(this)) {
            AlertDialog.Builder(this)
                    .setPositiveButton(
                            android.R.string.ok
                    ) { _, _ ->
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

        val drawer = //drawer_layout
                a.find<DrawerLayout>(MainDsl.drawer_layout)

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
                prefs.getBoolean("authorization", false)
                prefs.edit().putBoolean("authorization", false).apply()
                LoginActivity.logout(this)
                return super.onOptionsItemSelected(item)
            }
            else -> {
                FindFragment()
            }
        }
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, fr)
                .addToBackStack("a")
                .commit()
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        val drawer = //drawer_layout
                a.find<DrawerLayout>(MainDsl.drawer_layout)

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
                startActivity<SettingActivity>()//fr = SettingActivity.SettingsFragment()
                return true
            }
            R.id.find -> {
                fr = FindFragment()
            }
            R.id.nav_send -> {
                startActivity<Add>()
                return true
            }
            R.id.browser -> {
                startActivity<BrowseActivity>()
                return true
            }
            R.id.history -> {
                startActivity<HistoryActivity>()
                return true
            }
            else -> {
                fr = FindFragment()
            }
        }

        val fragmentManager = supportFragmentManager
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

        var collectionType: Enum<Nav> = Nav.Music
    }
}

