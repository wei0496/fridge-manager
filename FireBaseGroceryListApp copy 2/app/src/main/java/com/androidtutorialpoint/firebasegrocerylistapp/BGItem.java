package com.androidtutorialpoint.firebasegrocerylistapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cornfieldfox on 11/26/17.
 */

public class BGItem {
    int expiration_duration;
    String id;
    String type_tag;

    BGItem(int ed, String id, String type_tag)
    {
        this.expiration_duration = ed;
        this.type_tag = type_tag;
        this.id = id;
    }
    BGItem(ListItem listItem)
    {

        this.expiration_duration = Integer.parseInt(listItem.getExpirationDate());
        this.id = listItem.getListItemText();
        this.type_tag = listItem.getTag();

    }
    BGItem()
    {
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("expiration_duration", expiration_duration);
        result.put("type_tag", type_tag);
        return result;
    }
}
