package com.example.eastcyclingclub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ParticipantListClubRating extends ArrayAdapter<ParticipantHelperClassRating> {
    private Activity context;

    List<ParticipantHelperClassRating> participantHelperClassRatings;

    public ParticipantListClubRating(Activity context, List<ParticipantHelperClassRating> participantHelperClassRatings) {
        super(context, R.layout.participant_list_club_ratings, participantHelperClassRatings);
        this.context = context;
        this.participantHelperClassRatings = participantHelperClassRatings;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.participant_list_club_ratings, null, true);

        TextView textViewRatingName = (TextView) listViewItem.findViewById(R.id.textViewRatingName);
        TextView textViewRatingNumber = (TextView) listViewItem.findViewById(R.id.textViewRatingNumber);
        TextView textViewRatingComment = (TextView) listViewItem.findViewById(R.id.textViewRatingComment);

        ParticipantHelperClassRating participantHelperClassRating = participantHelperClassRatings.get(position);
        textViewRatingName.setText(participantHelperClassRating.getRatingName());
        textViewRatingNumber.setText(participantHelperClassRating.getRatingNumber() + "/5");
        textViewRatingComment.setText(participantHelperClassRating.getRatingComment());

        return listViewItem;
    }
}
