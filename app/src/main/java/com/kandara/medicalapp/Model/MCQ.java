package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.ArrayList;

/**
 * Created by abina on 2/6/2018.
 */

public class MCQ extends SugarRecord {


    @Unique
    long id;

    String imageUrl;
    String question;
    String rightAnswer;
    String wrongAnswer1;
    String wrongAnswer2;
    String wrongAnswer3;
    String cat;
    String subcat;
    String rightAnswerDesc;
    String photoUrl;
    String mcqId;
    int year;
    String board;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    int questionNumber;


    boolean isRevision;
    boolean isAttempted;

    public boolean isRevision() {
        return isRevision;
    }

    public void setRevision(boolean revision) {
        isRevision = revision;
    }

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWrongAnswer1() {
        return wrongAnswer1;
    }

    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }

    public String getWrongAnswer2() {
        return wrongAnswer2;
    }

    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }

    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
    }

    public String getMcqId() {
        return mcqId;
    }

    public void setMcqId(String mcqId) {
        this.mcqId = mcqId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }


    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getSubcat() {
        return subcat;
    }

    public void setSubcat(String subcat) {
        this.subcat = subcat;
    }

    public String getRightAnswerDesc() {
        return rightAnswerDesc;
    }

    public void setRightAnswerDesc(String rightAnswerDesc) {
        this.rightAnswerDesc = rightAnswerDesc;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            MCQ mcq = (MCQ) obj;
            if (getQuestion().equalsIgnoreCase(mcq.getQuestion())){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}
