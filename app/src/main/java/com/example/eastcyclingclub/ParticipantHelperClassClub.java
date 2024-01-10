package com.example.eastcyclingclub;

public class ParticipantHelperClassClub {
    String name, email, username, password, phoneNumber, mainContact, instagramUsername, twitterUsername, facebookLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMainContact() {
        return mainContact;
    }

    public void setMainContact(String mainContact) {
        this.mainContact = mainContact;
    }

    public String getInstagram() {
        return instagramUsername;
    }

    public void setInstagram(String instagramUsername) {
        this.instagramUsername = instagramUsername;
    }

    public String getTwitter() {
        return twitterUsername;
    }

    public void setTwitter(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public String getFacebook() {
        return facebookLink;
    }

    public void setFacebook(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public ParticipantHelperClassClub(String name, String email, String username, String password, String phoneNumber, String mainContact, String instagramUsername, String twitterUsername, String facebookLink) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.mainContact = mainContact;
        this.instagramUsername = instagramUsername;
        this.twitterUsername = twitterUsername;
        this.facebookLink = facebookLink;
    }

    public ParticipantHelperClassClub() {}
}
