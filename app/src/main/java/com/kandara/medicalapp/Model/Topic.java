package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by abina on 1/24/2018.
 */

public class Topic extends SugarRecord{


    @Unique
    long id;


    public void setId(long id) {
        this.id = id;
    }

    String title;

    public Topic(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        Topic newTopic= (Topic) obj;
        if(newTopic.getTitle().equalsIgnoreCase(getTitle())){
            return true;
        }else{
            return false;
        }
    }


    @Override
    public String toString() {
        return "Title : "+getTitle();
    }
}
