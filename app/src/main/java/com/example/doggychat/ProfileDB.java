package com.example.doggychat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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

public class ProfileDB implements DatabaseInterface {
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private StorageReference mStorageRef;
    private Context context;
    FirebaseDatabase database;





    ProfileDB() {
        //Constructor for user DB class
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public FirebaseAuth getDB() {
        return mAuth;
    }

    @Override
    public boolean uploadPictureFromDevice(String path) {
        Uri file = Uri.fromFile(new File(path));
        FirebaseUser user = mAuth.getCurrentUser();
        String dbpath = user.getDisplayName();
        UserProfileChangeRequest updateUser = new UserProfileChangeRequest.Builder().setPhotoUri(file).build();
        user.updateProfile(updateUser);
        StorageReference picRef = mStorageRef.child(dbpath + "/profile.jpeg");
        picRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Create metadata from the user profile information
                Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Return failure message
                Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @Override
    public boolean updateProfileLocation(String name, String location, FirebaseUser user) {
        String path = "Users/" + name + "/Location";
        DatabaseReference myRef = database.getReference(path);
        myRef.setValue(location);
        return true;
    }

    @Override
    public boolean updateDisplayName(String name, FirebaseUser user) {
        UserProfileChangeRequest updatedInfo = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
      //  user.updateProfile(updatedInfo);
        return true;
    }

    public void setContext(Context context2){
        this.context = context2;
    }

    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent signIn = new Intent(context, DisplayProfile.class);
                            context.startActivity(signIn);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }

    @Override
    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());


                        }

                        // ...
                    }
                });
    }



    @Override
    public boolean createUser(final FirebaseUser user) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userName = (EditText) promptsView.findViewById(R.id.userBioField);
        final EditText userLocationet = (EditText) promptsView.findViewById(R.id.editLocation);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username;
                String userLocation;
                username = userName.getText().toString();
                userLocation = userLocationet.getText().toString(); //Will need to look at this, probably needs another method that will specifically call realtime database
                if(updateProfileLocation(username, userLocation, user) && updateDisplayName(username, user)) {
                    Toast.makeText(context, "The user location is: " + userLocation, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "The display name is: " + username,
                                Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DisplayProfile.class);
                    context.startActivity(intent);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        return true;

    }
}

