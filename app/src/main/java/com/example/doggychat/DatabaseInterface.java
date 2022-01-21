package com.example.doggychat;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

public interface DatabaseInterface {


   FirebaseAuth getDB();
   boolean uploadPictureFromDevice(String path);
   boolean updateProfileLocation(String name, String location, FirebaseUser user);
   boolean updateDisplayName(String name, FirebaseUser user);
   void signIn(String email, String password);
   void createAccount(String email, String password);
   boolean createUser(FirebaseUser user);



}
