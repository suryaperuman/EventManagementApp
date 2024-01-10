package com.example.eastcyclingclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivityEventType extends AppCompatActivity{
    FirebaseDatabase database;
    DatabaseReference reference;
    Button returnToEventsBTN;
    EditText eventInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_edit_event_types);

        // Dropdown menu for the event type
        Spinner eventType = findViewById(R.id.editEventType);
        ArrayAdapter<CharSequence> eventAdapter  = ArrayAdapter.createFromResource(this, R.array.EventOptions, R.layout.admin_spinner_event_type);
        eventAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        eventType.setAdapter(eventAdapter);

        // Setting the event information to "preset" information
        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                eventInformation = (EditText)findViewById(R.id.eventInformationEditText);

                // Replace strings with database strings * * * * *
                switch (selectedItem) {
                    case "Time Trial":
                        eventInformation.setText("Time trial description", TextView.BufferType.EDITABLE);
                        break;
                    case "Hill Climb":
                        eventInformation.setText("Hill climb description", TextView.BufferType.EDITABLE);
                        break;
                    case "Road Stage Race":
                        eventInformation.setText("Road stage race description", TextView.BufferType.EDITABLE);
                        break;
                    case "Road Race":
                        eventInformation.setText("Road race description", TextView.BufferType.EDITABLE);
                        break;
                    case "Group Rides":
                        eventInformation.setText("Group Rides description", TextView.BufferType.EDITABLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
                // Method must be written but nothing has to happen
            }
        });

        // Return to events button
        returnToEventsBTN = findViewById(R.id.returnToEventsButton);
        returnToEventsBTN.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminActivityEvents.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });
    }
}
