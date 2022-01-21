package com.example.doggychat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayChat extends AppCompatActivity implements ChatAdapter.OnNoteListener {


    Chat chat = new Chat();
    String user;
    FirebaseAuth mAuth;
    ProfileDB db;
    FirebaseDatabase database;
    Context context;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<String> mUsers;

    @Override
    protected void onCreate(Bundle SavedInstanceBundle){
        db = new ProfileDB();
        Intent i = getIntent();
        context = DisplayChat.this;
        user = i.getStringExtra("user");
        mAuth = db.getDB();
        database = FirebaseDatabase.getInstance();
        super.onCreate(SavedInstanceBundle);
        setContentView(R.layout.chatgroups);
        LayoutInflater inflater;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.chatlayout);
        View view = inflater.inflate(R.layout.chatgroups, layout, true);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        mUsers = new ArrayList<>();
        readUsers(user);




    }

    //This method handles the page that shows each chat the user is a part of. Pictures arent showing up correctly yet
    private void readUsers(String user){
        final Utils toasty = new Utils();
        final String finalUser = user;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Users" + "/" + user + "/Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String displayName = snapshot1.getKey();
                    if(!mUsers.contains(displayName)){
                   //     toasty.showToast(DisplayChat.this, "We have a child: " + displayName);
                        mUsers.add(displayName);
                    }
                }
                chatAdapter = new ChatAdapter(getApplicationContext(), mUsers, DisplayChat.this);

                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(context, DisplayChatSession.class);
        intent.putExtra("user", user);
        intent.putExtra("sender", mUsers.get(position));
        startActivity(intent);

    }
}
