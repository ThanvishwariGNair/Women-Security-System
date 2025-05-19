package com.example.loginsignup;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {

        super.onNewToken(token);
        Log.d(Utils.TAG,token);

    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification().getTitle();
        String content = message.getNotification().getBody();
        String data = new Gson().toJson(message.getData());
        Utils.showNotification(this,title,content);

        Log.d(Utils.TAG,data);

    }
}
