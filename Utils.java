package com.example.loginsignup;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class Utils {
    public static final String TAG = "FCMTOKEN";

    @TargetApi(Build.VERSION_CODES.O)
    public static void showNotification(Context context, String title, String body){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"edmt.dev.channel");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(Notification.PRIORITY_MAX);

        //style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(body);
        bigTextStyle.setBigContentTitle("HIGH_ALERT");
        bigTextStyle.setSummaryText(title);

        builder.setStyle(bigTextStyle);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "edmt.dev.channel.id";
           NotificationChannel channel = new NotificationChannel(channelId,"EDMTDEV Channel" ,
                   NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
            manager.notify(new Random().nextInt(),builder.build());
        }

        }



}
