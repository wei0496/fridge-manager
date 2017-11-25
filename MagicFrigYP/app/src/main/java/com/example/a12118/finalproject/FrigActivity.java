package com.example.a12118.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class FrigActivity extends AppCompatActivity {

    ListView frig_list = findViewById(R.id.frig_list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frig);
    }
}
