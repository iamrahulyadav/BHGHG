package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by abina on 5/3/2018.
 */

public class StudyRevision extends SugarRecord {


    @Unique
    long id;

    int isStudy;

    public int getIsStudy() {
        return isStudy;
    }

    public void setIsStudy(int isStudy) {
        this.isStudy = isStudy;
    }

    String QuestionId;


    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }
}
