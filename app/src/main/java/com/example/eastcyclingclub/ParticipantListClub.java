package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListClub extends ArrayAdapter<GeneralHelperClassUser> {
    private Activity context;

    List<GeneralHelperClassUser> participantHelperClassClubs;

    public ParticipantListClub(Activity context, List<GeneralHelperClassUser> participantHelperClassClubs) {
        super(context, R.layout.participant_list_event_clubs, participantHelperClassClubs);
        this.context = context;
        this.participantHelperClassClubs = participantHelperClassClubs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.participant_list_event_clubs, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        GeneralHelperClassUser participantHelperClassClub = participantHelperClassClubs.get(position);
        textViewName.setText(participantHelperClassClub.getName());

        return listViewItem;
    }
}
