package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.R;
import com.example.myapplication.data.control.DBController;
import com.example.myapplication.data.entity.Media;
import com.example.myapplication.data.interfaces.IMediable;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadActivity extends AppCompatActivity {

    private DBController c;
    @BindView(R.id.buttonupd) Button button;
    @BindView(R.id.buttonplay) Button button2;
    @BindView(R.id.buttonsearch) Button button3;
    @BindView(R.id.scrollView2) ScrollView scrollView;
    @BindView(R.id.imageView2) ImageView imageView;
    @BindView(R.id.videoView) VideoView videoView;
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
        imageView.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setVisibility(View.VISIBLE);
            bmImage.setImageBitmap(result);
        }
    }

    private class DownloadVideoTask extends AsyncTask<String, Void, Void>{
        VideoView videoView;

        DownloadVideoTask(VideoView bmImage) {
            this.videoView = bmImage;
        }

        @Override
        protected Void doInBackground(String... strings) {
            videoView.setVideoURI(Uri.parse(strings[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            videoView.requestFocus(0);
            videoView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrollView.removeAllViews();
        videoView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Init();
        setTitle(media.getName());
        readInf(media.mediaType(),media.getId());
        switch (media.getType()){
            case "Music":{
                TextView textView2 = new TextView(this);
                textView2.setText(s);
                textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView2.setBackgroundResource(R.drawable.side_nav_bar);

                scrollView.addView(textView2);
                break;
            }
            case "Excerption":{
                TextView textView2 = new TextView(this);
                textView2.setText(s);
                textView2.setBackgroundResource(R.drawable.side_nav_bar);
                scrollView.addView(textView2);
                break;
            }
            case "Image":{
                new DownloadImageTask(imageView)
                        .execute(media.getInsideUri());
                break;
            }
            case "Film":{
                if(media.getInsideUri()!=null && !media.getInsideUri().equals(""))
                    new DownloadVideoTask(videoView)
                            .execute(media.getInsideUri());
                videoView.setMediaController(new MediaController(this));
                //scrollView.addView(videoView);
                break;
            }
            case "Anime":{
                if(media.getInsideUri()!=null && !media.getInsideUri().equals(""))
                    new DownloadVideoTask(videoView)
                            .execute(media.getInsideUri());
                videoView.setMediaController(new MediaController(this));
                //scrollView.addView(videoView);
                break;
            }
        }

    }

    @OnClick(R.id.buttonsearch)
    void brouse(){
        Intent ii = new Intent(this, BrowseActivity.class);
        ii.putExtra("search", "yandsearch?text=" + c.getAlbum(media.getId()) + " " + media.getName());
        startActivity(ii);
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
            if (media.getInsideUri()!=null && !media.getInsideUri().equals("")){
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
