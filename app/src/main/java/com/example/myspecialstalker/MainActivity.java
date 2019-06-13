package com.example.myspecialstalker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String MESSAGE = "message";

    private String phoneNumber;
    private String message;
    private EditText editText_phone;
    private EditText editText_message;
    private TextView textView;
    BroadcastReceiver br = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_phone = findViewById(R.id.field1_result);
        editText_message = findViewById(R.id.field2_result);
        textView = findViewById(R.id.ready_text);
        
        onRequestPermissionResult();
        loadData();
        listenForChangeInField(editText_phone,PHONE_NUMBER); // check for changes in phone number
        listenForChangeInField(editText_message, MESSAGE); // check for changes in message
        checkIfAppIsReady(); // app is ready when both fields are filled in
        InitBroadcastReceiver();
        sendMessage();
    }

    private void InitBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(br, filter, Manifest.permission.PROCESS_OUTGOING_CALLS,null);
    }

    private void checkIfAppIsReady() {
        if(!message.equals("") && !phoneNumber.equals("")){
            textView.setText("The app is ready to send SMS messages");
        }
    }

    private void listenForChangeInField(final EditText editText, final String key) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    textView.setText("Whoops, you are missing some fields");
                    saveData(key, "");
                } else if (!TextUtils.isEmpty(editText.getText().toString())) {
                    String text = editText.getText().toString();
                    saveData(key, text);
                    checkIfAppIsReady();

                }
            }
        });
    }

    private void saveData(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString(PHONE_NUMBER, "");
        message = sharedPreferences.getString(MESSAGE, "");
        editText_phone.setText(phoneNumber);
        editText_message.setText(message);
    }

    public void sendMessage(){
        String number = getIntent().getStringExtra("Phone number");
        if(number != null){
            Log.d("TAG", "tel:" + number);
            String message = editText_message.getText().toString();
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent("message sent"), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(editText_phone.getText().toString(), null, message+" "+number, pi, null);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    public void onRequestPermissionResult() {
        int PERMISSION_ALL = 1;

        String[] permissions = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.SEND_SMS};

        if (!checkPermissionsHelper(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            }
        }
    }


    public boolean checkPermissionsHelper(String... permissions){
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
