package com.kandara.medicalapp.Model;

/**
 * Created by abina on 8/2/2018.
 */

public class TeamMemberItem {

    String name;
    boolean isDevelopment;
    int photo;
    String position;
    String email;

    public TeamMemberItem(String name, boolean isDevelopment, int photo, String position, String email) {
        this.name = name;
        this.isDevelopment = isDevelopment;
        this.photo = photo;
        this.position = position;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDevelopment() {
        return isDevelopment;
    }

    public void setDevelopment(boolean development) {
        isDevelopment = development;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
