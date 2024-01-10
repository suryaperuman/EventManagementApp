package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ParticipantListEvent extends ArrayAdapter<ParticipantHelperClassEvent> {
    private Activity context;
    List<ParticipantHelperClassEvent> participantHelperClassEvents;

    public ParticipantListEvent(Activity context, List<ParticipantHelperClassEvent> participantHelperClassEvents) {
        super(context, R.layout.participant_list_event, participantHelperClassEvents);
        this.context = context;
        this.participantHelperClassEvents = participantHelperClassEvents;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.participant_list_event, null, true);

        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewRatingNumber);
        TextView textViewEventName = (TextView) listViewItem.findViewById(R.id.textViewRatingName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewRatingComment);
        TextView textViewMaxParticipants = (TextView) listViewItem.findViewById(R.id.textViewMaxParticipants);

        ParticipantHelperClassEvent participantHelperClassEvent = participantHelperClassEvents.get(position);
        textViewEventName.setText(participantHelperClassEvent.getEventName());
        textViewEventType.setText("Event type: " + participantHelperClassEvent.getEventType());
        textViewDate.setText("Event date: " + participantHelperClassEvent.getEventDate());
        textViewMaxParticipants.setText("Max participants: " + participantHelperClassEvent.getMaxParticipants());

        return listViewItem;
    }
    public void searchDataList(ArrayList<ParticipantHelperClassEvent> searchList){
        participantHelperClassEvents = searchList;
        notifyDataSetChanged();
    }
}
