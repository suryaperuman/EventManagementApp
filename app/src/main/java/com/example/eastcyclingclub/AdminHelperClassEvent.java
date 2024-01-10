package com.example.eastcyclingclub;

public class AdminHelperClassEvent {
    String eventType, difficultyLevel, minimumAge, maximumAge, pace;

    public String getEventType() {return eventType;}
    public void setEventType(String eventType) {this.eventType = eventType;}
    public String getDifficulty() {return difficultyLevel;}
    public void setDifficulty(String difficultyLevel) {this.difficultyLevel = difficultyLevel;}

    public String getMinimumAge() {
        if ((minimumAge == null) || (minimumAge.isEmpty())) {
            return "No Requirement Set";
        }

        return minimumAge;
    }
    public void setMinimumAge(String minimumAge) {this.minimumAge = minimumAge;}

    public String getMaximumAge() {
        if ((maximumAge == null) || (maximumAge.isEmpty())) {
            return "No Requirement Set";
        }

        return maximumAge;
    }
    public void setMaximumAge(String maximumAge) {this.maximumAge = maximumAge;}

    public String getPace() {
        if ((pace == null) || (pace.isEmpty())) {
            return "No Requirement Set";
        }
        return pace ;
    }
    public void setPace(String pace) {this.pace = pace;}


    public AdminHelperClassEvent(String eventType, String difficultyLevel, String minimumAge, String pace, String maximumAge) {
        this.eventType = eventType;
        this.difficultyLevel = difficultyLevel;
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.pace = pace;
    }
    public AdminHelperClassEvent() {}
}
