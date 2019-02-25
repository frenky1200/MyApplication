package com.example.myapplication.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.MyApp.Companion.hist
import com.example.myapplication.R
import com.example.myapplication.activities.PlayedActivity
import java.util.*

class MyService: NotificationListenerService(), MediaSessionManager.OnActiveSessionsChangedListener {
    private val controllerCallbacks = WeakHashMap<MediaController, MediaController.Callback>()
    private var binder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {

        return binder
    }

    override fun onCreate() {
        super.onCreate()

        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val mediaSessionManager = applicationContext.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

        val componentName = ComponentName(this, this.javaClass)
        val initialSessions: List<MediaController>?

        mediaSessionManager.addOnActiveSessionsChangedListener(this, componentName)
        initialSessions = mediaSessionManager.getActiveSessions(componentName)

        onActiveSessionsChanged(initialSessions)

    }

    override fun onActiveSessionsChanged(activeMediaControllers: List<MediaController>?) {

        for (controller in mediaControllers) {
            if (controllerCallbacks.size > 0) {
                controller.unregisterCallback(Objects.requireNonNull<MediaController.Callback>(controllerCallbacks[controller]))
                controllerCallbacks.remove(controller)
            }
        }
        controllerCallbacks.clear()

        if (activeMediaControllers != null) {
            for (controller in activeMediaControllers) {
                //String packageName = controller.getPackageName();

                val callback = object : MediaController.Callback() {
                    override fun onPlaybackStateChanged(state: PlaybackState?) {
                        controllerPlaybackStateChanged(controller, state)
                    }

                    override fun onMetadataChanged(metadata: MediaMetadata?) {
                        controllerMetadataChanged(controller, metadata)
                    }
                }

                controllerCallbacks[controller] = callback
                controller.registerCallback(callback)

                //controllerPlaybackStateChanged(controller, controller.playbackState)
                //controllerMetadataChanged(controller, controller.metadata)
            }
        }

        mediaControllers = activeMediaControllers!!
    }

    private fun controllerPlaybackStateChanged(controller: MediaController, state: PlaybackState?) {
        if (state != null && state.state != PlaybackState.STATE_PLAYING) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(0)
        }
    }

    private fun controllerMetadataChanged(controller: MediaController, metadata: MediaMetadata?) {

        notify(controller)
        if (metadata != null) {
            hist.add(name = metadata.getString(MediaMetadata.METADATA_KEY_TITLE),
                    album = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST))
            act?.doIt(metadata)
        }
    }

    fun notify(controller: MediaController) {
        if (controller.metadata != null && controller.playbackState != null)
            if (controller.playbackState!!.state == PlaybackState.STATE_PLAYING) {
                val str = controller.metadata!!.getString(MediaMetadata.METADATA_KEY_TITLE)

                val intent = Intent(this, PlayedActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                val notification = Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("Now playing")
                        .setContentText(str)
                        .setOngoing(true)
                        .setCategory(Notification.CATEGORY_STATUS)
                        .setColor(Color.argb(125, 242, 72, 63))
                        .setContentIntent(pendingIntent)
                val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(0, notification.build())
            }
    }

    fun getMediaControllers(): List<MediaController> {
        return mediaControllers
    }

    fun setAct(a: PlayedActivity?) {
        act = a
    }

    inner class MyBinder: Binder() {
        val service: MyService
            get() = this@MyService
    }

    companion object {

        var mediaControllers: List<MediaController> = ArrayList()

        fun isNotificationAccessEnabled(context: Context): Boolean {
            return NotificationManagerCompat.getEnabledListenerPackages(context)
                    .contains(context.packageName)
        }

        private var act: PlayedActivity? = null
    }
}
