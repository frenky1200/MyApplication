package com.example.myapplication.com.example.myapplicationfragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.Album;
import com.example.myapplication.data.control.DBController;
import com.example.myapplication.data.helpers.AlbumHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class Colllect extends Fragment {
    @BindView(R.id.ListView)
    ListView listView;
    @BindView(R.id.imageButton)
    ImageButton button;
    @BindView(R.id.editColl)
    EditText editText;
    protected String b;
    protected DBController c;
    protected List<Album> list;
    protected Animation anim;
    protected ArrayAdapter<Album> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_collection,  container, false);
        Bundle bundle = getArguments();
        b = bundle.getString("z");
        getActivity().setTitle(b);
        c = new DBController(getActivity());
        list = c.getAllAlbum(b);
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.second);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.startAnimation(anim);

        return view;
    }

    @OnClick(R.id.imageButton)
    void OnAddClick(){
        String newColl = editText.getText().toString();
        c.addAlbum(newColl, b);
        list.clear();
        list.addAll(c.getAllAlbum(b));
        adapter.notifyDataSetChanged();
    }

    @OnItemLongClick(R.id.ListView)
    boolean OnListLongClick(AdapterView<?> parent, View view, int position, long id){
        Album album = (Album)parent.getItemAtPosition(position);
        c.deleteAlbum(album);
        List<Album> list = c.getAllAlbum(b);
        ArrayAdapter<Album> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        return false;
    }

    @OnItemClick(R.id.ListView)
    void OnListClick(AdapterView <?> parent, View itemClicked, int position, long id){
        Album album = (Album)parent.getItemAtPosition(position);
        Bundle bundle = new Bundle();
        bundle.putString("album",album.getName());
        bundle.putString("type",album.getType());
        MediaFragment myFragment = new MediaFragment();
        myFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, myFragment)
                .addToBackStack("myStack")
                .commit();
    }
}
