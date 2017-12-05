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

    boolean flag = false;
    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    private String Uid;
    private Date Curr_Date;
    public static final String TAG = "myLogs";
    final Calendar cal = Calendar.getInstance();
    final Calendar cal1 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Curr_Date = new Date();
        //setContentView(R.layout.activity_main);
        try {
            Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mDB = FirebaseDatabase.getInstance().getReference();
            mListItemRef = mDB.child("listItem").child(Uid);

            mListItemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        ListItem listItem = child.getValue(ListItem.class);
                        String ExpirationDuration = listItem.getExpirationDate();
                        String CreationDate = listItem.getListItemCreationDate();
                        Log.e(TAG, CreationDate);
                        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        try{Date date = formatter.parse(CreationDate);
                            cal.setTime(date);}
                        catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        cal.add(Calendar.HOUR_OF_DAY, 24 * Integer.parseInt(ExpirationDuration));
                        Date temp = cal.getTime();
                        if (temp.compareTo(Curr_Date) > 0){
                            flag = true;
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                            notificationIntent.addCategory("android.intent.category.DEFAULT");

                            PendingIntent broadcast = PendingIntent.getBroadcast(ExpirationActivity.this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            // Set the alarm to start at 10:00 AM
                            // change the time below to set a different start time
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 10);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);

                            // The below code is for demo, triggered every 30 seconds
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3000, broadcast);

                            // comment the above code and uncomment the below code for actual use, triggered every 24 hours
                            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, broadcast);

                            //Calendar cal1 = Calendar.getInstance();
                            //For demo we set this to be called every 15 seconds
                            //cal1.add(Calendar.SECOND, 15);
                            //alarmManager.set(AlarmManager.RTC_WAKEUP, cal1.getTimeInMillis(), broadcast);
                            Log.e(TAG, "Expires");
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }

        catch (Exception e){}
        Intent intent = new Intent(ExpirationActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
