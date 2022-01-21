package com.example.doggychat;

import android.content.Intent;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chat implements Mediator {

    public Map<String, String> messages = new HashMap<>();



    @Override
    public void sendMessage(String user, String message, String sender) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date()); // Find todays date
        Content content = new Content(sender, message);
        DatabaseReference myRef = database.getReference("/Users" + "/" + user +"/Chats" + "/" + sender +"/" + currentDateTime);
        DatabaseReference myRef2 = database.getReference("/Users" + "/" + sender +"/Chats" + "/" + user + "/" + currentDateTime);
        myRef.setValue(content.toMap());
        myRef2.setValue(content.toMap());

    }

    @Override
    public void receiveMessage() {

    }

    @Override
    public void updateHistory(String message) {

    }



    public Map getMessages(){
        return messages;
    }
}
