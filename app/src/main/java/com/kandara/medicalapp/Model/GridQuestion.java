package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by abina on 5/2/2018.
 */

public class GridQuestion extends SugarRecord {

    @Unique
    long id;

    String QuestionId;

    boolean isCorrect;

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }
}
