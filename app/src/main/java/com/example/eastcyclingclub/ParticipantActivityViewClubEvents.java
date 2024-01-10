package com.example.eastcyclingclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParticipantActivityViewClubEvents extends AppCompatActivity {
    //FirebaseDatabase databaseEvents;
    DatabaseReference databaseEvents;
    TextView clubEventsTextView;
    ListView listView;
    Button returnToClubButton;
    String userUsername, userName, userRole, userPassword, userAge, userPace, userExperienceLevel,clubUsername;
    List<ClubHelperClassEvent> clubHelperClassEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_view_club_events);

        // Getting the current user's username
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                clubUsername = null;
                userUsername = null;
                userName = null;
                userRole = null;
                userPassword = null;
                userAge = null;
                userPace = null;
                userExperienceLevel = null;
            } else {
                clubUsername = extras.getString("clubUsername");
                userUsername = extras.getString("username");
                userName = extras.getString("name");
                userRole = extras.getString("role");
                userPassword = extras.getString("password");
                userAge = extras.getString("age");
                userPace = extras.getString("pace");
                userExperienceLevel = extras.getString("experienceLevel");
            }
        } else {
            clubUsername = (String) savedInstanceState.getSerializable("clubUsername");
            userUsername = (String) savedInstanceState.getSerializable("username");
            userName = (String) savedInstanceState.getSerializable("name");
            userRole = (String) savedInstanceState.getSerializable("role");
            userPassword = (String) savedInstanceState.getSerializable("password");
            userAge = (String) savedInstanceState.getSerializable("age");
            userPace = (String) savedInstanceState.getSerializable("pace");
            userExperienceLevel = (String) savedInstanceState.getSerializable("experienceLevel");
        }

        databaseEvents = FirebaseDatabase.getInstance().getReference("users").child(clubUsername).child("events");

        clubEventsTextView = findViewById(R.id.rateClubTextView);

        listView = (ListView) findViewById(R.id.listView);

        returnToClubButton = findViewById(R.id.returnToClubButton);

        clubHelperClassEvents = new ArrayList<>();

        // TODO: Implement participant enrolling in event functionality

        returnToClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParticipantActivityViewClub.class);
                intent.putExtra("clubUsername", clubUsername);
                intent.putExtra("username", userUsername);
                intent.putExtra("name", userName);
                intent.putExtra("password", userPassword);
                intent.putExtra("age", userUsername);
                intent.putExtra("pace", userName);
                intent.putExtra("experienceLevel", userRole);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clubHelperClassEvents.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ClubHelperClassEvent clubHelperClassEvent = postSnapshot.getValue(ClubHelperClassEvent.class);
                    clubHelperClassEvents.add(clubHelperClassEvent);
                }

                ClubListEvent eventAdapter = new ClubListEvent(ParticipantActivityViewClubEvents.this, clubHelperClassEvents);
                listView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
