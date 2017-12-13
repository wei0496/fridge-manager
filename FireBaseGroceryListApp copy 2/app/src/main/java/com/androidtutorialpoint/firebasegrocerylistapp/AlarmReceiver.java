package com.androidtutorialpoint.firebasegrocerylistapp;

/**
 * Created by Cornfieldfox on 11/25/17.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    int flag = 0;
    DatabaseReference mDB;
    DatabaseReference mListItemRef;
    private String Uid;
    private Date Curr_Date;
    public static final String TAG = "myLogs";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e(TAG, "Received");

        //check if any of our food are expiring here
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Curr_Date = new Date();

        // see if the app is connected to the database
        try {
            // get all the food items of the logged in user
            Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDB = FirebaseDatabase.getInstance().getReference();
            mListItemRef = mDB.child("listItem").child(Uid);

            mListItemRef.addValueEventListener(new ValueEventListener() {
                @Override
                //iterate through all items to see if they expired
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        ListItem listItem = child.getValue(ListItem.class);
                        String ExpirationDuration = listItem.getExpirationDate();
                        String CreationDate = listItem.getListItemCreationDate();
                        String food = listItem.getName();
                        Log.e(TAG, CreationDate);
                        Calendar cal = Calendar.getInstance();
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        // set a date object to the item's expired date
                        try {
                            Date date = formatter.parse(CreationDate);
                            cal.set(Calendar.YEAR, date.getYear());
                            cal.set(Calendar.MONTH, date.getMonth());
                            cal.set(Calendar.DAY_OF_MONTH, date.getDate());
                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //set the expiration notification to be one day before the actual expiration
                        cal.add(Calendar.HOUR_OF_DAY, 24 * (Integer.parseInt(ExpirationDuration)-1));
                        cal.add(Calendar.YEAR, 1900);
                        Date temp = cal.getTime();
                        Log.e(TAG, "comparing...");
                        Log.e(TAG, formatter.format(Curr_Date));
                        Log.e(TAG, formatter.format(temp));

                        //if the food expired in one day, send the notification to the user!
                        if (temp.compareTo(Curr_Date) < 0) {
                            Log.e(TAG, "expired!");
                            flag = 1;
                            Intent notificationIntent = new Intent(context, MenuActivity.class);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                            stackBuilder.addParentStack(MenuActivity.class);
                            stackBuilder.addNextIntent(notificationIntent);

                            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                            // set the notification content
                            Notification notification = builder.setContentTitle("Magic Fridge Notification!!")
                                    .setContentText("Your " + food + " are expiring in one day! -- Fridge Manager!!")
                                    .setTicker("Your Food are expiring soon! -- Fridge Manager")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentIntent(pendingIntent).build();

                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0, notification);
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
    }
}

