package com.example.eastcyclingclub;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ClubActivityAssociateEvent extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    Spinner eventType;
    ArrayAdapter<CharSequence> eventAdapter;
    ArrayList<CharSequence> eventTypeList;

    Button createEventTypeBTN, returnToEventsBTN;

    TextView eventDateText;
    EditText maxParticipantsText;
    String userUsername;
    EditText eventNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_associate_event);

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

        eventDateText = findViewById(R.id.eventDate);
        maxParticipantsText = findViewById(R.id.numParticipants);
        createEventTypeBTN = findViewById(R.id.createEventTypeButtton);
        eventNameText = findViewById(R.id.eventName);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("events");

        // Dropdown menu for the event type, pulling from firebase depending on which are being offered or not
        eventType = findViewById(R.id.pickEventType);
        eventTypeList = new ArrayList<>();
        eventAdapter  = new ArrayAdapter<CharSequence>(ClubActivityAssociateEvent.this, R.layout.admin_spinner_event_type, eventTypeList);
        eventAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        eventType.setAdapter(eventAdapter);
        showData();

        // Return to events button
        returnToEventsBTN = findViewById(R.id.returnToEventsButton);
        returnToEventsBTN.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ClubActivityEvents.class);
            intent.putExtra("userUsernameKey", userUsername);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });


        //logic for showing a date picker dialog and selecting date that event will be held
        eventDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(ClubActivityAssociateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eventDateText.setText(intMonthToWord(month)+ " " + String.valueOf(dayOfMonth) + " " + String.valueOf(year));
                    }
                }, year, month, day);

                dialog.show();
            }
        });


        createEventTypeBTN.setOnClickListener(view -> {

            //grab information from user input

            String eventDate = eventDateText.getText().toString();
            String maxParticipants = maxParticipantsText.getText().toString();
            String selectedEventType = eventType.getSelectedItem().toString();
            String eventName = eventNameText.getText().toString();

            //TODO -> edit the following logic below so that each event created of an event type is assigned to the CYCLING
            //TODO -> CLUB OWNER'S ACCOUNT. Make sure all fields are specific and valid

            // Checks if at least one option is entered: if so, allows event creation, if not, outputs warning message

            boolean allFieldsEmpty =
                    ( eventDate.length() == 0  )
                            && ( maxParticipants.length() == 0 )
                            && ( eventName.length() == 0 );


            if (!(eventDate.isEmpty() || maxParticipants.isEmpty() || eventName.isEmpty())) {

                DatabaseReference specificUserEventsReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUsername).child("events");
                DatabaseReference clubOwnerEvents = specificUserEventsReference.child(eventName);

                ClubHelperClassEvent helper = new ClubHelperClassEvent(selectedEventType, eventName, eventDate, maxParticipants);

                specificUserEventsReference.child(eventName).setValue(helper);

                Toast.makeText(ClubActivityAssociateEvent.this, "Event Created Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ClubActivityEvents.class);
                intent.putExtra("userUsernameKey", userUsername);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClubActivityAssociateEvent.this);
                builder.setTitle("Try again!");
                StringBuilder message = new StringBuilder("Please make sure you have the following entered:");

                if (eventName.isEmpty()) {
                    message.append("\n\t- An event name");
                }

                if (eventDate.isEmpty()) {
                    message.append("\n\t- An event date");
                }

                if (maxParticipants.isEmpty()) {
                    message.append("\n\t- A maximum number of participants allowed");
                }

                builder.setMessage(message);

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
    private String intMonthToWord(int x) {
        if (x == 0) {
            return "January";
        }
        if (x == 1) {
            return "February";
        }
        if (x == 2) {
            return "March";
        }
        if (x == 3) {
            return "April";
        }
        if (x == 4) {
            return "May";
        }
        if (x == 5) {
            return "June";
        }
        if (x == 6) {
            return "July";
        }
        if (x == 7) {
            return "August";
        }
        if (x == 8) {
            return "September";
        }
        if (x == 9) {
            return "October";
        }
        if (x == 10) {
            return "November";
        }
        if (x == 11) {
            return "December";
        }
        return null;
    }

    private void showData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    AdminHelperClassEvent adminHelperClassEvent = item.getValue(AdminHelperClassEvent.class);
                    eventTypeList.add(adminHelperClassEvent.getEventType());
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}