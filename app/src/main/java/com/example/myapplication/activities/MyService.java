package com.example.myapplication.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyService extends NotificationListenerService
        implements MediaSessionManager.OnActiveSessionsChangedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static List<MediaController> mediaControllers = new ArrayList<>();
    private Map<MediaController, MediaController.Callback> controllerCallbacks = new WeakHashMap<>();
    private SharedPreferences sharedPreferences;
    private String str = "";
    MyBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        MediaSessionManager mediaSessionManager =
                (MediaSessionManager)
                        getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);

        ComponentName componentName = new ComponentName(this, this.getClass());
        mediaSessionManager.addOnActiveSessionsChangedListener(this, componentName);

        // Trigger change event with existing set of sessions.
        List<MediaController> initialSessions = mediaSessionManager.getActiveSessions(componentName);
        onActiveSessionsChanged(initialSessions);
    }

    @Override
    public void onActiveSessionsChanged(@Nullable List<MediaController> activeMediaControllers) {

        for (MediaController controller : mediaControllers) {
            if(controllerCallbacks.size()>0){
                controller.unregisterCallback(controllerCallbacks.get(controller));
                controllerCallbacks.remove(controller);
            }
        }
        controllerCallbacks.clear();

        for (final MediaController controller : activeMediaControllers) {
            String packageName = controller.getPackageName();

            MediaController.Callback callback =
                    new MediaController.Callback() {
                        @Override
                        public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                            controllerPlaybackStateChanged(controller, state);
                        }

                        @Override
                        public void onMetadataChanged(MediaMetadata metadata) {
                            controllerMetadataChanged(controller, metadata);
                        }
                    };

            controllerCallbacks.put(controller, callback);
            controller.registerCallback(callback);

            // Media may already be playing - update with current state.

            controllerPlaybackStateChanged(controller, controller.getPlaybackState());
            controllerMetadataChanged(controller, controller.getMetadata());
        }

        mediaControllers = activeMediaControllers;
    }

    public static boolean isNotificationAccessEnabled(Context context) {
        return NotificationManagerCompat.getEnabledListenerPackages(context)
                .contains(context.getPackageName());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    private void controllerPlaybackStateChanged(MediaController controller, PlaybackState state) {
        if (state!=null && state.getState() == PlaybackState.STATE_PAUSED){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }
    }

    private static PlayedActivity act;

    private void controllerMetadataChanged(MediaController controller, MediaMetadata metadata) {
        if(controller.getMetadata()!=null)
            str = controller.getMetadata().getString(MediaMetadata.METADATA_KEY_TITLE);
        Intent intent = new Intent(this, PlayedActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder notification =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_menu_camera)
                        .setContentTitle("Now playing")
                        .setContentText(str)
                        .setOngoing(true)
                        .setCategory(Notification.CATEGORY_STATUS)
                        .setColor(Color.argb(255, 242, 72, 63))
        .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
        if(metadata!=null && act!=null){
            act.doIt(metadata);
        }
    }

    public List<MediaController> getMediaControllers(){
        return mediaControllers;
    }

    public void setAct(PlayedActivity a){
        act = a;
    }

    class MyBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }
}