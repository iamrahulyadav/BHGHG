package com.kandara.medicalapp.Model;

import java.util.ArrayList;

/**
 * Created by abina on 4/25/2018.
 */

public class Test {

    String tid;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    int id;
    String name;
    String description;
    long testDuration;
    long testStartTime;
    ArrayList<MCQ> mcqArrayList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Test() {
        mcqArrayList=new ArrayList<>();
    }

    public void addMCQ(MCQ mcq){
        mcqArrayList.add(mcq);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTestDuration() {
        return testDuration;
    }

    public void setTestDuration(long testDuration) {
        this.testDuration = testDuration;
    }

    public long getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(long testStartTime) {
        this.testStartTime = testStartTime;
    }

    public ArrayList<MCQ> getMcqArrayList() {
        return mcqArrayList;
    }

    public void setMcqArrayList(ArrayList<MCQ> mcqArrayList) {
        this.mcqArrayList = mcqArrayList;
    }
}
