package com.example.myapplication.services;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.activities.Add;
import com.example.myapplication.activities.PlayedActivity;


public class qwe extends Service {

    @Override
    public IBinder onBind(Intent intent) {

        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Сообщение о том, что загрузка закончена
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                assert dm != null;
                Cursor cursor = dm.query(query);
                if (cursor.moveToFirst()){
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        String uriLocalString = cursor.getString(16);
                        String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                        Intent intent1 = new Intent(context, Add.class);
                        intent1.setData(Uri.parse(uriLocalString));
                        intent1.putExtra("outsideUri", uriString);
                        PendingIntent pendingIntent =
                                PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
                        Notification.Builder notification =
                                new Notification.Builder(context)
                                        .setSmallIcon(R.drawable.ic_menu_camera)
                                        .setContentTitle("downloaded")
                                        .setCategory(Notification.CATEGORY_STATUS)
                                        .setOngoing(true)
                                        .setColor(Color.argb(255, 242, 72, 63))
                                        .setContentIntent(pendingIntent);
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (notificationManager != null) {
                            notificationManager.notify(3, notification.build());
                        }
                    }
                    cursor.close();
                }
            }
        }
    };
}
