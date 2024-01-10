package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListJoinedEvent extends ArrayAdapter<ClubHelperClassEvent> {

    private final Activity context;
    List<ClubHelperClassEvent> clubHelperClassEvents;

    public ParticipantListJoinedEvent(Activity context, List<ClubHelperClassEvent> clubHelperClassEvents) {
        super(context, R.layout.participant_activity_list_joined_event, clubHelperClassEvents);
        this.context = context;
        this.clubHelperClassEvents = clubHelperClassEvents;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_list_event, null, true);

        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewRatingNumber);
        TextView textViewEventName = (TextView) listViewItem.findViewById(R.id.textViewRatingName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewRatingComment);
        TextView textViewMaxParticipants = (TextView) listViewItem.findViewById(R.id.textViewMaxParticipants);

        ClubHelperClassEvent clubHelperClassEvent = clubHelperClassEvents.get(position);
        textViewEventType.setText("Event Type: " + clubHelperClassEvent.getEventType());
        textViewEventName.setText("Event Name: " + clubHelperClassEvent.getEventName());
        textViewDate.setText("Event Date: " + clubHelperClassEvent.getEventDate());
        textViewMaxParticipants.setText("Max Participants: " + clubHelperClassEvent.getMaxParticipants());

        return listViewItem;
    }
    public void searchDataList(ArrayList<ClubHelperClassEvent> searchList){
        clubHelperClassEvents = searchList;
        notifyDataSetChanged();
    }
}