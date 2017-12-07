package com.androidtutorialpoint.firebasegrocerylistapp;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;


import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by m804 on 24/11/2017.
 */

public class ExpirationActivity extends Activity {

    private Date Curr_Date;
    public static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 Curr_Date = new Date();
        //setContentView(R.layout.activity_main);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(ExpirationActivity.this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to start at 10:00 AM
        // change the time below to set a different start time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 0);

        // The below code is for demo, triggered every 30 seconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3000, broadcast);
        Log.e(TAG, "Expires");

        Intent intent = new Intent(ExpirationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
