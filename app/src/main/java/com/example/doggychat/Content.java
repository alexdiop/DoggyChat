package com.example.doggychat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Content {

    public String timestamp;
    public String sender;
    public String message;
    public String recipient;
    public Map<String, Content> History = new HashMap<>();

    public Content(){

    }

    public Content(String sender, String body){
        this.sender = sender;
        this.message = body;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender", sender);
        result.put("message", message);
        result.put("recipient", recipient);
        return result;
    }

    public String getSender(){
        return sender;
    }

    public String getMessage(){
        return message;
    }

    public String getTimestamp(){
        return timestamp;
    }



}
