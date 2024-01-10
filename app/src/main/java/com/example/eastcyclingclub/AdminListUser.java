package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdminListUser extends ArrayAdapter<GeneralHelperClassUser> {
    private Activity context;
    List<GeneralHelperClassUser> generalHelperClassUsers;

    public AdminListUser(Activity context, List<GeneralHelperClassUser> generalHelperClassUsers) {
        super(context, R.layout.admin_list_user, generalHelperClassUsers);
        this.context = context;
        this.generalHelperClassUsers = generalHelperClassUsers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.admin_list_user, null, true);

        TextView textViewName =  (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUserName);
        TextView textViewRole = (TextView) listViewItem.findViewById(R.id.textViewRole);
        TextView textViewEmail= (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewPassword = (TextView) listViewItem.findViewById(R.id.textViewPassword);

        GeneralHelperClassUser generalHelperClassUser = generalHelperClassUsers.get(position);
        textViewName.setText(generalHelperClassUser.getName());
        textViewUsername.setText("Username: " + generalHelperClassUser.getUsername());
        textViewRole.setText("Role: " + generalHelperClassUser.getRole());
        textViewEmail.setText("Email: " + generalHelperClassUser.getEmail());
        textViewPassword.setText("Password: " + generalHelperClassUser.getPassword());
        return listViewItem;
    }
}