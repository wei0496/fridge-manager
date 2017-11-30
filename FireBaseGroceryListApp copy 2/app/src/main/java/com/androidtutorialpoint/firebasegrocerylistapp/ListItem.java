package com.androidtutorialpoint.firebasegrocerylistapp;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ListItem {

    private String listItemText;
    private String ExpirationDate;
    private String listItemCreationDate;
    private String Tag;
    private Boolean reOrFree;// re: true; freezer:false


    ListItem(BGItem bgItem)
    {
        this.listItemText = bgItem.id;
        this.ExpirationDate = Integer.toString(bgItem.expiration_duration);
        this.Tag = bgItem.type_tag;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        this.listItemCreationDate = sdf.format(new Date());
        Log.w("date::",this.listItemCreationDate);
    }

    public String getListItemText() {
        return listItemText;
    }

    public void setListItemText(String listItemText) {
        this.listItemText = listItemText;
    }

    public void setListItemCreationDate(String listItemCreationDate) {
        this.listItemCreationDate = listItemCreationDate;
    }
    public boolean setExpirationDate(String date)
    {
        try {
            this.ExpirationDate = date;
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }


    @Override
    public String toString() {
        return this.listItemText +"\n" + this.listItemCreationDate;
    }

    public String getName() {
        if(listItemText!=null)
            return listItemText;
        else
            return null;
    }
    public String getListItemCreationDate() {
        if(listItemText!=null)
            return listItemCreationDate;
        else
            return null;
    }

    public String getExpirationDate() {
        if(ExpirationDate!=null)
            return ExpirationDate;
        else
            return null;
    }

    public String getTag() {
        if(Tag!=null)
            return Tag;
        else
            return null;
    }

    public Boolean setTag(String tag){
        try{
            this.Tag = tag;
            return true;
        }
        catch (Exception e)
        {
            System.out.print(e);
            return false;
        }
    }
    public String getReOrFree()
    {
        if (reOrFree!=null)
            return Boolean.toString(reOrFree);
        else
            return null;
    }
    public Boolean setReOrFree(Boolean reOrFree)
    {
        try{
            this.reOrFree = reOrFree;
            return true;
        }
        catch (Exception e)
        {
            System.out.print(e);
            return false;
        }

    }
    public ListItem() {
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        this.listItemCreationDate = sdf.format(new Date());
    }

    public ListItem(String listItemText, String Expiration, String Tag) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        this.listItemCreationDate = sdf.format(new Date());

        this.listItemText = listItemText;
        this.ExpirationDate = Expiration;
        this.Tag = Tag;
    }
    public ListItem(String listItemText, String Expiration, String Tag, Boolean reOrFree) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        this.listItemCreationDate = sdf.format(new Date());

        this.listItemText = listItemText;
        this.ExpirationDate = Expiration;
        this.Tag = Tag;
        this.reOrFree = reOrFree;
    }

    public boolean BGable()
    {
        if(this.ExpirationDate!=null && this.Tag!=null && this.listItemText!=null)
            return true;
        else
            return false;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("listItemText", listItemText);
        result.put("listItemCreationDate", listItemCreationDate);
        result.put("ExpirationDate", ExpirationDate);
        result.put("Tag", Tag);
        result.put("reOrFree",reOrFree);
        return result;
    }

}