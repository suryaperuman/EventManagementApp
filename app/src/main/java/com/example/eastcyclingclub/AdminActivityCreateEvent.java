package com.example.eastcyclingclub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivityCreateEvent extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;

    Button createEventTypeBTN, returnToEventsBTN;

    EditText minAge, maxAge, pace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_create_event);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("events");

        // Dropdown menu for the event type
        Spinner eventType = findViewById(R.id.editEventType);
        ArrayAdapter<CharSequence> eventAdapter  = ArrayAdapter.createFromResource(this, R.array.EventOptions, R.layout.admin_spinner_event_type);
        eventAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        eventType.setAdapter(eventAdapter);

        //Dropdown menu for difficulty level selection
        Spinner difficultyLevel = findViewById(R.id.difficultyLevel);
        ArrayAdapter<CharSequence> difficultyAdapter  = ArrayAdapter.createFromResource(this, R.array.ExperienceLevelOptions, R.layout.admin_spinner_event_type);
        difficultyAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        difficultyLevel.setAdapter(difficultyAdapter);

        // Variables for making admin-specified fields
        minAge = findViewById(R.id.minAge);
        maxAge = findViewById(R.id.maxAge);
        pace = findViewById(R.id.pace);
        createEventTypeBTN = findViewById(R.id.createEventTypeButtton);

        // Return to events button
        returnToEventsBTN = findViewById(R.id.returnToEventsButton);
        returnToEventsBTN.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminActivityEvents.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });



        createEventTypeBTN.setOnClickListener(view -> {

            //grab information from user input
            String minAgeText = minAge.getText().toString();
            String maxAgeText = maxAge.getText().toString();
            String paceText = pace.getText().toString();
            String selectedDifficultyLevel = difficultyLevel.getSelectedItem().toString();
            String selectedEventType = eventType.getSelectedItem().toString();


            // Checks if at least one of the age options are entered: if so, allows event creation, if not, outputs warning message
            if (((!minAgeText.isEmpty() || !maxAgeText.isEmpty()) && !paceText.isEmpty()) && !selectedDifficultyLevel.equals("Select Difficulty Level") && !selectedEventType.equals("Select Event Type")) {

                // Create an instance of the EventCreateHelperClass with event details
                AdminHelperClassEvent helper = new AdminHelperClassEvent(selectedEventType, selectedDifficultyLevel, minAgeText, paceText, maxAgeText);

                // Store the event information in the Firebase Realtime Database under the "events" node with a unique key
                reference.child(selectedEventType).setValue(helper);
                Toast.makeText(AdminActivityCreateEvent.this, "Event created successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), AdminActivityEvents.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivityCreateEvent.this);
                builder.setTitle("Try again!");
                builder.setMessage("Please specify event type, difficulty level, pace, and at least one of either minimum/maximum age requirements");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}