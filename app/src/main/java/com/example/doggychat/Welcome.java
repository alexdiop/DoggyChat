package com.example.doggychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Welcome extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProfileDB db;
    private static final String TAG = "EmailPassword";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ProfileDB();
        //Set view to login page
        setContentView(R.layout.login);
        //Initialize the textViews with the users inputted information
        final TextView userEmail = (TextView)findViewById(R.id.userEmail);
        final TextView userPassword = (TextView)findViewById(R.id.userPassword);
        final CheckBox createAccount = (CheckBox)findViewById(R.id.createAccount);
        Button start = (Button)findViewById(R.id.startButton);
        // Initialize Firebase Auth
        mAuth = db.getDB();
        //Returns the users ID
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                db.setContext(Welcome.this);
                if(createAccount.isChecked()){
                    db.createAccount(email.trim(), password);
                }
                else{
                    db.signIn(email, password);
                }
            }
        });
        
    }





    //Method to find a user in the database and return their profile information (I used this for test purposes, feel free to mess around with it)
    private void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }





}