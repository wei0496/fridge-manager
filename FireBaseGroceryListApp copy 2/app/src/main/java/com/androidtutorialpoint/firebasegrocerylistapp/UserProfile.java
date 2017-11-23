package com.androidtutorialpoint.firebasegrocerylistapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserProfile {

    private String FirstName;
    private String LastName;
    private String Email;

    public UserProfile (){
        // Default constructor required for calls to DataSnapshot.getValue(ListItem.class)
    }

    public UserProfile(String FirstName, String LastName, String Email) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("FirstName", FirstName);
        result.put("LastName", LastName);
        result.put("Email", Email);
        return result;
    }
}