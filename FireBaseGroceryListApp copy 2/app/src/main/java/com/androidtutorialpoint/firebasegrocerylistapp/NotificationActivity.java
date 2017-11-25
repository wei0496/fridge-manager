package com.androidtutorialpoint.firebasegrocerylistapp;
/**
 * Created by m804 on 24/11/2017.
 */

import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;


public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.notification_activity);
        Intent intent = new Intent(NotificationActivity.this, ListItemsActivity.class);
        startActivity(intent);
    }
}