package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    String[] tag1s = {"meat", "veggies", "dairy", "ice-cream"};
    String[] tag2s = {"refrigerator", "freezer"};
    EditText name,date;
    TextView tag1,tag2;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        confirm = (Button) findViewById(R.id.confirm);

        tag1 = (TextView) findViewById(R.id.tag1);
        tag1.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tag1.setText(tag1s[0]);
        tag2 = (TextView) findViewById(R.id.tag2);
        tag2.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tag2.setText(tag2s[0]);
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTag1Dialog();
            }
        });
        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTag2Dialog();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                String dateString = date.getText().toString();
                if(TextUtils.isEmpty(nameString)||TextUtils.isEmpty(dateString)){
                    Toast.makeText(AddActivity.this,"Your must entry name and date",Toast.LENGTH_SHORT).show();
                    return ;
                }
                Intent intent = new Intent();
                intent.putExtra("name",nameString);
                intent.putExtra("date",dateString);
                intent.putExtra("tag1",tag1.getText().toString());
                intent.putExtra("tag2",tag2.getText().toString());
                setResult(120,intent);
                AddActivity.this.finish();
                Toast.makeText(AddActivity.this,"Add Shopping Success",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowTag1Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("Please Choose:");
        builder.setItems(tag1s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag1.setText(tag1s[which]);
            }
        });
        builder.show();
    }

    private void ShowTag2Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("Please Choose:");
        builder.setItems(tag2s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag2.setText(tag2s[which]);
            }
        });
        builder.show();
    }
}
