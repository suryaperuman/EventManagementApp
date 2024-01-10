package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ClubListEvent extends ArrayAdapter<ClubHelperClassEvent> {
    private Activity context;
    List<ClubHelperClassEvent> clubHelperClassEvents;

    public ClubListEvent(Activity context, List<ClubHelperClassEvent> clubHelperClassEvents) {
        super(context, R.layout.club_list_event, clubHelperClassEvents);
        this.context = context;
        this.clubHelperClassEvents = clubHelperClassEvents;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.club_list_event, null, true);

        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewRatingNumber);
        TextView textViewEventName = (TextView) listViewItem.findViewById(R.id.textViewRatingName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewRatingComment);
        TextView textViewMaxParticipants = (TextView) listViewItem.findViewById(R.id.textViewMaxParticipants);

        ClubHelperClassEvent clubHelperClassEvent = clubHelperClassEvents.get(position);
        textViewEventType.setText("Event type: " + clubHelperClassEvent.getEventType());
        textViewEventName.setText("Event name: " + clubHelperClassEvent.getEventName());
        textViewDate.setText("Event date: " + clubHelperClassEvent.getEventDate());
        textViewMaxParticipants.setText("Max participants: " + clubHelperClassEvent.getMaxParticipants());

        return listViewItem;
    }
    public void searchDataList(ArrayList<ClubHelperClassEvent> searchList){
        clubHelperClassEvents = searchList;
        notifyDataSetChanged();
    }
}
