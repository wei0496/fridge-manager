package com.androidtutorialpoint.firebasegrocerylistapp;

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
    BGItem()
    {

    }
}
