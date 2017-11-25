package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidtutorialpoint.firebasegrocerylistapp.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListActivity extends AppCompatActivity {
    List<Item> list;
    MyAdapter adapter;
    String[] tag1s = {"meat", "veggies", "dairy", "ice-cream"};
    String[] tag2s = {"refrigerator", "freezer"};
    ListView lv;
    TextView tv[];//bottom
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_list);
        initView();
        initData();
        adapter = new MyAdapter();
        lv.setAdapter(adapter);

    }
 
    private void initView(){
        lv = (ListView) findViewById(R.id.lv);
        tv = new TextView[4];
        tv[0] = (TextView) findViewById(R.id.home);
        tv[0].setTag(0);
        tv[0].setSelected(true);
        tv[1] = (TextView) findViewById(R.id.explore);
        tv[1].setTag(1);
        tv[2] = (TextView) findViewById(R.id.shopping);
        tv[2].setTag(2);
        tv[3] = (TextView) findViewById(R.id.account);
        tv[3].setTag(3);
        for (int i = 0;i<tv.length;i++){
            tv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = (int) v.getTag();
                    //clear other focus
                    for (int i = 0;i<tv.length;i++){
                        tv[i].setTextColor(getResources().getColor(R.color.text_color));
                        tv[i].setSelected(false);
                    }
                    tv[p].setTextColor(getResources().getColor(R.color.blue_color));
                    tv[p].setSelected(true);
                }
            });
        }
    }
    private int random(int max){
        Random r = new Random();
        return r.nextInt(max)%max;
    }
    private void initData() {
        list = new ArrayList<>();
        Item item = new Item();
        item.id = 1;
        item.name = "Beef";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "7 Days";
        list.add(item);
        item = new Item();
        item.id = 2;
        item.name = "Milk";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "7 Days";
        list.add(item);
        item = new Item();
        item.id = 3;
        item.name = "Apple";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "10 Days";
        list.add(item);
        item = new Item();
        item.id = 4;
        item.name = "Haagen-Dazs";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "2 Days";
        list.add(item);
        item = new Item();
        item.id = 5;
        item.name = "Orange";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "10 Days";
        list.add(item);
        item = new Item();
        item.id = 6;
        item.name = "Chicken";
        item.tag1 = tag1s[random(tag1s.length)];//random
        item.tag2 = tag2s[random(tag2s.length)];//random
        item.date = "5 Days";
        list.add(item);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ListActivity.this).inflate(R.layout.item_list, null);
            TextView id = (TextView) convertView.findViewById(R.id.id);
            id.setText(list.get(position).id+"");
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(list.get(position).name);
            TextView tag1 = (TextView) convertView.findViewById(R.id.tag1);
            tag1.setText(list.get(position).tag1);
            tag1.setTag(position);
            TextView tag2 = (TextView) convertView.findViewById(R.id.tag2);
            tag2.setText(list.get(position).tag2);
            tag2.setTag(position);
            EditText et = (EditText) convertView.findViewById(R.id.date);
            et.setText(list.get(position).date);
            tag1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    ShowTag1Dialog(pos);
                }
            });
            tag2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    ShowTag2Dialog(pos);
                }
            });
            return convertView;
        }

    }

    private void ShowTag1Dialog(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("Please Choose:");
        builder.setItems(tag1s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.get(pos).tag1 = tag1s[which];
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void ShowTag2Dialog(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        builder.setTitle("Please Choose:");
        builder.setItems(tag2s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.get(pos).tag2 = tag2s[which];
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }
}
