package com.kandara.medicalapp.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.ChapterItem;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.View.FilterView;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.kandara.medicalapp.activity.MCQActivity;
import com.kandara.medicalapp.activity.MainActivity;
import com.kandara.medicalapp.activity.SplashActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abina on 3/2/2018.
 */

public class DataManager {

    public static void PopulateUser(final Context context, final TextView fullName, final RoundedImageView profileImage, String uid) {

        String tag_string_req = "get_user_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/usermeta/uid/" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Data Reponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject userData = jsonObject.getJSONObject("data");


                            if (userData.has("lastname") && userData.has("firstname") ) {
                                fullName.setText(userData.getString("firstname") + " " + userData.getString("lastname"));
                            }else{
                                fullName.setText("Guest User");
                            }
                            Picasso.with(context).load(AppConstants.MAIN_URL+"/media/"+userData.getString("profilePhoto")).into(profileImage);



                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage() + "");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }



    public static void populateTotal(final TextView totalQuestion, final String query) {

        String tag_string_req = "get_user_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/mcq?limit=1&page=1&" + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MCQ Response", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            totalQuestion.setText("Total Questions : "+dataObject.getInt("total"));
                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    public static void startMCQ(final Activity activity, final String query) {

        String tag_string_req = "get_user_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/mcq?limit=1&page=1&" + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MCQ Response", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            int total = dataObject.getInt("total");
                            if (total > 0) {
                                Intent intent = new Intent(activity, MCQActivity.class);
                                intent.putExtra("isStudy", false);
                                intent.putExtra("filterquery", query);
                                activity.startActivity(intent);
                            } else {
                                Toast.makeText(activity, "Please select at least one chapter or university", Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }




    public static void PopulateFullName(final Context context, final TextView fullName, final RoundedImageView profileImage, String uid) {
        String tag_string_req = "req_populate_user";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_USERS + "?_id=" + uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("message");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                fullName.setText(eachDataJsonObject.getString("firstname") + " " + eachDataJsonObject.getString("lastname"));
                                Picasso.with(context).load("https://i.ndtvimg.com/i/2017-02/rare-disease-620x350_620x350_41488260155.jpg").into(profileImage);
                            }
                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage() + "");
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    public static void LikeDiscussion(Context context, String did){

    }

    public static boolean isLiked(Context context, Discussion discussion){
        return false;
    }

    public static void PostVote(Context context, String did, String vid){

    }

    public static boolean isVoted(Context context, Discussion discussion){
        return false;
    }

    public static void addRevisionMCQ(Context context, String mcqid){

    }

    public static void addRevisionStudy(Context context, String studyid){

    }
}
