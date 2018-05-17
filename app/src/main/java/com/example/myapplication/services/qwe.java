package com.example.myapplication.services;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ImageView;


public class qwe extends Service {

    private static final String IMAGE_URL = "https://yandex.by/images/search?text=%D0%BA%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B8&img_url=https%3A%2F%2Ffullhdpictures.com%2Fwp-content%2Fuploads%2F2016%2F04%2FDubai-HD-Wallpaper.jpg&pos=0&rpt=simage";

    ImageView imageView;
    DownloadManager downloadManager;

    @Override
    public IBinder onBind(Intent intent) {

        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //setContentView(R.layout.activity_main);

        //imageView = (ImageView)findViewById(R.id.imageView);

        //Получаем ссылку на DownloadManager сервис
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);


        //Создаем новый запрос
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(IMAGE_URL));
        request.setTitle("Title.jpg"); //заголовок будущей нотификации
        request.setDescription("My description"); //описание будущей нотификации
        request.setMimeType("image"); //mine type загружаемого файла

        //Установите следующий флаг, если хотите,
        //что-бы уведомление осталось по окончании загрузки
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Добавляем запрос в очередь
        downloadManager.enqueue(request);

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
        //unregisterReceiver(receiver);
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
                Cursor cursor = dm.query(query);
                if (cursor.moveToFirst()){
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                        String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        //imageView.setImageURI(Uri.parse(uriString));
                    }
                }
                //Сообщение о клике по нотификации
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)){
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                //несколько параллельных загрузок могут быть объеденены в одну нотификацию,
                //по этому мы пытаемся получить список всех загрузок, связанных с
                //выбранной нотификацией
                long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(ids);
                Cursor cursor = dm.query(query);
                int idIndex = cursor.getColumnIndex(DownloadManager.COLUMN_ID);
                if (cursor.moveToFirst()){
                    do {
                        //здесь вы можете получить id загрузки и
                        //реализовать свое поведение
                        long downloadId = cursor.getLong(idIndex);

                    } while (cursor.moveToNext());
                }
            }
        }
    };
}
