package com.kandara.medicalapp.Model;

/**
 * Created by abina on 5/6/2018.
 */

public class MCQAttempt {

    String mcqId;
    int rightAnswerPos;
    int userSelectedPos;

    public String getMcqId() {
        return mcqId;
    }

    public void setMcqId(String mcqId) {
        this.mcqId = mcqId;
    }

    public int getRightAnswerPos() {
        return rightAnswerPos;
    }

    public void setRightAnswerPos(int rightAnswerPos) {
        this.rightAnswerPos = rightAnswerPos;
    }

    public int getUserSelectedPos() {
        return userSelectedPos;
    }

    public void setUserSelectedPos(int userSelectedPos) {
        this.userSelectedPos = userSelectedPos;
    }
}
