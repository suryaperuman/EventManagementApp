package com.example.eastcyclingclub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParticipantActivityCompleteProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference reference;
    EditText participantAge, participantPace;

    String participantExperienceLevel;

    Spinner spinner;
    Button completeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_complete_profile);

        String userUsername;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userUsername = null;
            } else {
                userUsername = extras.getString("username");
            }
        } else {
            userUsername = (String) savedInstanceState.getSerializable("username");
        }

        // Initializing database
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(userUsername);

        // Variables for each text field and the complete profile button
        participantAge = findViewById(R.id.ageParticipant);
        participantPace = findViewById(R.id.paceParticipant);
        completeProfile = findViewById(R.id.completeProfile);

        spinner = findViewById(R.id.dropdown_menu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ExperienceLevelOptions, R.layout.participant_spinner_experience_level);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        participantExperienceLevel = spinner.getSelectedItem().toString();


        completeProfile.setOnClickListener(view -> {
            // Grabbing information from user input
            String participantAgeText = participantAge.getText().toString();
            String participantPaceText = participantPace.getText().toString();
            String participantExperienceLevel = spinner.getSelectedItem().toString();

            if (participantAgeText.isEmpty() || (participantPaceText.isEmpty() || participantExperienceLevel.isEmpty())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParticipantActivityCompleteProfile.this);
                builder.setTitle("Try again!");
                builder.setMessage("Ensure that all fields are specified");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else if (!validateAge(participantAgeText)) {
                participantAge.setError("Age entered is invalid");
                participantAge.requestFocus();
            }
            else if (!validatePace(participantPaceText)) {
                participantPace.setError("Pace entered is invalid");
                participantPace.requestFocus();
            }
            else {
                DatabaseReference specificUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUsername);

                specificUserReference.child("age").setValue(participantAgeText);
                specificUserReference.child("pace").setValue(participantPaceText);
                specificUserReference.child("experienceLevel").setValue(participantExperienceLevel);

                Toast.makeText(ParticipantActivityCompleteProfile.this, "Profile Completed!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ParticipantActivityEvents.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("age", participantAgeText);
                intent.putExtra("pace", participantPaceText);
                intent.putExtra("experienceLevel", participantExperienceLevel);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });
    }

    private boolean validateAge(String age) {
        return Integer.parseInt(age) > 0;
    }
    private boolean validatePace(String pace) {
        return Integer.parseInt(pace) > 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        // Depending on the selected item, perform actions accordingly
        if (selectedItem.equals("Newbie")) {
            Toast.makeText(this, "Newbie Selected", Toast.LENGTH_SHORT).show();
        }
        else if (selectedItem.equals("Beginner")) {
            Toast.makeText(this, "Beginner Selected", Toast.LENGTH_SHORT).show();
        }
        if (selectedItem.equals("Average")) {
            Toast.makeText(this, "Average Selected", Toast.LENGTH_SHORT).show();
        }
        if (selectedItem.equals("Experienced")) {
            Toast.makeText(this, "Experienced Selected", Toast.LENGTH_SHORT).show();
        }
        if (selectedItem.equals("Pro")) {
            Toast.makeText(this, "Pro Selected", Toast.LENGTH_SHORT).show();
        }
        if (selectedItem.equals("Cycling is Everything To Me"))
            Toast.makeText(this, "Cycling is Everything To Me", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show();
    }
}