package com.kandara.medicalapp.Model;

/**
 * Created by abina on 1/25/2018.
 */

public class News {

    String title;
    String description;
    String photo;

    public News(String title, String description, String photo) {
        this.title = title;
        this.description = description;
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
