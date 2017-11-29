package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 12118 on 2017/11/13.
 */

public class CustomAdapter extends BaseAdapter {

    private String names[];              // name for the item
    private ArrayList<Integer> icons;   // icons for the item
    private int expiration_value[];  // expiration duration for the item
    private ArrayList<ListItem> list;

    Context context;

    public CustomAdapter(Context baseContext, ArrayList<ListItem> mListItems) {
        //initializing our data in the constructor.
        context = baseContext;  //saving the context we'll need it again.

        names = baseContext.getResources().getStringArray(R.array.names);  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml

        icons = new ArrayList<Integer>();   //Could also use helper function "getDrawables(..)" below to auto-extract drawable resources, but keeping things as simple as possible.
        icons.add(R.mipmap.fruits_icon);
        icons.add(R.mipmap.meat_icon);
        icons.add(R.mipmap.milk_icon);

        expiration_value = baseContext.getResources().getIntArray(R.array.expiration_duration);

        list = mListItems;
    }

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
        View row;   //  this refers to the row to be inflated or displayed.

        if (convertView == null) {  //indicates this is the first time we are creating this row.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.frig_fragment, parent, false);
        } else {
            row = convertView;
        }

        // fill the row with the correct image and strings
        ImageView icon = (ImageView) row.findViewById(R.id.icon);
        TextView item_id = (TextView) row.findViewById(R.id.item_id);
        TextView expiration_bar = (TextView) row.findViewById(R.id.expiration_date);

//        item_id.setText(names[position]);
//        expiration_bar.setMax(expiration_value[position]);
//        icon.setImageResource(icons.get(position).intValue());

        icon.setImageResource(R.mipmap.milk_icon);
        item_id.setText(list.get(position).getName());
        expiration_bar.setText(list.get(position).getExpirationDate());

        return row;
    }
}
