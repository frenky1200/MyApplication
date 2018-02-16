package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.data.Anime;
import com.example.myapplication.data.Book;
import com.example.myapplication.data.Film;
import com.example.myapplication.data.interfaces.IHelper;
import com.example.myapplication.data.interfaces.IMediable;
import com.example.myapplication.data.Media;
import com.example.myapplication.data.Music;
import com.example.myapplication.data.control.DBController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadActivity extends AppCompatActivity {

    static ReadActivity a1;
    private DBController c;
    @BindView(R.id.listviewadd) ListView list;
    @BindView(R.id.textViewname) TextView textView;
    @BindView(R.id.buttonupd) Button button;
    String[] s = {""};
    protected String nameString;
    protected Integer idInt;
    Media media;
    ArrayAdapter<String> adapter;

    void Init(){
        c = new DBController(this);
        nameString = getIntent().getStringExtra("name");
        idInt = getIntent().getIntExtra("id",1);
        media = c.getMedia(idInt);
        a1 = this;
    }

    private <T extends IMediable> void readInf(T media,int id){
        media = c.getMedia1(media, id);
        s[0] = media.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_present);
        ButterKnife.bind(this);
        Init();

        textView.setText(media.getName());
        setTitle(media.getName());
        readInf(media.mediaType(),media.getId());

        //

        //if(media.getType().equals("Book")){

        //Book book = new Book();

        //book = c.getMedia(book, media.getId());

        //s[0] = book.getName();

        //}

        //

        //if(media.getType().equals("Music")){

        //Music music = new Music();

        //music = c.getMedia(music, media.getId());

        //s[0] = music.getName();

        //}

        //

        //if(media.getType().equals("Anime")){

        //Anime anime = new Anime();

        //anime = c.getMedia(anime, media.getId());

        //s[0] = anime.getName();

        //}

        //

        //if(media.getType().equals("Film")){

        //Film music = new Film();

        //music = c.getMedia(music, media.getId());

        //s[0] = music.getName();

        //}

        //

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, s);
        list.setAdapter(adapter);
    }

    @OnClick(R.id.buttonupd)
    void OnClick(){
        Intent intent = new Intent(ReadActivity.this, AddActivity.class);
        intent.putExtra("name",media.getName());
        intent.putExtra("id",media.getId());
        startActivity(intent);
    }

    public static ReadActivity getActivity(){
        return a1;
    }
}
