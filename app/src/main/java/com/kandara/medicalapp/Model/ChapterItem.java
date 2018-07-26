package com.kandara.medicalapp.Model;

/**
 * Created by abina on 4/22/2018.
 */

public class ChapterItem {


    String chaptername;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;

    public String getChaptername() {
        return chaptername;
    }

    public void setChaptername(String chaptername) {
        this.chaptername = chaptername;
    }
}
