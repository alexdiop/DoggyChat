package com.example.doggychat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayChatSession extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private MessageAdapter msgAdapter;
    public FloatingActionButton send;
    FirebaseAuth mAuth;
    ProfileDB db;
    FirebaseDatabase database;
    Context context;
    String user;
    private List<String> messages;
    private List<String> users;
    String sender;
    Utils toasty = new Utils();
    Chat chat;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatpage);
        //Instantiate classes
        chat = new Chat();
        db = new ProfileDB();
        //Get the intent information from last activity
        Intent intent = getIntent();
        context = DisplayChatSession.this;
        user = intent.getStringExtra("user");
        sender = intent.getStringExtra("sender");

        database = FirebaseDatabase.getInstance();
        mAuth = db.getDB();
        messages = new ArrayList<>();
        users = new ArrayList<>();

        //Set the recyclerView and linear layout manager
        recyclerViewMessages = findViewById(R.id.recyclerview2);
        recyclerViewMessages.setHasFixedSize(true);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);

        //Instantiate the layouts widgets
        final TextView msgET = (TextView)findViewById(R.id.messageInput);
        send = (FloatingActionButton)findViewById(R.id.fab2); //Button to send a message


        //Set the listener for the database
        readMessages(user, sender);



        //Set a listener on the button that reads the users input and sends it to the Chat class to be sent to database
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgBody = msgET.getText().toString().trim();
                chat.sendMessage(user, msgBody, sender);
            }
        });




    }




    //Creates a database reference and then sets a listener on it for any messages being sent between user and sender, then it adds them to the adapter to be displayed
    private void readMessages(String user, String sender){
        final Utils toasty = new Utils();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users/" + user + "/Chats/" + sender);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                users.clear();
                for(DataSnapshot snap1 : snapshot.getChildren()){
                    Content content = snap1.getValue(Content.class);
                    messages.add(content.getMessage());
                    users.add(content.getSender());
                }
                msgAdapter = new MessageAdapter(getApplicationContext(), messages, users);
                recyclerViewMessages.setAdapter(msgAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







}
