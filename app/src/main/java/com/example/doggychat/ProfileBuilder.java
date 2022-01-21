package com.example.doggychat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ProfileBuilder {

    public String name;
    public String location;
    public String userbio;
    public Map<String, Boolean> map = new HashMap<>();

    public ProfileBuilder(){

    }

    public ProfileBuilder(String name, String location, String userbio){
        this.name=name;
        this.location=location;
        this.userbio=userbio;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("Location", location);
        result.put("userbio", userbio);
        return result;
    }



}
