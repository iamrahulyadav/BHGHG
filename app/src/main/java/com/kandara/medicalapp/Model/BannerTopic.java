package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by abina on 1/24/2018.
 */

public class BannerTopic extends SugarRecord{

    String title;
    String photoUrl;

    public BannerTopic(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public BannerTopic(String title, String photoUrl, String desc) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.desc = desc;
    }

    public String getPhotoUrl() {

        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    String desc;
}
