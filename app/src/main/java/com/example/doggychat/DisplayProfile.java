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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DisplayProfile extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    ProfileDB db;
    FirebaseAuth mAuth;
    ImageView profilePic;
    TextView displayName;
    TextView locationName;
    TextView userBio;
    Profile user = new Profile();
    private DrawerLayout dl;
    private ActionBarDrawerToggle menuToggle;
    Context context;
    String location;
    String name;





    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepage);
        db = new ProfileDB();
        mAuth = db.getDB();
        db.setContext(this);
        displayName = (TextView)findViewById(R.id.profileName);
        userBio = (TextView)findViewById(R.id.userBio);
        profilePic = findViewById(R.id.profilePicture);
        locationName = findViewById(R.id.profileLocation);
        name = mAuth.getCurrentUser().getDisplayName();
        displayName.setText(name);
        dl = (DrawerLayout) findViewById(R.id.dl);
        menuToggle = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        menuToggle.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(menuToggle);
        menuToggle.syncState();

        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Handle each case of the menu
                int id = item.getItemId();
                context = DisplayProfile.this;

                if(id == R.id.Profile){
                    Intent intent = new Intent(context, DisplayProfile.class);
                    startActivity(intent);
                }
                else if(id == R.id.Feed){
               //     Intent intent = new Intent(context, Feed.class);
                //    startActivity(intent);
                }
                else if (id == R.id.Settings){

                }
                else if (id == R.id.Events){
                  //  Intent intent = new Intent(context, CreatedEvents.class);
                 //   startActivity(intent);
                }
                else if (id == R.id.Chat){
                    Intent intent = new Intent(context, DisplayChat.class);
                    intent.putExtra("user", name);
                    startActivity(intent);
                }


                return true;
            }
        });

        // Sets a listener on the bio section of the page that will pop out the userbio layout to get input to populate bio section
        userBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(DisplayProfile.this);
                View promptsView = li.inflate(R.layout.userbio, null, true);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayProfile.this);
                alertDialogBuilder.setView(promptsView);
                final EditText userBioET = (EditText)promptsView.findViewById(R.id.userBioField);
                alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userBio.setText(userBioET.getText());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //Set a listener on the button to upload a picture
        FloatingActionButton uploadPicButton = (FloatingActionButton)findViewById(R.id.uploadPictureButton);
        uploadPicButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        FloatingActionButton mapViewButton = (FloatingActionButton)findViewById(R.id.mapViewButton);
        mapViewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //go to map view
                viewMap();

            }
        });

        FloatingActionButton preferencesButton = (FloatingActionButton)findViewById(R.id.preferencesButton);
        preferencesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //go to preferences view
                editPreferences();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return menuToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }



    @Override //onStart is called right after onCreate finishes, it populates the profile page fields
    protected void onStart(){
        super.onStart();
        profilePic.setImageURI(mAuth.getCurrentUser().getPhotoUrl());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { //Due to asynchronous nature I had to use a wait call or else it would try to populate fields before Database finishes creating user
            @Override
            public void run() {
                location = user.updateLoc();
                locationName.setText(location);
                String name = mAuth.getCurrentUser().getDisplayName();
                displayName.setText(name);
                profilePic.setImageURI(mAuth.getCurrentUser().getPhotoUrl());
            }
        }, 1500);
    }


    @Override //Is called when the upload picture button is clicked and gets the local file from the user to send to database
    protected void onActivityResult(int request, int result, Intent intent){
        super.onActivityResult(request, result, intent);
        if (request == RESULT_LOAD_IMAGE && result == RESULT_OK && null != intent) {
            Uri selectedImage = intent.getData();
            profilePic.setImageURI(selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            db.uploadPictureFromDevice(picturePath);
            cursor.close();

        }


    }


    public void setContext(Context context){
        this.context = context;
    }

    public void viewMap(){
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void editPreferences(){
        Intent intent = new Intent(this, Preferences.class);
        startActivity(intent);
    }


}
