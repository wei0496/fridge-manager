package com.androidtutorialpoint.firebasegrocerylistapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class resource {

    private String ReceiptText;
    private String FoodName;
    private String Tag;
    private int expiration_duration;

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String FoodName) {
        this.FoodName = FoodName;
    }

    public resource() {
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
    }

    public resource(String ReceiptText, String FoodName, String Tag,  int expiration_duration) {
        this.ReceiptText = ReceiptText;
        this.FoodName = FoodName;
        this.expiration_duration = expiration_duration;
        this.Tag = Tag;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ReceiptText", ReceiptText);
        result.put("FoodName", FoodName);
        result.put("Tag", Tag);
        result.put("expiration_duration", expiration_duration);
        return result;
    }
}