package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdminListEvent extends ArrayAdapter<AdminHelperClassEvent>{
    private Activity context;
    List<AdminHelperClassEvent> adminHelperClassEvents;

    public AdminListEvent(Activity context, List<AdminHelperClassEvent> adminHelperClassEvents) {
        super(context, R.layout.admin_list_event, adminHelperClassEvents);
        this.context = context;
        this.adminHelperClassEvents = adminHelperClassEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.admin_list_event, null, true);

        TextView textViewEventType = (TextView) listViewItem.findViewById(R.id.textViewRatingNumber);
        TextView textViewDifficulty = (TextView) listViewItem.findViewById(R.id.textViewDifficulty);
        TextView textViewMinimumAge = (TextView) listViewItem.findViewById(R.id.textViewMinimumAge);
        TextView textViewMaximumAge = (TextView) listViewItem.findViewById(R.id.textViewMaximumAge);
        TextView textViewPace = (TextView) listViewItem.findViewById(R.id.textViewPace);

        AdminHelperClassEvent adminHelperClassEvent = adminHelperClassEvents.get(position);
        textViewEventType.setText("Event Type: " + adminHelperClassEvent.getEventType());
        textViewDifficulty.setText("Difficulty: " + adminHelperClassEvent.getDifficulty());
        textViewMinimumAge.setText("Minimum Age: " + adminHelperClassEvent.getMinimumAge());
        textViewMaximumAge.setText("Maximum Age: " + adminHelperClassEvent.getMaximumAge());
        textViewPace.setText("Pace Needed: " + adminHelperClassEvent.getPace());

        return listViewItem;
    }
}
