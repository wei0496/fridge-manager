package com.androidtutorialpoint.firebasegrocerylistapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 12118 on 2017/11/13.
 */

public class CustomAdapter extends BaseAdapter {

    private String names[];              // name for the item
    private HashMap<String,Integer> icons;   // icons for the item
    private int expiration_value[];  // expiration duration for the item
    private ArrayList<ListItem> list;
    private ArrayList<ListItem> listAll;

    Context context;

    public CustomAdapter(Context baseContext, ArrayList<ListItem> mListItems) {
        //initializing our data in the constructor.
        context = baseContext;  //saving the context we'll need it again.

        names = baseContext.getResources().getStringArray(R.array.names);  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml

        icons = new HashMap<>();   //Could also use helper function "getDrawables(..)" below to auto-extract drawable resources, but keeping things as simple as possible.
        icons.put("meat",R.mipmap.meat_icon);
        icons.put("fruit",R.mipmap.fruits_icon);
        icons.put("dairy",R.mipmap.milk_icon);
        icons.put("veggies",R.mipmap.vegetables_icon);
        icons.put("ice-cream",R.mipmap.ice_cream_icon);
        icons.put("other",R.mipmap.other_icon);
//        String[] tag1s = { "ice-cream","other"};

        expiration_value = baseContext.getResources().getIntArray(R.array.expiration_duration);
//        list = new ArrayList<>();
//        list.addAll(mListItems);
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
        TextView location_tag = (TextView) row.findViewById(R.id.location_tag);

//        item_id.setText(names[position]);
//        expiration_bar.setMax(expiration_value[position]);
//        icon.setImageResource(icons.get(position).intValue());

//        icon.setImageResource(icons.get(list.get(position).getName()));

        icon.setImageResource(icons.get(list.get(position).getTag()));
        item_id.setText(list.get(position).getName());
        expiration_bar.setText(list.get(position).getExpirationDate());

        if (list.get(position).getReOrFree() == "true")
        {
            location_tag.setText("in refrigerator");
        }
        else
        {
            location_tag.setText("in freezer");
        }

        return row;
    }

    //for delete from my refrigerator
    public ListItem deleteItem(final int position)
    {
        Log.w("delete item",Integer.toString(position));
        final ListItem delItem = list.get(position);

        //database manipulate:
        // path to this user's all items

        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("listItem").child(Uid);
        Log.w("dbref",dbref.toString());
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // key waiting to be delete.
                List<String> keyList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, ListItem> listMap = new HashMap<>();
                    ListItem childItem = ds.getValue(ListItem.class);
                    if (childItem.getName() == delItem.getName() && childItem.getExpirationDate() == delItem.getExpirationDate()) {
                        keyList.add(ds.getKey());
                    }
                }
                Log.w("length:",Integer.toString(keyList.size()));
                for(String key:keyList)
                {
                    DatabaseReference temp = dbref.child(key);
                    temp.removeValue();
                }

//                HashMap<String,Object> updatemap = new HashMap<>();
//                updatemap.put(Uid,updateList);
//                dbref.updateChildren(updatemap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //update view
        listAll.remove(list.get(position));
        list.remove(position);

        notifyDataSetChanged();
        Toast.makeText(context,"delete success",Toast.LENGTH_SHORT).show();
        return delItem;
    }

    public Boolean deleteItem(ListItem delItem)
    {
        if(list.contains(delItem))
        {
            listAll.remove(delItem);
            list.remove(delItem);
            notifyDataSetInvalidated();
            return true;
        }
        else{
            return false;
        }
    }
    public void filter(ArrayList<String> filerList)
    {
        if(filerList.size()==0)
        {
            list.addAll(listAll);
            notifyDataSetChanged();
            return;
        }
        else
        {
            Log.w("listAllsize::",Integer.toString(listAll.size()));
            list.clear();

            for(ListItem listItem:listAll)
            {
                if(filerList.contains(listItem.getTag()))
                {
                    list.add(listItem);
                }
            }
            notifyDataSetChanged();
            return;
        }

    }

    public void setAll(ArrayList<ListItem> all)
    {
        listAll = new ArrayList<>();
        listAll.addAll(all);
    }

}
