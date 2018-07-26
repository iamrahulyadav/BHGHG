package com.kandara.medicalapp.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Qt {

	String question;
	String rightAnswer;
	ArrayList<String> wrongAnswers;
	public Qt() {
		super();
		wrongAnswers=new ArrayList();
	}
	
	public String getQuestion() {
		return question;
	}



	public void setQuestion(String question) {
		this.question = question;
	}



	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer=rightAnswer;
	}
	
	public void addWrongAnswer(String wrongAnswer) {
		wrongAnswers.add(wrongAnswer);
		Collections.shuffle(wrongAnswers);
	}
	
	public String getRightAnswer() {
		return rightAnswer;
	}
	
	public String getRandomWrongAnswer() {
		Random random=new Random();
		int index=random.nextInt(3);
		return wrongAnswers.get(index);
	}
	
	public ArrayList<String> getWrongAnswers(){
		return wrongAnswers;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return question+"\nA.\t"+rightAnswer+"\nB.\t"+wrongAnswers.get(0)+"\nC.\t"+wrongAnswers.get(1)+"\nD.\t"+wrongAnswers.get(2);
	}
	

	
	
}
