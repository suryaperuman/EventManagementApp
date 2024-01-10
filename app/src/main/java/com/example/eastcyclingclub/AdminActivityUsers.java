package com.example.eastcyclingclub;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class AdminActivityUsers extends AppCompatActivity {


    ListView listViewUsers;

    List<GeneralHelperClassUser> generalHelperClassUsers;

    DatabaseReference databaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_users);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.account);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id==R.id.event){
                startActivity(new Intent(getApplicationContext(), AdminActivityEvents.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }if (id==R.id.profile){
                startActivity(new Intent(getApplicationContext(), AdminActivityProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
            }if (id==R.id.account){
                return true;
            }
            return false;
        });


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);


        generalHelperClassUsers = new ArrayList<>();

        //adding an onclicklistener to button

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                GeneralHelperClassUser generalHelperClassUser = generalHelperClassUsers.get(i);
                showUpdateDeleteDialog(generalHelperClassUser.getName(), generalHelperClassUser.getEmail(), generalHelperClassUser.getUsername(), generalHelperClassUser.getPassword(), generalHelperClassUser.getRole());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                generalHelperClassUsers.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting users
                    GeneralHelperClassUser generalHelperClassUser = postSnapshot.getValue(GeneralHelperClassUser.class);
                    //adding users to the list
                    generalHelperClassUsers.add(generalHelperClassUser);
                }

                //creating adapter
                AdminListUser userAdapter = new AdminListUser(AdminActivityUsers.this, generalHelperClassUsers);
                //attaching adapter to the listview
                listViewUsers.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDeleteDialog(String name, String email, String username, String password , String role) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.admin_activity_update_user, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        editTextName.setText(name);
        final EditText editTextEmail = (EditText) dialogView.findViewById(R.id.editTextEmail);
        editTextEmail.setText(email);
        final EditText editTextPassword = (EditText) dialogView.findViewById(R.id.editTextPassword);
        editTextPassword.setText(password);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);

        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (name.matches("")||email.matches("")||password.matches("")){
                    Toast.makeText(getApplicationContext(), "Cannot Update with empty fields", Toast.LENGTH_LONG).show();
                }else{
                    updateProduct(name, email, username, password, role);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(username);
                b.dismiss();
            }
        });
    }

    private void updateProduct(String name, String email,String username, String password, String role) {
        //getting the specified users reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username);
        //updating users
        GeneralHelperClassUser generalHelperClassUser = new GeneralHelperClassUser(name, email, username, password, role);
        dR.setValue(generalHelperClassUser);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteProduct(String username) {
        //getting the specified users reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(username);
        //removing users
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
    }
}