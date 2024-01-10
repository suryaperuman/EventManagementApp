package com.example.eastcyclingclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClubActivityEvents extends AppCompatActivity {

    // Variables for the expanding button
    FloatingActionButton expandFAB, offerFAB;
    TextView offerText, editText;
    Boolean areAllFABsVisible;

    String userUsername;
    ListView listViewEvents;
    List<ClubHelperClassEvent> clubHelperClassEvents;
    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_events);

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
        bottomNavigationView.setSelectedItemId(R.id.event);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.event){
                return true;
            }if (id == R.id.profile){
                Intent intent = new Intent(getApplicationContext(), ClubActivityProfile.class);
                intent.putExtra("userUsernameKey", userUsername);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            return false;
        });

        // Floating action button for expanding to the offer event button and edit event button
        expandFAB = findViewById(R.id.expandMenuFAB);
        offerFAB = findViewById(R.id.offerEventFAB);

        offerText = findViewById(R.id.offerEventText);

        offerFAB.setVisibility(View.GONE);
        offerText.setVisibility(View.GONE);

        areAllFABsVisible = false;

        expandFAB.setOnClickListener(view -> {
            if (!areAllFABsVisible) {
                offerFAB.show();
                offerText.setVisibility(View.VISIBLE);

                areAllFABsVisible = true;
            }
            else {
                offerFAB.hide();
                offerText.setVisibility(View.GONE);

                areAllFABsVisible = false;
            }
        });

        // From current layout to creating event layout
        offerFAB.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ClubActivityAssociateEvent.class);
            intent.putExtra("userUsernameKey", userUsername);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });

        databaseEvents = FirebaseDatabase.getInstance().getReference("users").child(userUsername).child("events");
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);

        clubHelperClassEvents = new ArrayList<>();

       listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClubHelperClassEvent clubOwnerEventsHelperClass = clubHelperClassEvents.get(position);
                showUpdateDeleteDialog(clubOwnerEventsHelperClass.getEventType(),clubOwnerEventsHelperClass.getEventDate(), clubOwnerEventsHelperClass.getEventName() , clubOwnerEventsHelperClass.getMaxParticipants(), userUsername);
                return true;
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

                ClubListEvent eventAdapter = new ClubListEvent(ClubActivityEvents.this, clubHelperClassEvents);
                listViewEvents.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(String eventType, String date, String eventName, String maxParticipants, String userUsername) {
        Intent intent = new Intent(ClubActivityEvents.this, ClubActivityUpdateEvent.class);

        intent.putExtra("eventTypeKey", eventType);
        intent.putExtra("dateKey", date);
        intent.putExtra("eventNameKey", eventName);
        intent.putExtra("maxParticipantsKey", maxParticipants);
        intent.putExtra("userUsernameKey", userUsername);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        finish();
    }
}