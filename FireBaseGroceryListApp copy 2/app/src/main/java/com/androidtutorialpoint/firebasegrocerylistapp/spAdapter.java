package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cornfieldfox on 12/1/17.
 */

public class spAdapter extends ArrayAdapter<CheckBox> {
    ArrayList<CheckBox> cbList;
    Context context;
    public spAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CheckBox> objects) {
        super(context, resource, objects);
        this.context = context;
        cbList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    public View getCustomView(int position, View covertView, ViewGroup parent)
    {
        View row;
        if(covertView== null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.spinner,null);

        }
        else {
            row = covertView;
        }


        CheckBox cb = (CheckBox) row.findViewById(R.id.spinnerRow);
        cb.setText(cbList.get(position).getText());
        return row;
    }
}
