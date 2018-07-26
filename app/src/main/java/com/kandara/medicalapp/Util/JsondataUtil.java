package com.kandara.medicalapp.Util;

import android.content.Context;
import android.util.Log;

import com.kandara.medicalapp.Model.BannerTopic;
import com.kandara.medicalapp.Model.Comment;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.LeaderBoardModel;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.News;
import com.kandara.medicalapp.Model.Qt;
import com.kandara.medicalapp.Model.St;
import com.kandara.medicalapp.Model.Test;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.Model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by abina on 1/24/2018.
 */

public class JsondataUtil {
    public static Test getTestById(Context context, int id) {
        try {
            String jsonString = loadJSONFromAsset(context, "Tests.json");
            JSONArray mainJsonArray = new JSONArray(jsonString);
            for (int k = 0; k < mainJsonArray.length(); k++) {

                Test test=new Test();
                JSONObject testJsonObject=mainJsonArray.getJSONObject(k);

                JSONArray jsonArray = testJsonObject.getJSONArray("mcqs");
                test.setId(testJsonObject.getInt("id"));
                test.setName(testJsonObject.getString("title"));
                test.setDescription(testJsonObject.getString("desc"));
                test.setTestDuration(testJsonObject.getLong("testduration"));
                test.setTestStartTime(testJsonObject.getLong("testarttime"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MCQ mcq = new MCQ();
                    mcq.setQuestion(jsonObject.getString("question"));
                    mcq.setRightAnswer(jsonObject.getString("rightanswer"));
                    mcq.setId(i);
                    mcq.setWrongAnswer1(jsonObject.getString("wronganswer1"));
                    mcq.setWrongAnswer2(jsonObject.getString("wronganswer2"));
                    mcq.setWrongAnswer3(jsonObject.getString("wronganswer3"));
                    test.addMCQ(mcq);
                }
                if(test.getId()==id){
                    return test;
                }
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return null;
    }

    public static ArrayList<Test> getTestArrayList(Context context) {
        ArrayList<Test> tests = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "Tests.json");
            JSONArray mainJsonArray = new JSONArray(jsonString);
            for (int k = 0; k < mainJsonArray.length(); k++) {

                Test test=new Test();
                JSONObject testJsonObject=mainJsonArray.getJSONObject(k);

                JSONArray jsonArray = testJsonObject.getJSONArray("mcqs");
                test.setId(testJsonObject.getInt("id"));
                test.setName(testJsonObject.getString("title"));
                test.setDescription(testJsonObject.getString("desc"));
                test.setTestDuration(testJsonObject.getLong("testduration"));
                test.setTestStartTime(testJsonObject.getLong("testarttime"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MCQ mcq = new MCQ();
                    mcq.setQuestion(jsonObject.getString("question"));
                    mcq.setRightAnswer(jsonObject.getString("rightanswer"));
                    mcq.setId(i);
                    mcq.setWrongAnswer1(jsonObject.getString("wronganswer1"));
                    mcq.setWrongAnswer2(jsonObject.getString("wronganswer2"));
                    mcq.setWrongAnswer3(jsonObject.getString("wronganswer3"));
                    test.addMCQ(mcq);
                }
                tests.add(test);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return tests;
    }




    public static ArrayList<MCQ> getMCQArrayList(Context context) {
        ArrayList<MCQ> mcqArrayList = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "MCQ.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MCQ mcq=new MCQ();
                mcq.setQuestion(jsonObject.getString("question"));
                mcq.setCat(jsonObject.getString("category"));
                mcq.setSubcat(jsonObject.getString("subcat"));
                mcq.setRightAnswer(jsonObject.getString("rightanswer"));
                mcq.setRightAnswerDesc(jsonObject.getString("rightansdesc"));
                mcq.setPhotoUrl(jsonObject.getString("imagelink"));

                long l = Long.parseLong(jsonObject.getString("_id"));
                mcq.setId(l);
                mcq.setWrongAnswer1(jsonObject.getString("wronganswer1"));
                mcq.setWrongAnswer2(jsonObject.getString("wronganswer2"));
                mcq.setWrongAnswer3(jsonObject.getString("wronganswer3"));
                mcqArrayList.add(mcq);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }

        return mcqArrayList;
    }

    public static ArrayList<St> getSts(Context context) {
        ArrayList<St> qtArrayList = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "study.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                St st=new St();
                st.setAnswer(jsonObject.getString("answer"));
                st.setQuestion(jsonObject.getString("question"));
                qtArrayList.add(st);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }

        return qtArrayList;
    }

    public static ArrayList<Qt> getQts(Context context) {
        ArrayList<Qt> qtArrayList = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "mcqs.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                Qt qt=new Qt();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                qt.setQuestion(jsonObject.getString("question"));
                qt.setRightAnswer(jsonObject.getString("rightAnswer"));
                JSONArray jsonArray1=jsonObject.getJSONArray("wrongAnswers");
                for(int k=0; k<jsonArray1.length(); k++){
                    qt.addWrongAnswer(jsonArray1.getString(k));
                }
                qtArrayList.add(qt);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }

        return qtArrayList;
    }

    public static ArrayList<News> getNews(Context context) {
        ArrayList<News> newsArrayList = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "News.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News(jsonObject.getString("title"), jsonObject.getString("description"), jsonObject.getString("photo"));
                newsArrayList.add(news);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return newsArrayList;
    }

    public static ArrayList<String> getStudyTopics(Context context) {
        ArrayList<String> topics = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "chapters.json");
            JSONObject jsonObject=new JSONObject(jsonString);
            Iterator keysToCopyIterator = jsonObject.keys();
            ArrayList<String> keysList = new ArrayList<>();
            while(keysToCopyIterator.hasNext()) {
                String key = (String) keysToCopyIterator.next();
                keysList.add(toFirstUpper(key));
            }
            return keysList;

        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return topics;
    }


