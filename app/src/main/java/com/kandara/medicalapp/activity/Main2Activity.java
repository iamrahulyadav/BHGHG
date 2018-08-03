package com.kandara.medicalapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.DiscussionAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.DiscussionAnswer;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Qt;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.SubTopic;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.JsondataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<Study> studeis;
    ArrayList<MCQ> mcqs;
    ArrayList<Discussion> discussions;

    int page = 1;
    int maximumpage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        studeis = new ArrayList<>();
        mcqs = new ArrayList<>();
        discussions=new ArrayList<>();
        //ss();
        //getAllMCQ();
        //downloadAllContent();
        //postAllMCQ();
        postAllStudy();
        //getAllDiscussion();
    }

    private void postAllMCQ() {
        try {
            String jsonString = JsondataUtil.loadJSONFromAsset(getApplicationContext(), "allmcq.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.e("Count", (i + 1) + " out of " + jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                postEachMCQ(jsonObject);
            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
    }

    private void postEachMCQ(JSONObject jsonObject) {
        JSONObject params = new JSONObject();
        try {
            JSONArray wrongAnswers = new JSONArray();
            wrongAnswers.put(jsonObject.getString("wronganswer1"));
            wrongAnswers.put(jsonObject.getString("wronganswer2"));
            wrongAnswers.put(jsonObject.getString("wronganswer3"));
            params.put("wrongAnswers", wrongAnswers);
            params.put("question", jsonObject.getString("question"));
            params.put("category", jsonObject.getString("cat"));
            params.put("board", jsonObject.getString("board"));
            params.put("year", jsonObject.getString("year"));
            params.put("subCategory", jsonObject.getString("board") + "SSEPPE" + jsonObject.getString("year"));
            params.put("rightAnswer", jsonObject.getString("rightanswer"));
            params.put("rightAnswerDesc", jsonObject.getString("desc") + "nodesc");
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
        }
        String tag_string_req = "post_mcq";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_MCQ, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MedicalApplication.getInstance().addToRequestQueue(jsonObjectRequest, tag_string_req);
    }

    private void deleteAllDiscussion() {
        String tag_string_req = "req_study";
        for (Discussion discussion : discussions) {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AppConstants.URL_DISCUSSION + "/" + discussion.getId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        }
    }



    private void getAllDiscussion(){


        String tag_string_req = "req_mcqs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_DISCUSSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                Discussion discussion=new Discussion(eachDataJsonObject.getString("_id"));

                                discussions.add(discussion);
                            }

                            deleteAllDiscussion();

                        } catch (JSONException e) {
                            Log.e("Json Exception", e.getMessage()+"");
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


    private void postAllStudy() {
        try {
            String jsonString = JsondataUtil.loadJSONFromAsset(getApplicationContext(), "final2.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.e("Count", (i + 1) + " out of " + jsonArray.length());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                postEachStudy(jsonObject);

            }
        } catch (JSONException e) {
            Log.e("json Exception", e.getMessage() + "");
        }
    }

    private void postEachStudy(JSONObject jsonObject) {
        String tag_string_req = "post_study";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.10.12:5000/api/study", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("RESPONSE", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MedicalApplication.getInstance().addToRequestQueue(jsonObjectRequest, tag_string_req);
    }


    public void ss() {
        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/Study?limit=1000&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            maximumpage = dataObject.getInt("pages");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final Study study = new Study();
                                study.setId(eachDataJsonObject.getInt("sid"));
                                study.setQuestionNumber(i + 1);
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                if (eachDataJsonObject.has("imageUrl")) {
                                    study.setImageUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                }
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                study.setQuestion(eachDataJsonObject.getString("question"));
                                JSONArray answersArray = eachDataJsonObject.getJSONArray("answers");
                                for (int k = 0; k < answersArray.length(); k++) {
                                    study.setAnswer(answersArray.getString(k));
                                }
                                studeis.add(study);
                            }
                            deleteAllStudy();
                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    private void getAllStudy() {
        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_STUDY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                Study study = new Study();
                                study.setQuestionNumber(i + 1);
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                study.setImageUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                study.setQuestion(eachDataJsonObject.getString("question"));
                                JSONArray answersArray = eachDataJsonObject.getJSONArray("answers");
                                for (int k = 0; k < answersArray.length(); k++) {
                                    study.setAnswer(answersArray.getString(k));
                                }
                                studeis.add(study);
                            }
                            deleteAllStudy();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void deleteAllStudy() {
        String tag_string_req = "req_study";
        for (Study study : studeis) {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AppConstants.URL_STUDY + "/" + study.getStudyId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        }
    }


    private void getAllMCQ() {
        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_MCQ+"?limit=1000&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                MCQ mcq = new MCQ();
                                mcq.setMcqId(eachDataJsonObject.getString("_id"));
                                mcqs.add(mcq);
                            }
                            deleteAllMCQ();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void deleteAllMCQ() {
        String tag_string_req = "req_study";
        for (MCQ mcq : mcqs) {
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AppConstants.URL_MCQ + "/" + mcq.getMcqId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        }
    }

    private void downloadAllContent() {

        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_STUDY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                Study study = new Study();
                                study.setId(eachDataJsonObject.getInt("sid"));
                                study.setQuestionNumber(i + 1);
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                if (eachDataJsonObject.has("imageUrl")) {
                                    study.setImageUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                }
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                study.setQuestion(eachDataJsonObject.getString("question"));
                                JSONArray answersArray = eachDataJsonObject.getJSONArray("answers");
                                for (int k = 0; k < answersArray.length(); k++) {
                                    study.setAnswer(answersArray.getString(k));
                                }
                                studeis.add(study);
                            }
                            downloadMCQ();
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

    private void downloadMCQ() {
        String tag_string_req = "req_mcqs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_MCQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                MCQ mcq = new MCQ();
                                mcq.setId(eachDataJsonObject.getInt("mid"));
                                mcq.setMcqId(eachDataJsonObject.getString("_id"));
                                mcq.setQuestionNumber(i + 1);
                                if (eachDataJsonObject.has("imageUrl")) {
                                    mcq.setPhotoUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                }
                                mcq.setRightAnswerDesc(eachDataJsonObject.getString("rightAnswerDesc"));
                                mcq.setRightAnswer(eachDataJsonObject.getString("rightAnswer"));
                                mcq.setSubcat(eachDataJsonObject.getString("subCategory"));
                                mcq.setCat(eachDataJsonObject.getString("category"));
                                mcq.setQuestion(eachDataJsonObject.getString("question"));
                                JSONArray wrongAnswerArray = eachDataJsonObject.getJSONArray("wrongAnswers");
                                mcq.setWrongAnswer1(wrongAnswerArray.getString(0));
                                mcq.setWrongAnswer2(wrongAnswerArray.getString(1));
                                mcq.setWrongAnswer3(wrongAnswerArray.getString(2));
                                mcqs.add(mcq);

                            }
                            postAllStudy();
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
}
