package com.example.eastcyclingclub;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ClubActivityProfile extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView profileName, profileRole, profileUsername, phoneNumber, mainContact, instagramUsername, twitterUsername, facebookLink;
    Button editProfile, logout;
    String userUsername;
    ImageView clubPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_profile);

        // Getting the current user's username
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userUsername = null;
            } else {
                userUsername = extras.getString("userUsernameKey");
            }
        } else {
            userUsername = (String) savedInstanceState.getSerializable("userUsernameKey");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.event){
                Intent intent = new Intent(getApplicationContext(), ClubActivityEvents.class);
                intent.putExtra("userUsernameKey", userUsername);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }if (id == R.id.profile){
                return true;
            }
            return false;
        });

        // Initializing database
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(userUsername);

        profileName = findViewById(R.id.nameTextView);
        profileRole = findViewById(R.id.profileRole);
        profileUsername = findViewById(R.id.profileUsername);
        phoneNumber = findViewById(R.id.phoneNumberTextView);
        mainContact = findViewById(R.id.mainContactTextView);
        instagramUsername = findViewById(R.id.instagramUsernameTextView);
        twitterUsername = findViewById(R.id.twitterUsernameTextView);
        facebookLink = findViewById(R.id.facebookLinkTextView);


        editProfile = findViewById(R.id.editButton);
        logout = findViewById(R.id.logoutButton);

        showUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClubActivityEditProfile.class);
                intent.putExtra("userUsernameKey", userUsername);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(ClubActivityProfile.this, GeneralActivityLogin.class));
            }
        });

    }

    public void showUserData(){

        DatabaseReference specificUserReference = database.getInstance().getReference().child("users").child(userUsername);

        final String[] nameFromDatabase = new String[1];
        final String[] roleFromDatabase = new String[1];
        final String[] usernameFromDatabase = new String[1];
        final String[] phoneNumberFromDatabase = new String[1];
        final String[] mainContactFromDatabase = new String[1];
        final String[] instagramUsernameFromDatabase = new String[1];
        final String[] twitterUsernameFromDatabase = new String[1];
        final String[] facebookLinkFromDatabase = new String[1];
        final String[] profilePicturePathFromDatabase = new String[1];

        specificUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nameFromDatabase[0] = dataSnapshot.child("name").getValue(String.class);
                    roleFromDatabase[0] = dataSnapshot.child("role").getValue(String.class);
                    usernameFromDatabase[0] = dataSnapshot.child("username").getValue(String.class);
                    phoneNumberFromDatabase[0] = dataSnapshot.child("phoneNumber").getValue(String.class);
                    mainContactFromDatabase[0] = dataSnapshot.child("mainContact").getValue(String.class);
                    instagramUsernameFromDatabase[0] = dataSnapshot.child("instagramUsername").getValue(String.class);
                    twitterUsernameFromDatabase[0] = dataSnapshot.child("twitterUsername").getValue(String.class);
                    facebookLinkFromDatabase[0] = dataSnapshot.child("facebookLink").getValue(String.class);
                    profilePicturePathFromDatabase[0] = dataSnapshot.child("profilePicturePath").getValue(String.class);

                    profileName.setText("Name: " + nameFromDatabase[0]);
                    profileRole.setText("Role: " + roleFromDatabase[0]);
                    profileUsername.setText("Username: " + usernameFromDatabase[0]);
                    phoneNumber.setText("Phone number: " + phoneNumberFromDatabase[0]);

                    if (mainContactFromDatabase[0].isEmpty()) {
                        mainContact.setText("Main contact: None");
                    }
                    else {
                        mainContact.setText("Main contact: " + mainContactFromDatabase[0]);
                    }

                    if (instagramUsernameFromDatabase[0].isEmpty()) {
                        instagramUsername.setText("Instagram username: None");
                    }
                    else {
                        instagramUsername.setText("Instagram username: " + instagramUsernameFromDatabase[0]);
                    }

                    if (twitterUsernameFromDatabase[0].isEmpty()) {
                        twitterUsername.setText("Twitter username: None");
                    }
                    else {
                        twitterUsername.setText("Twitter username: " + twitterUsernameFromDatabase[0]);
                    }
                    if (facebookLinkFromDatabase[0].isEmpty()) {
                        facebookLink.setText("Facebook username: None");
                    }
                    else {
                        facebookLink.setText("Facebook link: " + facebookLinkFromDatabase[0]);
                    }

                    Log.d("TAG", "Profile values retrieved");
                }
                else {
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