    public static String toFirstUpper(String strng){

        String s1 = strng.substring(0, 1).toUpperCase();
        String topicCapital = s1 + strng.substring(1).toLowerCase();
        return topicCapital;
    }

    public static ArrayList<String> getSubTopics(String topic, Context context) {
        ArrayList<String> topics = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "chapters.json");
            JSONObject jsonObject=new JSONObject(jsonString);
            ArrayList<String> subtopicsArray=new ArrayList<>();

            JSONArray jsonArray=jsonObject.getJSONArray(topic);
            for(int i=0; i<jsonArray.length(); i++){
                subtopicsArray.add(jsonArray.getString(i));
            }
            return subtopicsArray;

        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return topics;
    }


    public static ArrayList<Topic> getTopics(Context context) {
        ArrayList<Topic> topics = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "StudyTopic.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Topic topic = new Topic(jsonObject.getString("name"));
                topics.add(topic);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return topics;
    }



    public static ArrayList<BannerTopic> getBannerTopics(Context context) {
        ArrayList<BannerTopic> topics = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "BannerTopics.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BannerTopic topic = new BannerTopic(jsonObject.getString("name"), jsonObject.getString("photo"), jsonObject.getString("desc"));
                topics.add(topic);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return topics;
    }

    public static ArrayList<LeaderBoardModel> getLeaderboard(Context context) {
        ArrayList<LeaderBoardModel> leaderBoardModelArrayList = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(context, "Leaderboard.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                LeaderBoardModel leaderBoardModel = new LeaderBoardModel(jsonObject.getString("name"), jsonObject.getInt("points"), jsonObject.getString("profilephoto"), jsonObject.getInt("position"));
                leaderBoardModelArrayList.add(leaderBoardModel);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }

        Collections.sort(leaderBoardModelArrayList, new CustomComparator());
        return leaderBoardModelArrayList;
    }

    public static class CustomComparator implements Comparator<LeaderBoardModel> {
        @Override
        public int compare(LeaderBoardModel o1, LeaderBoardModel o2) {
            return o1.getPosition() - o2.getPosition();
        }
    }


    public static User getUserDetail(Context context) {
        User user = new User();
        try {
            String jsonString = loadJSONFromAsset(context, "UserData.json");
            JSONObject jsonObject = new JSONObject(jsonString);
            user.setFirstName(jsonObject.getString("firstname"));
            user.setLastName(jsonObject.getString("lastname"));
            user.setUserid(jsonObject.getInt("userid"));
            user.setEmail(jsonObject.getString("email"));
            user.setProfilePhoto(jsonObject.getString("profilephoto"));
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
        return user;
    }

    public static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
