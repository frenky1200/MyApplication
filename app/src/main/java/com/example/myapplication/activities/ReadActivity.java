package com.example.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.control.DBController;
import com.example.myapplication.data.entity.Media;
import com.example.myapplication.data.interfaces.IMediable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadActivity extends AppCompatActivity {

    private DBController c;
    @BindView(R.id.textView2) TextView textView2;
    @BindView(R.id.buttonupd) Button button;
    @BindView(R.id.buttonplay) Button button2;
    String s = "";
    protected Integer idInt;
    Media media;
    ArrayAdapter<String> adapter;

    void Init(){
        c = new DBController(this);
        idInt = getIntent().getIntExtra("id",1);
        media = c.getMedia(idInt);
    }

    private <T extends IMediable> void readInf(T media, int id){
        media = c.getMedia1(media, id);
        s = media.getName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_present);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Init();

        setTitle(media.getName());
        readInf(media.mediaType(),media.getId());
        textView2.setText(s);
    }

    @OnClick(R.id.buttonplay)
    void OnPlayClick(){
        if (media.getInsideUri() == null && media.getOutsideUri() == null) {
            Toast.makeText(this,"Вы не сохранили источник",Toast.LENGTH_SHORT).show();
        }else{
            if (media.getInsideUri()!=null){
                Intent playAudioIntent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(media.getInsideUri());
                uri.getEncodedSchemeSpecificPart();
                playAudioIntent.setDataAndType(Uri.parse(media.getInsideUri()), "audio/*");

                startActivity(playAudioIntent);
            }else{
                Intent playAudioIntent = new Intent(Intent.ACTION_VIEW);
                playAudioIntent.setDataAndType(Uri.parse(media.getOutsideUri()), "audio/*");
                startActivity(playAudioIntent);
            }
        }
    }

    @OnClick(R.id.buttonupd)
    void OnClick(){
        Intent intent = new Intent(ReadActivity.this, AddActivity.class);
        intent.putExtra("id",media.getId());
        startActivity(intent);
    }
}
