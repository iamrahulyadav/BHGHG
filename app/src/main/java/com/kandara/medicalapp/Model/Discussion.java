package com.kandara.medicalapp.Model;

import java.util.ArrayList;

/**
 * Created by abina on 1/28/2018.
 */

public class Discussion {

    ArrayList<DiscussionAnswer> answers;
    ArrayList<String> favoritedBy;
    String id;
    String uid;
    String question;
    boolean type;


    public Discussion(String id) {
        this.id = id;
        answers=new ArrayList<>();
        favoritedBy=new ArrayList<>();
    }

    public void addAnswer(DiscussionAnswer discussionAnswer){
        answers.add(discussionAnswer);
    }

    public void addFavoritedPeopleId(String uid){
        favoritedBy.add(uid);
    }

    public ArrayList<DiscussionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<DiscussionAnswer> answers) {
        this.answers = answers;
    }

    public ArrayList<String> getFavoritedBy() {
        return favoritedBy;
    }

    public void setFavoritedBy(ArrayList<String> favoritedBy) {
        this.favoritedBy = favoritedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
