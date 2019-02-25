package com.example.myapplication.broadcast

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.activities.Add

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor = dm.query(query)
            if (cursor.moveToFirst()) {

                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val uriLocalString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))

                if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                    val intent1 = Intent(context, Add::class.java)
                    intent1.data = Uri.parse(uriLocalString)
                    intent1.putExtra("outsideUri", uriString)
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT)
                    val notification = Notification.Builder(context)
                            .setSmallIcon(R.drawable.ic_menu_send)
                            .setContentTitle("downloaded")
                            .setCategory(Notification.CATEGORY_STATUS)
                            .setOngoing(true)
                            .setColor(Color.argb(125, 242, 72, 63))
                            .setContentIntent(pendingIntent)
                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(3, notification.build())
                }
                cursor.close()

                Toast.makeText(context, "Файл Загружен в папку Download", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

