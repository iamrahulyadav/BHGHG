package com.kandara.medicalapp.Model;

/**
 * Created by abina on 1/25/2018.
 */

public class LeaderBoardModel {

    int position;
    String name;
    int points;
    String profilePhoto;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LeaderBoardModel(String name, int points, String profilePhoto, int position) {
        this.name = name;
        this.position=position;
        this.points = points;
        this.profilePhoto=profilePhoto;
    }

    public String getName() {

        return name;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
