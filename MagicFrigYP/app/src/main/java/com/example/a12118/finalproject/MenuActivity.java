package com.example.a12118.finalproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private ListView mFrigItemList;     //Reference to the listview GUI component
    private ListAdapter frigAdapter;
    private LinearLayout frig_list;
    private LinearLayout shopping_list;
    private LinearLayout accoount_list;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    frig_list.setVisibility(View.VISIBLE);
                    shopping_list.setVisibility(View.GONE);
                    accoount_list.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    frig_list.setVisibility(View.GONE);
                    shopping_list.setVisibility(View.VISIBLE);
                    accoount_list.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:
                    frig_list.setVisibility(View.GONE);
                    shopping_list.setVisibility(View.GONE);
                    accoount_list.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize items
        frig_list = (LinearLayout)findViewById(R.id.frig_list);
        shopping_list = (LinearLayout)findViewById(R.id.shopping);
        accoount_list = (LinearLayout)findViewById(R.id.account);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFrigItemList = (ListView)findViewById(R.id.frig_item_list);
        frigAdapter = new CustomAdapter(this.getBaseContext());  //instead of passing the boring default string adapter, let's pass our own, see class MyCustomAdapter below!
        mFrigItemList.setAdapter(frigAdapter);
    }

}
