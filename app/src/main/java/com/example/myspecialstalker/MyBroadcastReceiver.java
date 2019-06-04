package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Toast.makeText(context, "tel:" + number, Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("Phone number", number);
            context.startActivity(i);
        }
    }

    }

