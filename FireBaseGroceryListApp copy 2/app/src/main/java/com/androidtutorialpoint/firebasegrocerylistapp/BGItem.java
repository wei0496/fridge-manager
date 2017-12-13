package com.androidtutorialpoint.firebasegrocerylistapp;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cornfieldfox on 11/26/17.
 */

public class BGItem implements Serializable {
    int expiration_duration;
    String id;
    String type_tag;
    // initiate background database
    BGItem(int ed, String id, String type_tag)
    {
        this.expiration_duration = ed;
        this.type_tag = type_tag;
        this.id = id;
    }
    //transform from listItem to BGItem
    BGItem(ListItem listItem)
    {

        this.expiration_duration = Integer.parseInt(listItem.getExpirationDate());
        this.id = listItem.getListItemText();
        this.type_tag = listItem.getTag();

    }

    BGItem()
    {
    }
    //for firebase
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("expiration_duration", expiration_duration);
        result.put("type_tag", type_tag);
        return result;
    }

    // getter

    public int getExpirationDate() {
        return expiration_duration;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type_tag;
    }

}
