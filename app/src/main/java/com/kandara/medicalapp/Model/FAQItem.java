package com.kandara.medicalapp.Model;

/**
 * Created by abina on 8/2/2018.
 */

public class FAQItem {

    String question;
    String answer;


    public FAQItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
