package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static com.example.myspecialstalker.MainActivity.CHANEL_ID;

public class MyBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                    .setSmallIcon(R.drawable.paper_plane)
                    .setContentTitle("Message Status")
                    .setContentText("Sending...")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(2393, builder.build());

            Intent i = new Intent(context, MainActivity.class);
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            i.putExtra("Phone number", number);
            context.startActivity(i);


        }
    }


}

