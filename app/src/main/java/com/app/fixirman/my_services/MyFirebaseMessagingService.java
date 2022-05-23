package com.app.fixirman.my_services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.fixirman.R;
import com.app.fixirman.utils.SessionManager;
import com.app.fixirman.view.activity.MainActivity;
import com.app.fixirman.view.activity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    SessionManager sessionManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("FirebaseMessaging", "From: " + remoteMessage.getFrom());
        sessionManager = new SessionManager(getApplicationContext());
        generateNotification(remoteMessage);
    }

    void generateNotification(RemoteMessage remoteMessage) {
        String title;
        String message;
        try {
             title = remoteMessage.getNotification().getTitle();
             message = remoteMessage.getNotification().getBody();
        }catch (NullPointerException e)
        {
            title = "New Notification";
            message = "Tap to see the detail";
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, SplashActivity.class);
        resultIntent.putExtra("key","notification");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("M_CH_ID", "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);

            notificationManager.createNotificationChannel(notificationChannel);
        }


        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this, "M_CH_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setAutoCancel(true);

        mBuilder.setContentIntent(resultPendingIntent);
        if (notificationManager != null) {
            notificationManager.notify(45, mBuilder.build());
        }
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.saveToken(token);
    }
}