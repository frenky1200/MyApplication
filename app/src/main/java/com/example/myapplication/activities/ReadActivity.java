package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        switch (prefs.getString("colors","Зеленый")){
            case "Серый":{setTheme(R.style.AppTheme);break;}
            case "Красный":{setTheme(R.style.Red);break;}
            case "Зеленый":{setTheme(R.style.Green);break;}
            case "Синий":{setTheme(R.style.Blue);break;}
            case "Желтый":{setTheme(R.style.Yellow);break;}
        }
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
        String type ;

        switch (media.getType()){
            case "Music":{ type = "audio/*" ; break;}
            case "Image":{ type = "image/*" ; break;}
            case "Film":{ type = "video/*" ; break;}
            case "Excerption":{ type = "text/*" ; break;}
            default:{ type = "image/*";}
        }

        if ((media.getInsideUri() == null||media.getInsideUri().equals("") )&&
                ( media.getOutsideUri().equals("")||media.getOutsideUri() == null)) {
            Toast.makeText(this,"Вы не сохранили источник",Toast.LENGTH_SHORT).show();
        }
        else{
            if (media.getInsideUri()!=null){
                Intent playAudioIntent = new Intent(Intent.ACTION_VIEW);
                playAudioIntent.setDataAndType(Uri.parse(media.getInsideUri()), type);
                startActivity(playAudioIntent);
            }
            else{
                Intent playAudioIntent = new Intent(Intent.ACTION_VIEW);
                playAudioIntent.setDataAndType(Uri.parse(media.getOutsideUri()), type);
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
