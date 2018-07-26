package com.kandara.medicalapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.StudyModeSSSSAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.SubchapterList;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.HtmlCleaner;
import com.kandara.medicalapp.Util.UtilDialog;
import com.kandara.medicalapp.View.headerlistview.HeaderListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StudyModeActivity extends AppCompatActivity {
    HeaderListView listView;
    EditText searchEditText;
    ArrayList<SubchapterList> studyList;
    ArrayList<SubchapterList> searchList;
    ArrayList<Study> studyArrayList;
    ArrayList<Study> searchQueryList;
    AppCompatImageView searchIcon;
    ArrayList<String> selectedSubTopics;
    RelativeLayout progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studymode);
        progressView = findViewById(R.id.mcqProgressView);
        updateToolbarAndStatusBar();
        studyArrayList = new ArrayList<>();
        selectedSubTopics = getIntent().getStringArrayListExtra("subTopics");
        listView = findViewById(R.id.studyListView);
        searchEditText = findViewById(R.id.searchEdittext);
        searchIcon = findViewById(R.id.searchIcon);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTxt = searchEditText.getText().toString();
                if (!searchTxt.isEmpty()) {

                    if (!AccountManager.isUserPremium(getApplicationContext())) {
                        UtilDialog.showUpgradeDialog(StudyModeActivity.this);
                    } else {
                        getSearchStudyData(searchTxt);
                    }
                } else {
                    getStudyData();
                }
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchTxt = searchEditText.getText().toString();
                    if (!searchTxt.isEmpty()) {
                        if (!AccountManager.isUserPremium(getApplicationContext())) {
                            UtilDialog.showUpgradeDialog(StudyModeActivity.this);
                        } else {
                            getSearchStudyData(searchTxt);
                        }
                    } else {
                        getStudyData();
                    }
                    return true;
                }
                return false;
            }
        });
        getStudyData();

    }


    public void updateToolbarAndStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.main_bg_color));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void getStudyData() {

        String query = "";
        for (int i = 0; i < selectedSubTopics.size(); i++) {
            if (i != selectedSubTopics.size() - 1) {
                query += "subCategory=" + selectedSubTopics.get(i).toUpperCase() + "&";
            } else {
                query += "subCategory=" + selectedSubTopics.get(i).toUpperCase();
            }
        }
        progressView.setVisibility(View.VISIBLE);
        String premQuery = "";
        if (!AccountManager.isUserPremium(getApplicationContext())) {
            premQuery = "limit=" + AppConstants.FREE_USERS_DATA_LIMIT + "&page=1&";
        }
        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_STUDY + "?" + premQuery + query.replaceAll(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressView.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final Study study = new Study();
                                study.setId(eachDataJsonObject.getInt("sid"));
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                if (eachDataJsonObject.has("imageUrl")) {
                                    study.setImageUrl("http://163.172.172.57:5000" + eachDataJsonObject.getString("imageUrl"));
                                }
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                String question = eachDataJsonObject.getString("question");

                                question=HtmlCleaner.cleanThis(question);

                                String answer = eachDataJsonObject.getJSONArray("answers").getString(0);
                                answer = HtmlCleaner.cleanThis(answer);
                                study.setQuestion(question.split("::::")[1]);
                                study.setQuestionNumber(Integer.parseInt(question.split("::::")[0]));
                                study.setAnswer(answer);
                                studyArrayList.add(study);
                            }
                            Collections.reverse(studyArrayList);
                            Collections.sort(studyArrayList, new CustomComparator());

                            if (!AccountManager.isUserPremium(getApplicationContext())) {

                                final Study study = new Study();
                                study.setId(1212);
                                study.setQuestionNumber(0);
                                study.setStudyId("asasasa");
                                study.setSubcategory("Premium Account");
                                study.setCategory("UPGRADE");
                                study.setQuestion("Upgrade your account");
                                study.setAnswer("<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "\t<head>\n" +
                                        "\t\t<title></title>\n" +
                                        "\t</head>\n" +
                                        "\t<body>\n" +
                                        "\n" +
                                        "\t\t<p>Upgrade your account to get following features</p>\n" +
                                        "\n" +
                                        "\t\t<ul>\n" +
                                        "\t\t\t<li>Download Chapters and Past Questions</li>\n" +
                                        "\t\t\t<li>No limit for any content</li>\n" +
                                        "\t\t\t<li>Search functionality</li>\n" +
                                        "\t\t</ul>\n" +
                                        "\t</body>\n" +
                                        "</html>");
                                studyArrayList.add(study);
                            }
                            populateHashMap();
                            StudyModeSSSSAdapter studyModeSSSSAdapter = new StudyModeSSSSAdapter(studyList, StudyModeActivity.this);
                            listView.setAdapter(studyModeSSSSAdapter);
                        } catch (JSONException e) {
                            Log.e("Error getting data", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressView.setVisibility(View.GONE);
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    private void getSearchStudyData(final String searchTxt) {
        progressView.setVisibility(View.VISIBLE);

        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstants.URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressView.setVisibility(View.GONE);
                        searchQueryList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray dataArray = jsonObject.getJSONArray("message");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final Study study = new Study();
                                study.setId(eachDataJsonObject.getInt("sid"));
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                if (eachDataJsonObject.has("imageUrl")) {
                                    study.setImageUrl("http://163.172.172.57:5000" + eachDataJsonObject.getString("imageUrl"));
                                }


                                String answer = eachDataJsonObject.getJSONArray("answers").getString(0);

                                Document doc = Jsoup.parse(answer);

                                for (Element element : doc.select("*")) {
                                    if (!element.hasText() && element.isBlock()) {
                                        element.remove();
                                    }
                                }
                                answer = doc.body().html();
                                study.setAnswer(answer);
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));

                                String question = eachDataJsonObject.getString("question");
                                study.setQuestion(question.split("::::")[1]);
                                study.setQuestionNumber(Integer.parseInt(question.split("::::")[0]));
                                searchQueryList.add(study);
                            }
                            Collections.sort(searchQueryList, new CustomComparator());

                            populateHashMap();
                            StudyModeSSSSAdapter studyModeSSSSAdapter = new StudyModeSSSSAdapter(searchList, StudyModeActivity.this);
                            studyModeSSSSAdapter.setSearchTxt(searchTxt);
                            listView.setAdapter(studyModeSSSSAdapter);

                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                            progressView.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressView.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("question", searchTxt);
                return params;
            }
        };
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    public class CustomComparator implements Comparator<Study> {
        @Override
        public int compare(Study study1, Study study2) {
            return study1.getQuestionNumber() - study2.getQuestionNumber();
        }
    }


    private void populateHashMap() {
        studyList = new ArrayList<>();
        searchList = new ArrayList<>();

        ArrayList<String> subChapterArrayList = new ArrayList<>();
        for (Study study : studyArrayList) {
            if (!subChapterArrayList.contains(study.getSubcategory())) {
                subChapterArrayList.add(study.getSubcategory());
            }
        }

        for (String subchaptername : subChapterArrayList) {
            SubchapterList subchapterList = new SubchapterList(subchaptername);
            for (Study study : studyArrayList) {
                if (study.getSubcategory().equalsIgnoreCase(subchaptername)) {
                    subchapterList.addStudy(study);
                }
            }
            studyList.add(subchapterList);
        }

        if (searchQueryList != null) {
            for (String subchaptername : subChapterArrayList) {
                SubchapterList subchapterList = new SubchapterList(subchaptername);
                for (Study study : searchQueryList) {
                    if (study.getSubcategory().equalsIgnoreCase(subchaptername)) {
                        subchapterList.addStudy(study);
                    }
                }
                searchList.add(subchapterList);
            }
        }
    }


}
