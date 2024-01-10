package com.example.eastcyclingclub;

public class ClubHelperClassEvent {
    String eventType, eventName, eventDate, maxParticipants;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(String maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ClubHelperClassEvent(String eventType, String eventName, String eventDate, String maxParticipants) {
        this.eventType = eventType;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.maxParticipants = maxParticipants;
    }

    public ClubHelperClassEvent() {}
}
