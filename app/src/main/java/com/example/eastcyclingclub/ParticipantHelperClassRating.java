package com.example.eastcyclingclub;

public class ParticipantHelperClassRating {
    String ratingName, ratingNumber, ratingComment;

    public String getRatingName() {
        return ratingName;
    }
    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public String getRatingNumber() {
        return ratingNumber;
    }
    public void setRatingNumber(String ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public ParticipantHelperClassRating(String ratingName, String ratingNumber, String ratingComment) {
        this.ratingName = ratingName;
        this.ratingNumber = ratingNumber;
        this.ratingComment = ratingComment;
    }

    public ParticipantHelperClassRating() {}
}
