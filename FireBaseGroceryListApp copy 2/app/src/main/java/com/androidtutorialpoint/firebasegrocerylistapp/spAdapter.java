package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Cornfieldfox on 12/1/17.
 */

public class spAdapter extends ArrayAdapter<CheckBox> {
    //for specific filter
    ArrayList<CheckBox> cbName;
    Context context;
    ArrayList<CheckBox> cbList;
    public ArrayList<String> filterList;
    filterCallBack mCallBack;
    public spAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CheckBox> objects,filterCallBack mCallBack) {
        //getting the specific filter and make it to checkbox
        super(context, resource, objects);
        this.context = context;
        cbName = new ArrayList<>();

        CheckBox start = new CheckBox(context);
        start.setText("start");
        cbName.add(start);


        cbName.addAll(objects);
        cbList = new ArrayList<>();
        filterList  = new ArrayList<>();
        this.mCallBack = mCallBack;
    }

    @Nullable
    @Override
    public CheckBox getItem(int position) {
        return cbList.get(position);
    }

    @Override
    public int getCount() {
        return cbName.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // how will it look like when drop down.
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
        // the first item should look like a arrow.
        // each item has a text and a check box.
        // for the fist item. we just let the checkbox GONE, left the indicator.
        if(position ==0)
        {
            row.findViewById(R.id.spinnerRow).setVisibility(View.INVISIBLE);
            row.findViewById(R.id.spinnerRow).setActivated(false);
        }
        else
        {
            // other items have checkbox
            row.findViewById(R.id.filterTxt).setVisibility(View.GONE);


            final CheckBox cb = (CheckBox) row.findViewById(R.id.spinnerRow);
            cb.setText(cbName.get(position).getText().toString());

            cb.setTag("cb"+Integer.toString(position));
            if (cbList.size()-1<position)
                cbList.add(cb);
            else
                cbList.set(position,cb);
            //add listener, for filter.
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("clickable::",Boolean.toString(cb.isChecked()));
                    CheckBox checkBox = (CheckBox) v;


                    if(checkBox.isChecked() && !filterList.contains(checkBox.getText())) {
                        filterList.add(checkBox.getText().toString());
                        mCallBack.FilterChanged(filterList);

                    }
                    else
                    {
                        if(filterList.contains(checkBox.getText().toString())){
                            filterList.remove(checkBox.getText().toString());
                            mCallBack.FilterChanged(filterList);
                        }
                    }

                }
            });
        }
        return row;
    }

}


