package com.kandara.medicalapp.Model;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.ArrayList;

/**
 * Created by abina on 2/11/2018.
 */

@Table
public class Study  extends SugarRecord{


    @Unique
    long id;

    String question;
    String imageUrl;
    String category;
    String subcategory;
    String answer;
    String studyId;


    int questionNumber;

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public Study(){
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Study) {
            if(getQuestion().equalsIgnoreCase(((Study) obj).getQuestion()) && getCategory().equalsIgnoreCase(((Study) obj).getCategory()) && getSubcategory().equalsIgnoreCase(((Study) obj).getSubcategory())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
