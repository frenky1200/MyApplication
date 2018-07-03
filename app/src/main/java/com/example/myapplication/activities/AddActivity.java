package com.example.myapplication.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.data.control.DBController;
import com.example.myapplication.data.entity.Media;
import com.example.myapplication.data.interfaces.IMediable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends AppCompatActivity {

    @BindView(R.id.buttonsave) Button button;
    @BindView(R.id.editText) EditText inside;
    @BindView(R.id.editText2) EditText outside;
    @BindView(R.id.editText3) EditText editText;
    @BindView(R.id.editText4) EditText editText2;
    @BindView(R.id.editText5) EditText editText3;
    private DBController c;
    protected Integer idInt;
    private Media media;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        switch (prefs.getString("colors","")){
            case "Серый":{setTheme(R.style.AppTheme);break;}
            case "Красный":{setTheme(R.style.Red);break;}
            case "Зеленый":{setTheme(R.style.Green);break;}
            case "Синий":{setTheme(R.style.Blue);break;}
            case "Желтый":{setTheme(R.style.Yellow);break;}
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_edit);
        ButterKnife.bind(this);

        c = new DBController(this);
        idInt = getIntent().getIntExtra("id",-1);
        media = c.getMedia(idInt);
        readInf(media.mediaType(),media.getId());
        editText.setText(media.getName());
        editText2.setText(media.getTags());
        inside.setText(media.getInsideUri());
        outside.setText(media.getOutsideUri());
        setTitle(media.getName());
    }

    private <T extends IMediable> void readInf(T media, int id){
        media = c.getMedia1(media, id);
        editText3.setText(media.getName());
    }

    @OnClick(R.id.buttonsave)
    public void OnSaveClick(){
        media.setName(editText.getText().toString());
        media.setTags(editText2.getText().toString());
        media.setInsideUri(inside.getText().toString());
        media.setOutsideUri(outside.getText().toString());
        c.updateBook(media);

        IMediable m;
        m = c.getMedia1(media.mediaType(),media.getId());
        m.setName(editText3.getText().toString());
        c.updateMedia(m);
        finish();
    }
}
