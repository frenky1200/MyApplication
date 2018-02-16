package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.data.Anime;
import com.example.myapplication.data.Book;
import com.example.myapplication.data.Film;
import com.example.myapplication.data.Media;
import com.example.myapplication.data.Music;
import com.example.myapplication.data.control.DBController;
import com.example.myapplication.data.interfaces.IMediable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.buttonsave) Button button;
    @BindView(R.id.editText3) EditText editText;
    @BindView(R.id.editText4) EditText editText2;
    @BindView(R.id.editText5) EditText editText3;
    private DBController c;
    protected Integer idInt;
    protected String nameString;
    private Media media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_edit);
        ButterKnife.bind(this);

        c = new DBController(this);
        nameString = getIntent().getStringExtra("name");
        idInt = getIntent().getIntExtra("id",1);
        media = c.getMedia(idInt);
        editText.setText(media.getName());
        editText2.setText(media.getTags());
        setTitle(media.getName());

        readInf(media.mediaType(),media.getId());

        //if(media.getType().equals("Book")){
        //Book book = new Book();
        //book = c.getMedia(book, media.getId());
        //editText3.setText(book.getName());
        //}
        //
        //if(media.getType().equals("Music")){
        //Music music = new Music();
        //music = c.getMedia(music, media.getId());
        //editText3.setText(music.getName());
        //}
        //
        //if(media.getType().equals("Anime")){
        //Anime anime= new Anime();
        //anime = c.getMedia(anime, media.getId());
        //editText3.setText(anime.getName());
        //}
        //
        //if(media.getType().equals("Film")){
        //Film film = new Film();
        //film = c.getMedia(film, media.getId());
        //editText3.setText(film.getName());
        //}
    }

    private <T extends IMediable> void readInf(T media, int id){
        media = c.getMedia1(media, id);
        editText3.setText(media.getName());
    }

    @OnClick(R.id.buttonsave)
    public void OnSaveClick(){
        media.setName(editText.getText().toString());
        media.setTags(editText2.getText().toString());
        c.updateBook(media);

        //if(media.getType().equals("Book")){

        //Book book = new Book();

        //book = c.getMedia(book, media.getId());

        //book.setTitle(editText3.getText().toString());

        //c.updateBook(book);

        //}

        //

        //if(media.getType().equals("Music")){

        //Music music = new Music();

        //music = c.getMedia(music, media.getId());

        //music.setName(editText3.getText().toString());

        //c.updateBook(music);

        //}

        //

        //if(media.getType().equals("Anime")){

        //Anime anime= new Anime();

        //anime = c.getMedia(anime, media.getId());

        //anime.setName(editText3.getText().toString());

        //c.updateBook(anime);

        //}

        //

        //if(media.getType().equals("Film")){

        //Film music = new Film();

        //music = c.getMedia(music, media.getId());

        //music.setName( editText3.getText().toString());

        //c.updateBook(music);

        //}

        IMediable m;
        m = c.getMedia1(media.mediaType(),media.getId());
        m.setName(editText3.getText().toString());
        c.updateMedia(m);

        Intent intent = new Intent(AddActivity.this, ReadActivity.class);
        intent.putExtra("name",media.getName());
        intent.putExtra("id",media.getId());
        ReadActivity.getActivity().finish();
        startActivity(intent);
        finish();
    }
}
