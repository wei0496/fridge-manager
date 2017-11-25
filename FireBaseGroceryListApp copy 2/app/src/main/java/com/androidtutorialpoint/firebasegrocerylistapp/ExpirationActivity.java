package com.androidtutorialpoint.firebasegrocerylistapp;

/**
 * Created by Cornfieldfox on 11/25/17.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ExpirationActivity extends Activity {

    boolean flag = true;
    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    private String Uid;
    public static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDB= FirebaseDatabase.getInstance().getReference();
        mListItemRef = mDB.child("listItem").child(Uid);

        mListItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren() ) {
                    ListItem listItem=child.getValue(ListItem.class);
                    String ExpirationDuration = listItem.getExpirationDate();
                    String CreationDate = listItem.getListItemCreationDate();
                    Log.e(TAG,CreationDate);
                    flag = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });

        if (flag) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 15);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        }
        Intent intent = new Intent(ExpirationActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}