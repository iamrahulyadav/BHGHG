package com.kandara.medicalapp.Model;

import java.util.ArrayList;

/**
 * Created by abina on 3/2/2018.
 */

public class DiscussionAnswer {
    String id;
    String option;
    String votes;
    ArrayList<String> votedBy;

    public DiscussionAnswer(String id) {
        this.id = id;
        votedBy=new ArrayList<>();
    }

    public void addVotedBy(String id){
        votedBy.add(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public ArrayList<String> getVotedBy() {
        return votedBy;
    }

    public void setVotedBy(ArrayList<String> votedBy) {
        this.votedBy = votedBy;
    }
}
