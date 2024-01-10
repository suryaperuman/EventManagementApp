package com.example.eastcyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParticipantActivityProfile extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    TextView profileName, profileRole, profileUsername, profileAge, profilePace, profileExperienceLevel, welcomeText;
    Button editProfile, logout;
    String userUsername, userName, userRole, userPassword, userAge, userPace, userExperienceLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_profile);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userUsername = null;
                userName = null;
                userPassword = null;
                userRole = null;
                userAge = null;
                userPace = null;
                userExperienceLevel = null;
            } else {
                userUsername = extras.getString("username");
                userName = extras.getString("name");
                userPassword = extras.getString("password");
                userRole = extras.getString("role");
                userAge = extras.getString("age");
                userPace = extras.getString("pace");
                userExperienceLevel = extras.getString("experienceLevel");
            }
        } else {
            userUsername = (String) savedInstanceState.getSerializable("username");
            userName = (String) savedInstanceState.getSerializable("name");
            userRole = (String) savedInstanceState.getSerializable("role");
            userPassword = (String) savedInstanceState.getSerializable("password");
            userAge = (String) savedInstanceState.getSerializable("age");
            userPace = (String) savedInstanceState.getSerializable("pace");
            userExperienceLevel = (String) savedInstanceState.getSerializable("experienceLevel");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.eventsAvailable) {
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityEvents.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("password", userPassword);
                intent.putExtra("age", userAge);
                intent.putExtra("pace", userPace);
                intent.putExtra("experienceLevel", userExperienceLevel);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            if (id == R.id.profile) {
                return true;
            }
            if (id == R.id.eventsJoined) {
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityJoinedEvents.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("password", userPassword);
                intent.putExtra("age", userAge);
                intent.putExtra("pace", userPace);
                intent.putExtra("experienceLevel", userExperienceLevel);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            return false;
        });

        // Initializing database
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        profileUsername = findViewById(R.id.profileUsername);
        profileRole = findViewById(R.id.profileRole);
        profileUsername = findViewById(R.id.profileUsername);
        profileAge = findViewById(R.id.profileAge);
        profilePace = findViewById(R.id.profilePace);
        profileExperienceLevel = findViewById(R.id.profileExperienceLevel);

        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome Back\n" + userName);
        editProfile = findViewById(R.id.editButton);
        logout = findViewById(R.id.logoutButton);

        showUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityEditProfile.class);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("password", userPassword);
                intent.putExtra("age", userAge);
                intent.putExtra("pace", userPace);
                intent.putExtra("experienceLevel", userExperienceLevel);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParticipantActivityProfile.this, GeneralActivityLogin.class));
            }
        });

    }

    public void showUserData() {

        DatabaseReference specificUserReference = database.getInstance().getReference().child("users").child(userUsername);

        final String[] roleFromDatabase = new String[1];
        final String[] usernameFromDatabase = new String[1];
        final String[] ageFromDatabase = new String[1];
        final String[] paceFromDatabase = new String[1];
        final String[] experienceLevelFromDatabase = new String[1];

        specificUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    roleFromDatabase[0] = dataSnapshot.child("role").getValue(String.class);
                    usernameFromDatabase[0] = dataSnapshot.child("username").getValue(String.class);
                    ageFromDatabase[0] = dataSnapshot.child("age").getValue(String.class);
                    paceFromDatabase[0] = dataSnapshot.child("pace").getValue(String.class);
                    experienceLevelFromDatabase[0] = dataSnapshot.child("experienceLevel").getValue(String.class);

                    profileRole.setText("Role: " + roleFromDatabase[0]);
                    profileUsername.setText("Username: " + usernameFromDatabase[0]);
                    profileAge.setText("Age: " + ageFromDatabase[0] + " years old");
                    profilePace.setText("Pace: " + paceFromDatabase[0]);
                    profileExperienceLevel.setText("Experience Level: " + experienceLevelFromDatabase[0]);

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
}