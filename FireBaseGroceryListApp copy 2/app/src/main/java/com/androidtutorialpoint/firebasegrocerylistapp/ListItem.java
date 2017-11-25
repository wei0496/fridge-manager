package com.androidtutorialpoint.firebasegrocerylistapp;

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

    public String getListItemText() {
        return listItemText;
    }

    public void setListItemText(String listItemText) {
        this.listItemText = listItemText;
    }

    public void setListItemCreationDate(String listItemCreationDate) {
        this.listItemCreationDate = listItemCreationDate;
    }

    @Override
    public String toString() {
        return this.listItemText +"\n" + this.listItemCreationDate;
    }

    public String getName() {return listItemText;}
    public String getListItemCreationDate() {
        return listItemCreationDate;
    }

    public String getExpirationDate() {return ExpirationDate; }

    public String getTag() {return Tag; }

    public ListItem() {
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
    }

    public ListItem(String listItemText, String Expiration, String Tag) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.listItemCreationDate = sdf.format(new Date());
        this.listItemText = listItemText;
        this.ExpirationDate = Expiration;
        this.Tag = Tag;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("listItemText", listItemText);
        result.put("listItemCreationDate", listItemCreationDate);
        result.put("ExpirationDate", ExpirationDate);
        result.put("Tag", Tag);
        return result;
    }

}