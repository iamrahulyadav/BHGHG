package com.kandara.medicalapp.Model;

import java.util.ArrayList;

/**
 * Created by abina on 5/27/2018.
 */

public class SubchapterList {

    String subChapterName;

    ArrayList<Study> studyArrayList;

    public SubchapterList(String subChapterName) {
        this.subChapterName = subChapterName;
        studyArrayList=new ArrayList<>();
    }

    public String getSubChapterName() {
        return subChapterName;
    }

    public void setSubChapterName(String subChapterName) {
        this.subChapterName = subChapterName;
    }

    public ArrayList<Study> getStudyArrayList() {
        return studyArrayList;
    }

    public void addStudy(Study study){
        studyArrayList.add(study);
    }


}
