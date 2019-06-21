package com.example.myspecialstalker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.util.Log;
import static android.content.ContentValues.TAG;
import static com.example.myspecialstalker.MainActivity.CHANEL_ID;
import static com.example.myspecialstalker.MainActivity.DELIVERED;
import static com.example.myspecialstalker.MainActivity.SENT;

public class MyBroadcastReceiver extends BroadcastReceiver {
    String myNumber;
    String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

            initChannels(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                    .setSmallIcon(R.drawable.paper_plane)
                    .setContentTitle("Message Status")
                    .setContentText("Sending...")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(2393, builder.build());
            
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            sendMessage(number, context);


        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANEL_ID,
                "Channel name",
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Message status");
        notificationManager.createNotificationChannel(channel);
    }

    public void sendMessage(String number, Context context) {
        Log.d(TAG, "myNumber:"+myNumber+" Message: "+message);
        Intent sentIntent = new Intent(context, MyService.class);
        sentIntent.setAction(SENT);
        PendingIntent sentPI = PendingIntent.getService(context, 0, sentIntent, 0);

        Intent deliverIntent = new Intent(context, MyService.class);
        sentIntent.setAction(DELIVERED);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
        if(myNumber != null && message !=null){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(myNumber, null, message+" "+number , sentPI, deliveredPI);
        }

    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setMyNumber(String myNumber) {
        this.myNumber = myNumber;
    }
}

