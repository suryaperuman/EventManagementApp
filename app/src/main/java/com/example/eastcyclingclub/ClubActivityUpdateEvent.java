package com.example.eastcyclingclub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ClubActivityUpdateEvent extends AppCompatActivity {

    TextView date;
    Button editEventBTN, deleteEventBTN, returnToEventsBTN;
    EditText eventName, maxParticipants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity_update_event);

        String dateValue, eventNameValue, maxParticipantsValue, userUsernameValue, eventTypeValue;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                eventTypeValue = null;
                dateValue = null;
                eventNameValue = null;
                maxParticipantsValue = null;
                userUsernameValue = null;
            } else {
                eventTypeValue = extras.getString("eventTypeKey");
                dateValue = extras.getString("dateKey");
                eventNameValue = extras.getString("eventNameKey");
                maxParticipantsValue = extras.getString("maxParticipantsKey");
                userUsernameValue = extras.getString("userUsernameKey");
            }
        } else {
            eventTypeValue = (String) savedInstanceState.getSerializable("dateKey");
            dateValue = (String) savedInstanceState.getSerializable("dateKey");
            eventNameValue = (String) savedInstanceState.getSerializable("eventNameKey");
            maxParticipantsValue = (String) savedInstanceState.getSerializable("maxParticipantsKey");
            userUsernameValue = (String) savedInstanceState.getSerializable("userUsernameKey");
        }


        editEventBTN = findViewById(R.id.editEventButton);
        deleteEventBTN = findViewById(R.id.deleteEventButton);

        date = (TextView) findViewById(R.id.date);
        date.setText(dateValue);

        //setting fields to previously entered values for editing
        if (eventNameValue != null) {
            eventName = findViewById(R.id.eventName);
            eventName.setText(eventNameValue);
        }
        if (maxParticipantsValue != null) {
            maxParticipants = findViewById(R.id.maxParticipants);
            maxParticipants.setText(maxParticipantsValue);
        }

        //logic for showing a date picker dialog and selecting date that event will be held
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(ClubActivityUpdateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(intMonthToWord(month)+ " " + String.valueOf(dayOfMonth) + " " + String.valueOf(year));
                    }
                }, year, month, day);

                dialog.show();
            }
        });

        editEventBTN.setOnClickListener(view -> {

            String dateText = date.getText().toString();
            String oldEventName = eventNameValue;
            String newEventName = eventName.getText().toString();
            String maxParticipantsText = maxParticipants.getText().toString();

            if (dateText.equals(dateValue) && (newEventName.equals(oldEventName)) && (maxParticipantsText.equals(maxParticipantsValue)))  {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClubActivityUpdateEvent.this);
                builder.setTitle("Try Again!");
                builder.setMessage("No Edits Detected");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            // Checks if all options are selected: if so, allows event creation, if not, outputs warning message
            else if (!dateText.isEmpty() && !newEventName.isEmpty() && !maxParticipantsText.isEmpty()) {
                updateProduct(eventTypeValue, newEventName, oldEventName, maxParticipantsText, dateText, userUsernameValue);
                Toast.makeText(ClubActivityUpdateEvent.this, "Event Updated", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(getApplicationContext(), ClubActivityEvents.class);
                intent2.putExtra("userUsernameKey", userUsernameValue);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ClubActivityUpdateEvent.this);
                builder.setTitle("Try again!");
                StringBuilder message = new StringBuilder("Please make sure you have the following entered:");

                if (newEventName.isEmpty()) {
                    message.append("\n\t- An event name");
                }

                if (dateText.isEmpty()) {
                    message.append("\n\t- An event date");
                }

                if (maxParticipantsText.isEmpty()) {
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

        deleteEventBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(userUsernameValue, eventNameValue);
                Toast.makeText(ClubActivityUpdateEvent.this, "Event Deleted", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(getApplicationContext(), ClubActivityEvents.class);
                intent3.putExtra("userUsernameKey", userUsernameValue);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });

        // Return to events button
        returnToEventsBTN = findViewById(R.id.returnToEventsButton);
        returnToEventsBTN.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), ClubActivityEvents.class);
            intent1.putExtra("userUsernameKey", userUsernameValue);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });
    }

    private void updateProduct(String eventType, String newEventName, String oldEventName, String maxParticipants, String date, String userUsername) {
        DatabaseReference specificUserEventsReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUsername).child("events");
        if (newEventName.equals(oldEventName)){
            ClubHelperClassEvent clubHelperClassEvent = new ClubHelperClassEvent(eventType, newEventName, date, maxParticipants);
            specificUserEventsReference.child(newEventName).setValue(clubHelperClassEvent);
            Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
        }else{
            deleteProduct(userUsername, oldEventName);
            ClubHelperClassEvent clubHelperClassEvent = new ClubHelperClassEvent(eventType, newEventName, date, maxParticipants);
            specificUserEventsReference.child(newEventName).setValue(clubHelperClassEvent);
            Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteProduct(String userUsername, String eventName) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(userUsername).child("events").child(eventName);
        dR.removeValue();
        // Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_LONG).show();
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
}