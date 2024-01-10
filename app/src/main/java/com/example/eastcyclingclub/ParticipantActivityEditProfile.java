package com.example.eastcyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParticipantActivityEditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editName, editEmail, editPassword, editAge, editPace;
    Spinner spinner;
    Button saveButton;
    String nameUser, usernameUser, passwordUser, ageUser, paceUser, experienceLevelUser;

    FirebaseDatabase database;
    DatabaseReference reference;

    public ParticipantActivityEditProfile(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_edit_profile);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                usernameUser = null;
                nameUser = null;
                passwordUser = null;
                ageUser = null;
                paceUser = null;
                experienceLevelUser = null;
            } else {
                usernameUser = extras.getString("username");
                nameUser = extras.getString("name");
                passwordUser = extras.getString("password");
                ageUser = extras.getString("age");
                paceUser = extras.getString("pace");
                experienceLevelUser = extras.getString("experienceLevel");
            }
        } else {
            usernameUser = (String) savedInstanceState.getSerializable("username");
            nameUser = (String) savedInstanceState.getSerializable("name");
            passwordUser = (String) savedInstanceState.getSerializable("password");
            ageUser = (String) savedInstanceState.getSerializable("age");
            paceUser =(String) savedInstanceState.getSerializable("pace");
            experienceLevelUser = (String) savedInstanceState.getSerializable("experienceLevel");
        }

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editUsername);
        editAge = findViewById(R.id.editAge);
        editPace = findViewById(R.id.editPace);

        showUserData();

        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String age = editAge.getText().toString();
                String pace = editPace.getText().toString();
                String experienceLevel = spinner.getSelectedItem().toString();


                if (allCredentialsAreValid(name, email, password, age, pace)) {
                    reference.child(usernameUser).child("name").setValue( name );
                    reference.child(usernameUser).child("email").setValue( email );
                    reference.child(usernameUser).child("password").setValue( password );
                    reference.child(usernameUser).child("age").setValue( age );
                    reference.child(usernameUser).child("pace").setValue( pace );
                    reference.child(usernameUser).child("experienceLevel").setValue(experienceLevel);
                    Toast.makeText(ParticipantActivityEditProfile.this, "Saved", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ParticipantActivityEditProfile.this, ParticipantActivityProfile.class);

                    intent.putExtra("name", name );
                    intent.putExtra("username", usernameUser);
                    intent.putExtra("email", email );
                    intent.putExtra("password", password);
                    intent.putExtra("age", age);
                    intent.putExtra("pace", pace);
                    intent.putExtra("experienceLevel", experienceLevel);

                    startActivity(intent);
                } else if ( name.isEmpty() ) {
                    editName.setError("No Name Specified");
                    editName.requestFocus();
                }
                else if ( email.isEmpty() ) {
                    editEmail.setError("No Email Specified");
                    editEmail.requestFocus();
                }
                else if ( password.isEmpty() ) {
                    editPassword.setError("No Password Specified");
                    editPassword.requestFocus();
                }
                else if ( age.isEmpty() ) {
                    editAge.setError("No Age Specified");
                    editAge.requestFocus();
                }
                else if (pace.isEmpty() ) {
                    editPace.setError("No Pace Specified");
                    editPace.requestFocus();
                }
                else {
                    Toast.makeText(ParticipantActivityEditProfile.this, "Ensure All Fields Are Entered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean allCredentialsAreValid(String newUserName, String newEmail, String newPassword, String newAge, String newPace){

        String emailRegex = "^[a-zA-Z0-9][a-zA-Z0-9_]+@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)";

        Pattern pattern = Pattern.compile(emailRegex, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(newEmail);

        // makes sure the email is valid, username is non-empty, and password is non-empty
        return ( matcher.matches() && !( newUserName.isEmpty() ) && !( newPassword.isEmpty() ) && (!newAge.isEmpty()) && (!newPace.isEmpty()) );
    }

    public void showUserData() {
        if( usernameUser == null ){ return; }

        DatabaseReference specificUserReference = database.getInstance().getReference().child("users").child(usernameUser);

        final String[] nameFromDatabase = new String[1];
        final String[] emailFromDatabase = new String[1];
        final String[] passwordFromDatabase = new String[1];
        final String[] ageFromDatabase = new String[1];
        final String[] paceFromDatabase = new String[1];
        final String[] experienceLevelFromDatabase = new String[1];

        spinner = findViewById(R.id.editExperienceLevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ExperienceLevelOptions, R.layout.participant_spinner_experience_level);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                specificUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            nameFromDatabase[0] = dataSnapshot.child("name").getValue(String.class);
                            emailFromDatabase[0] = dataSnapshot.child("email").getValue(String.class);
                            passwordFromDatabase[0] = dataSnapshot.child("password").getValue(String.class);
                            ageFromDatabase[0] = dataSnapshot.child("age").getValue(String.class);
                            paceFromDatabase[0] = dataSnapshot.child("pace").getValue(String.class);
                            experienceLevelFromDatabase[0] = dataSnapshot.child("experienceLevel").getValue(String.class);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editName.setText(nameFromDatabase[0]);
                                    editEmail.setText(emailFromDatabase[0]);
                                    editPassword.setText(passwordFromDatabase[0]);
                                    editAge.setText(ageFromDatabase[0]);
                                    editPace.setText(paceFromDatabase[0]);
                                    spinner.setSelection(adapter.getPosition(experienceLevelFromDatabase[0]));
                                }
                            });

                            Log.d("TAG", "Profile values retrieved");
                        } else {
                            Log.d("TAG", "Profile values do not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("TAG", "Error retrieving profile values", databaseError.toException());
                    }
                });
            }
        }).start();
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