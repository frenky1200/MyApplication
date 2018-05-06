package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.com.example.fragments.CollectionFragment;
import com.example.myapplication.com.example.fragments.FindFragment;

import static com.example.myapplication.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

        FindFragment youFragment = new FindFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()          // получаем экземпляр FragmentTransaction
                .replace(R.id.content_main, youFragment)
                .addToBackStack("a")
                .commit();

        startService(new Intent(this, MyService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MyService.isNotificationAccessEnabled(this)) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
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
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.music:{
                collectionType = Nav.Music;
                break;}
            case R.id.anime:{
                collectionType = Nav.Anime;
                break;}
            case R.id.excerption:{
                collectionType = Nav.Excerption;
                break;}
            case R.id.book:{
                collectionType = Nav.Book;
                break;}
            default:{}
        }

        CollectionFragment youFragment = new CollectionFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()// получаем экземпляр FragmentTransaction
                .replace(R.id.content_main, youFragment)
                .addToBackStack("a")
                .commit();

        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Enum<Nav> collectionType = Nav.Music;

    public enum Nav{
        Music,
        Book,
        Film,
        Excerption,
        Anime,
        Dorama
    }
}


