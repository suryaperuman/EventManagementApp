package com.example.eastcyclingclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminActivityEvents extends AppCompatActivity {

    // Variables for the expanding button
    FloatingActionButton expandFAB, offerFAB;

    TextView offerText, editText;

    Boolean areAllFABsVisible;

    ListView listViewEvents;

    List<AdminHelperClassEvent> adminHelperClassEvents;

    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_events);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.event);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id==R.id.event){
                return true;
            }if (id==R.id.profile){
                startActivity(new Intent(getApplicationContext(), AdminActivityProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }if (id==R.id.account){
                startActivity(new Intent(getApplicationContext(), AdminActivityUsers.class));
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
                // editFAB.show();
                offerText.setVisibility(View.VISIBLE);
                //editText.setVisibility(View.VISIBLE);

                areAllFABsVisible = true;
            }
            else {
                offerFAB.hide();
                // editFAB.hide();
                offerText.setVisibility(View.GONE);
                //editText.setVisibility(View.GONE);

                areAllFABsVisible = false;
            }
        });

        // From current layout to creating event layout
        offerFAB.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminActivityCreateEvent.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });

        databaseEvents = FirebaseDatabase.getInstance().getReference("events");
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);

        adminHelperClassEvents = new ArrayList<>();

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AdminHelperClassEvent adminHelperClassEvent = adminHelperClassEvents.get(position);
                showUpdateDeleteDialog(adminHelperClassEvent.getEventType(), adminHelperClassEvent.getDifficulty() , adminHelperClassEvent.getMinimumAge(), adminHelperClassEvent.getMaximumAge(), adminHelperClassEvent.getPace());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminHelperClassEvents.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AdminHelperClassEvent adminHelperClassEvent = postSnapshot.getValue(AdminHelperClassEvent.class);
                    adminHelperClassEvents.add(adminHelperClassEvent);
                }

                AdminListEvent eventAdapter = new AdminListEvent(AdminActivityEvents.this, adminHelperClassEvents);
                listViewEvents.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDeleteDialog(String eventType, String difficultyLevel, String minimumAge, String maximumAge, String pace) {
        Intent intent = new Intent(AdminActivityEvents.this, AdminActivityUpdateEvent.class);

        intent.putExtra("eventTypeKey", eventType);
        intent.putExtra("difficultyKey", difficultyLevel);
        intent.putExtra("minimumAgeKey", minimumAge);
        intent.putExtra("maximumAgeKey", maximumAge);
        intent.putExtra("paceKey", pace);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        finish();
    }
}