package com.example.doggychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Preferences extends AppCompatActivity {

    FirebaseDatabase database;
    String displayName;

    private int locationPref;
    private int statusPref;
    private int eventPref;
    private int eventNotifications;
    private int statusNotfications;
    private int checkInNotifications;

    Spinner locationSpinner;
    Spinner statusSpinner;
    Spinner eventSpinner;
    Switch eventToggle;
    Switch statusToggle;
    Switch checkInToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        retrievePreferences();//get preferences from database and update local values
        ProfileDB db = new ProfileDB();

        // Initialize Firebase Auth
        FirebaseAuth mAuth = db.getDB();

        //Returns the users display name
        displayName = mAuth.getCurrentUser().getDisplayName();

        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();//return to profile
            }
        });

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        fillSpinner(locationSpinner);

        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        fillSpinner(statusSpinner);

        eventSpinner = (Spinner) findViewById(R.id.eventSpinner);
        fillSpinner(eventSpinner);

        eventToggle = findViewById(R.id.eventToggle);
        eventToggle.setChecked(true);
        statusToggle = findViewById(R.id.statusToggle);
        checkInToggle = findViewById(R.id.checkInToggle);

        retrievePreferences();//get preferences from database and update local values

        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                savePreferences();//upload preferences to database
                finish();//return to profile
            }
        });
    }

    @Override
    protected void onActivityResult(int request, int result, Intent intent){
        super.onActivityResult(request, result, intent);
    }

    private void fillSpinner(Spinner spinner){
        String[] arraySpinner = new String[] { "Only Me", "My Friends", "Everyone"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //"Only Me", "My Friends", "Everyone" = 0,1,2
    }

    //retrieve the users preferences from the database
    private void retrievePreferences(){
        SimpleDateFormat formatter= new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Users/" + displayName + "/Preferences");
        DatabaseReference child = reference.child("Last Changed");
        child.setValue(formatter.format(date));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference reference;

                //snapshot.getChildren() has a value of 6 (Preferences has 6 children)
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    boolean notEmpty = false;
                    if(snapshot.hasChild("LocationPreference")){
                        notEmpty = true;
                    }
                    if(notEmpty){
                        locationPref = snapshot.child("LocationPreference").getValue(Integer.class);
                    }else{
                        reference = database.getReference().child("Users/" + displayName + "/Preferences/LocationPreference");
                        reference.setValue(0);
                        locationPref = 0; //default value of "Only Me"
                    }

                    if(notEmpty)
                        statusPref = snapshot.child("StatusPreference").getValue(Integer.class);
                    else{
                        reference = database.getReference().child("Users/" + displayName + "/Preferences/StatusPreference");
                        reference.setValue(1);
                        statusPref = 1; //default value of "My Friends"
                    }

                    if(notEmpty)
                        eventPref = snapshot.child("EventPreference").getValue(Integer.class);
                    else{
                        reference = database.getReference().child("Users/" + displayName + "/Preferences/EventPreference");
                        reference.setValue(2);
                        eventPref = 2; //default value of "Everyone"
                    }

                    if(notEmpty)
                        eventNotifications = snapshot.child("EventNotifications").getValue(Integer.class);
                    else{
                        reference = database.getReference().child("Users/" + displayName + "/Preferences/EventNotifications");
                        reference.setValue(1);
                        eventNotifications = 1; //default value of "ON"
                    }

                    if(notEmpty)
                        statusNotfications = snapshot.child("StatusNotifications").getValue(Integer.class);
                    else{
                        reference = database.getReference().child("Users/" + displayName + "/Preferences/StatusNotifications");
                        reference.setValue(1);
                        statusNotfications = 1; //default value of "ON"
                    }

                    if(notEmpty)
                        checkInNotifications = snapshot.child("CheckInNotifications").getValue(Integer.class);
                    else{
                        reference = database.getReference().child("Users/" + displayName + "/Preferences/CheckInNotifications");
                        reference.setValue(1);
                        checkInNotifications = 1; //default value of "ON"
                    }
                }

                setFields();//set the UI values to reflect the update above
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //grab data from fields and store in local variables
    //if data fields have not been manipulated by user, a default set of values will be set
    private void savePreferences(){

        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Users/" + displayName + "/Preferences");

        reference.child("LocationPreference").setValue(locationSpinner.getSelectedItemPosition());
        reference.child("StatusPreference").setValue(statusSpinner.getSelectedItemPosition());
        reference.child("EventPreference").setValue(eventSpinner.getSelectedItemPosition());

        if(eventToggle.isChecked()){
            reference.child("EventNotifications").setValue(1);
        }else{
            reference.child("EventNotifications").setValue(0);
        }

        if(statusToggle.isChecked()){
            reference.child("StatusNotifications").setValue(1);
        }else{
            reference.child("StatusNotifications").setValue(0);
        }

        if(checkInToggle.isChecked()){
            reference.child("CheckInNotifications").setValue(1);
        }else{
            reference.child("CheckInNotifications").setValue(0);
        }
    }

    //update ui fields to reflect the current values of preferences
    public void setFields (){

        //set the value of each spinner
        locationSpinner.setSelection(locationPref, true);
        statusSpinner.setSelection(statusPref);
        eventSpinner.setSelection(eventPref);

        //set the value of each toggle
        if(eventNotifications > 0){
            eventToggle.setChecked(true);
        }else{
            eventToggle.setChecked(false);
        }

        if(statusNotfications > 0){
            statusToggle.setChecked(true);
        }else
            statusToggle.setChecked(false);

        if(checkInNotifications > 0){
            checkInToggle.setChecked(true);
        }else
            checkInToggle.setChecked(false);
    }
}