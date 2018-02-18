package com.example.myapplication.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.com.example.myapplicationfragments.CollectionFragment;
import com.example.myapplication.com.example.myapplicationfragments.FindFragment;

import static com.example.myapplication.R.id.drawer_layout;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FindFragment youFragment = new FindFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()          // получаем экземпляр FragmentTransaction
                .replace(R.id.content_main, youFragment)
                .commit();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Bundle bundle = new Bundle();
        int id = item.getItemId();

        switch (id){
            case R.id.music:{
                collectionType = Nav.Music;
                bundle.putString("z", "Music");}
            case R.id.anime:{
                collectionType = Nav.Anime;
                bundle.putString("z", "Anime");}
            case R.id.excerption:{
                collectionType = Nav.Excerption;
                bundle.putString("z", "Film");}
            case R.id.book:{
                collectionType = Nav.Book;
                bundle.putString("z", "Book");}
            default:{}
        }

        CollectionFragment youFragment = new CollectionFragment();
        youFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()// получаем экземпляр FragmentTransaction
                .replace(R.id.content_main, youFragment)
                .addToBackStack("myStack")
                .commit();

        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Enum<Nav> collectionType = Nav.Start;

    public enum Nav{
        Start,
        Music,
        Book,
        Film,
        Excerption,
        Anime,
        Dorama
    }
}


