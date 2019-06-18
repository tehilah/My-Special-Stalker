package com.example.myspecialstalker;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import static com.example.myspecialstalker.MainActivity.CHANEL_ID;
import static com.example.myspecialstalker.MainActivity.DELIVERED;
import static com.example.myspecialstalker.MainActivity.SENT;

public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();

        if(action != null && action.equals(SENT)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_ID)
                    .setSmallIcon(R.drawable.paper_plane)
                    .setContentTitle("Message Status")
                    .setContentText(SENT)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(2393, builder.build());
        }
        if(action != null && action.equals(DELIVERED)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_ID)
                    .setSmallIcon(R.drawable.paper_plane)
                    .setContentTitle("Message Status")
                    .setContentText(DELIVERED)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(2393, builder.build());
        }

    }
}
