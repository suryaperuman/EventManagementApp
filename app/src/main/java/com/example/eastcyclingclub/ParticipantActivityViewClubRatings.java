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

public class ParticipantActivityViewClubRatings extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference databaseRatings;
    TextView averageRatingTextView;
    ListView ratingsListView;
    Button returnToClubButton;
    String userUsername, userName, userRole, userPassword, userAge, userPace, userExperienceLevel,clubUsername;
    double averageRatingNumber;
    List<ParticipantHelperClassRating> participantHelperClassRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_activity_view_club_ratings);

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

        databaseRatings = FirebaseDatabase.getInstance().getReference("users").child(clubUsername).child("ratings");

        averageRatingTextView = findViewById(R.id.averageRatingTextView);

        returnToClubButton = findViewById(R.id.returnToClubButton);

        participantHelperClassRatings = new ArrayList<>();

        ratingsListView = findViewById(R.id.ratingsListView);

        databaseRatings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantHelperClassRatings.clear();

                double totalRating = 0;
                int numRatings = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ParticipantHelperClassRating participantHelperClassRating = postSnapshot.getValue(ParticipantHelperClassRating.class);
                    participantHelperClassRatings.add(participantHelperClassRating);

                    // Assuming your ParticipantHelperClassRating has a method to get the rating
                    if ( participantHelperClassRating != null ) {
                        totalRating += Double.parseDouble(participantHelperClassRating.getRatingNumber());
                        numRatings++;
                    }
                }

                if (numRatings > 0) {
                    // Calculate the average
                    averageRatingNumber = totalRating / numRatings;
                    averageRatingTextView.setText(String.format("Average rating: %.2f", averageRatingNumber) + " (" + numRatings + ")");
                } else {
                    averageRatingTextView.setText("No ratings yet");
                }

                // Update the ListView with the new data
                ParticipantListClubRating ratingAdapter = new ParticipantListClubRating(ParticipantActivityViewClubRatings.this, participantHelperClassRatings);
                ratingsListView.setAdapter(ratingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        DatabaseReference databaseRatings = FirebaseDatabase.getInstance().getReference("users").child(clubUsername).child("ratings");

        databaseRatings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantHelperClassRatings.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ParticipantHelperClassRating participantHelperClassRating = postSnapshot.getValue(ParticipantHelperClassRating.class);
                    participantHelperClassRatings.add(participantHelperClassRating);
                }

                ParticipantListClubRating ratingAdapter = new ParticipantListClubRating(ParticipantActivityViewClubRatings.this, participantHelperClassRatings);
                ratingsListView.setAdapter(ratingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
