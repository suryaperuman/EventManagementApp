package com.example.eastcyclingclub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivityUpdateEvent extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView eventType;
    Button editEventBTN, deleteEventBTN, returnToEventsBTN;
    EditText minAge, maxAge, pace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_update_event);

        String eventTypeValue, minimumAgeValue, maximumAgeValue, paceValue, difficultyValue;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                eventTypeValue = null;
                difficultyValue = null;
                minimumAgeValue = null;
                maximumAgeValue = null;
                paceValue = null;
            } else {
                eventTypeValue = extras.getString("eventTypeKey");
                difficultyValue = extras.getString("difficultyKey");
                minimumAgeValue = extras.getString("minimumAgeKey");
                maximumAgeValue = extras.getString("maximumAgeKey");
                paceValue = extras.getString("paceKey");
            }

        } else {
            eventTypeValue = (String) savedInstanceState.getSerializable("eventTypeKey");
            difficultyValue = (String) savedInstanceState.getSerializable("difficultyKey");
            minimumAgeValue = (String) savedInstanceState.getSerializable("minimumAgeKey");
            maximumAgeValue = (String) savedInstanceState.getSerializable("maximumAgeKey");
            paceValue = (String) savedInstanceState.getSerializable("paceKey");
        }


        //Dropdown menu for difficulty level selection
        Spinner difficultyLevel = findViewById(R.id.difficultyLevel);
        ArrayAdapter<CharSequence> difficultyAdapter  = ArrayAdapter.createFromResource(this, R.array.ExperienceLevelOptions, R.layout.admin_spinner_event_type);
        difficultyAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        difficultyLevel.setAdapter(difficultyAdapter);

        difficultyLevel.setSelection(difficultyAdapter.getPosition(difficultyValue));

        editEventBTN = findViewById(R.id.editEventButton);
        deleteEventBTN = findViewById(R.id.deleteEventButton);

        eventType = (TextView) findViewById(R.id.eventType);
        eventType.setText(eventTypeValue);

        //setting fields to previously entered values for editing
        if (minimumAgeValue != null) {
            minAge = findViewById(R.id.minAge);
            minAge.setText(minimumAgeValue);
        }
        if (maximumAgeValue != null) {
            maxAge = findViewById(R.id.maxAge);
            maxAge.setText(maximumAgeValue);
        }
        if (paceValue != null) {
            pace = findViewById(R.id.pace);
            pace.setText(paceValue);
        }

        editEventBTN.setOnClickListener(view -> {

            String minAgeText = minAge.getText().toString();
            String maxAgeText = maxAge.getText().toString();
            String paceText = pace.getText().toString();
            String selectedDifficultyLevel = difficultyLevel.getSelectedItem().toString();

            if (!selectedDifficultyLevel.equals("Select Your Experience Level") && !paceText.isEmpty() && (!minAgeText.isEmpty() || maxAgeText.isEmpty())) {
                updateProduct(eventTypeValue, selectedDifficultyLevel, minAgeText, maxAgeText, paceText);
                Toast.makeText(AdminActivityUpdateEvent.this, "Event updated", Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(getApplicationContext(), AdminActivityEvents.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivityUpdateEvent.this);
                builder.setTitle("Try again!");
                builder.setMessage("Ensure that difficulty level and pace are specified");
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
                deleteProduct(eventTypeValue);
                Toast.makeText(AdminActivityUpdateEvent.this, "Event Deleted", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(getApplicationContext(), AdminActivityEvents.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }
        });

        // Return to events button
        returnToEventsBTN = findViewById(R.id.returnToEventsButton);
        returnToEventsBTN.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), AdminActivityEvents.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            finish();
        });
    }

    private void updateProduct(String eventType, String difficultyLevel, String minimumAge, String maximumAge, String pace) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(eventType);

        AdminHelperClassEvent adminHelperClassEvent = new AdminHelperClassEvent(eventType,difficultyLevel, minimumAge, pace, maximumAge);
        dR.setValue(adminHelperClassEvent);
        Toast.makeText(getApplicationContext(), "Event Type Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteProduct(String eventType) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("events").child(eventType);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Event type deleted", Toast.LENGTH_LONG).show();
    }
}
