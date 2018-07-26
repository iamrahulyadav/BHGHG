package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by abina on 1/24/2018.
 */

public class SubTopic extends SugarRecord{


    @Unique
    long id;

    public void setId(long id) {
        this.id = id;
    }

    String title;
    String topic;

    public SubTopic(String title, String topic) {
        this.title = title;
        this.topic = topic;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    @Override
    public boolean equals(Object obj) {
        SubTopic newTopic= (SubTopic) obj;
        if(newTopic.getTitle().equalsIgnoreCase(getTitle()) && newTopic.getTopic().equalsIgnoreCase(getTopic())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return "Title : "+getTitle()+"\nTopic : "+getTopic();
    }
}
