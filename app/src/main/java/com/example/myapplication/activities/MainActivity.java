package com.example.myapplication.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.fragments.CollectionFragment;
import com.example.myapplication.fragments.FindFragment;
import com.example.myapplication.services.MyService;

import static com.example.myapplication.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch (prefs.getString("colors","")){
            case "Серый":{setTheme(R.style.AppTheme);break;}
            case "Красный":{setTheme(R.style.Red);break;}
            case "Зеленый":{setTheme(R.style.Green);break;}
            case "Синий":{setTheme(R.style.Blue);break;}
            case "Желтый":{setTheme(R.style.Yellow);break;}
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MyService.isNotificationAccessEnabled(this)) {
        new AlertDialog.Builder(this)
                .setPositiveButton(
                        android.R.string.ok,
                        (dialogInterface, i) -> {
                            String action;
                            if (Build.VERSION.SDK_INT >= 22) {
                                action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
                            } else {
                                action = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
                            }
                            startActivity(new Intent(action));
                        })
                .show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }

        startService(new Intent(this, MyService.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fr;
        switch (id){
            case R.id.action_settings: {
                fr = new SettingActivity.SettingsFragment();
                break;
            }case R.id.action_quit: {
                SharedPreferences pref = getSharedPreferences("activities.LoginActivity",MODE_PRIVATE);
                pref.getBoolean("authorization", false);
                pref.edit().putBoolean("authorization", false).apply();
                LoginActivity.logout(this);
                return true;
            }default:{
                fr = new FindFragment();}
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()// получаем экземпляр FragmentTransaction
                .replace(R.id.content_main, fr)
                .addToBackStack("a")
                .commit();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Fragment fr;
        switch (id){
            case R.id.music:{
                collectionType = Nav.Music;
                fr = new CollectionFragment();
                break;
            }case R.id.anime:{
                collectionType = Nav.Anime;
                fr = new CollectionFragment();
                break;
            }case R.id.excerption:{
                collectionType = Nav.Excerption;
                fr = new CollectionFragment();
                break;
            }case R.id.film:{
                collectionType = Nav.Film;
                fr = new CollectionFragment();
                break;
            }case R.id.image:{
                collectionType = Nav.Image;
                fr = new CollectionFragment();
                break;
            }case R.id.setting:{
                fr = new SettingActivity.SettingsFragment();
                break;
            }case R.id.find:{
                fr = new FindFragment();
                break;
            }case R.id.nav_send:{
                startActivity(new Intent(this, Add.class));
                return true;
            }case R.id.browser:{
                startActivity(new Intent(this, BrowseActivity.class));
                return true;
            }default:{
                fr = new FindFragment();}
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()// получаем экземпляр FragmentTransaction
                .replace(R.id.content_main, fr)
                .addToBackStack("a")
                .commit();
        return true;
    }

    public static void display(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static Enum<Nav> collectionType = Nav.Music;

    public enum Nav{

        Music(R.string.music),
        Book(R.string.book),
        Film(R.string.film),
        Excerption(R.string.excerption),
        Anime(R.string.anime),
        Image(R.string.image);

        private int mResourceId;

        public final String value(Context App){
            return App.getString(mResourceId);
        }

        Nav(int id) {
            mResourceId = id;
        }

    }
}

