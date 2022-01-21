package com.example.doggychat;

import android.content.ContextWrapper;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class Database {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private StorageReference mStorageRef;

    //Default Constructor to initialize the database
   Database() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public FirebaseAuth getDB(){
        return mAuth;
    }

    public void uploadPictureFromDevice(String path){
       Uri file = Uri.fromFile(new File(path));
       FirebaseUser user = mAuth.getCurrentUser();
       String newPath = "Nick/profile";
       StorageReference picRef = mStorageRef.child(newPath); //This will need to be tested and looked at, not sure where it is storing the picture yet
       picRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               //Create metadata from the user profile information
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
              //Return failure message
           }
       });

    }

    public boolean updateProfile(String name, String location, FirebaseUser user){
        UserProfileChangeRequest updateProfile = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        user.updateProfile(updateProfile);
        return true;
    }



}







