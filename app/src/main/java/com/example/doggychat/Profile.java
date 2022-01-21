package com.example.doggychat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Profile{

    public ArrayList<Chat> chats = new ArrayList<Chat>();
    public String name;
    public String location;
    public String status;
    public String[] friendsList;
    public int activityPoints;
    private static int RESULT_LOAD_IMAGE = 1;
    ProfileDB db;
    FirebaseAuth mAuth;
    ImageView profilePic;
    TextView displayName;
    TextView locationName;
    TextView userBio;
    FirebaseUser user;
    public String imageURL;

    Profile(){
        db = new ProfileDB();
        mAuth = db.getDB();
        user = mAuth.getCurrentUser();
        name = mAuth.getCurrentUser().getDisplayName();
        try {
            imageURL = mAuth.getCurrentUser().getPhotoUrl().toString();
        }
        catch (Exception e){

        }
    }

    //Test method for uploading messages to the database, not in use currently
    public void updateUI(Context context){
        final Context context2 = context;
        FirebaseDatabase database = FirebaseDatabase.getInstance();



    }



    //Adds a listener on the database so if the location changes then the profile page will be updated
    public String updateLoc(){
        DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference().child("Users");
        String name = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
//        dbref.child(name).child("Location").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                try{
//                    if(snapshot.getValue() != null){
//                        try{
//                            location = snapshot.getValue().toString();
//
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                    else{
//
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return location;
    };


    public FirebaseUser getUser(){
        return user;
    }

    public String getName(){
        return name;
    }

    //Not currently in use
    public void setProfilePic(){

    }

    public void createProfile(){

    }

    public void editName(){

    }

    public void editLocation(){

    }

    public void editPreferences(){

    }


    public void changePicture(){

    }


    public void addFriend(){

    }



}
