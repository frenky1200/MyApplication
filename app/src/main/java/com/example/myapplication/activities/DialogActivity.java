package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.data.Media;
import com.example.myapplication.data.control.DBController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends AppCompatActivity {
    String a;
    Integer b;
    DBController c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        c = new DBController(this);
        a = getIntent().getStringExtra("name");
        b = getIntent().getIntExtra("id",1);
    }

    @BindView(R.id.button) Button update;
    @BindView(R.id.button2) Button delete;

    @OnClick(R.id.button)
    public void OnUpdClick(){
        Intent intent = new Intent(DialogActivity.this,ReadActivity.class);
        intent.putExtra("name",a);
        intent.putExtra("id",b);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button2)
    public void OnDelClick(){
        Media media = c.getMedia(b);
        c.deleteMedia(media);
        finish();
    }
}
