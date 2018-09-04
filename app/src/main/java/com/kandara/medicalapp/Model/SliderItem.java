package com.kandara.medicalapp.Model;

/**
 * Created by abina on 8/11/2018.
 */

public class SliderItem {

    public interface ClickListener{
        void onClick();
    }

    String topic;
    String content;
    ClickListener clickListener;
    String photoUrl;

    public SliderItem(String topic, String content, ClickListener clickListener, String photoUrl) {
        this.topic = topic;
        this.content = content;
        this.clickListener = clickListener;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
