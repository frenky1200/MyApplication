package com.example.myapplication.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.ReadActivity;
import com.example.myapplication.data.control.DBController;
import com.example.myapplication.data.entity.Media;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExcerptionAdapter extends BaseAdapter{
    private DBController c;
    private List<Media> medias;
    private Context context;
    private int layoutId;
    private LayoutInflater inflater;

    public ExcerptionAdapter(Context context, int layoutId, List<Media> medias){
        this.medias = medias;
        this.context = context;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return medias.size();
    }

    @Override
    public Object getItem(int i) {
        return medias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null){
            holder = (ViewHolder) view.getTag();
        }
        else{
            view = inflater.inflate(layoutId, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        customizeView(view, holder, medias.get(position));

        return view;
    }


    private void customizeView(View view, ViewHolder holder, final Media media) {
        String Name = media.getName();
        String Album = media.getAlbum();

        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("title");  // заголовок
        ad.setMessage("Delete?"); // сообщение
        ad.setPositiveButton("Yes", (dialog, arg1) -> {
            c = new DBController((Activity) context);
            c.deleteMedia(media);
            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
            medias.remove(media);
            notifyDataSetChanged();
        });
        ad.setNegativeButton("No", (dialog, arg1) -> Toast.makeText(context, "Not deleted", Toast.LENGTH_LONG).show());

        holder.textView3.setText(Name);
        holder.textView5.setText(Album);
        holder.imageDelete.setOnClickListener(view1 -> ad.show());
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadActivity.class);
            intent.putExtra("id", media.getId());
            context.startActivity(intent);
        });
    }
    static class ViewHolder {
        @BindView(R.id.textView3) TextView textView3;
        @BindView(R.id.textView5) TextView textView5;
        @BindView(R.id.imageDelete) ImageButton imageDelete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
