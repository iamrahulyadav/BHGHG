package com.kandara.medicalapp.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Dialogs.FlagDialog;
import com.kandara.medicalapp.Dialogs.QuestionSelectDialog;
import com.kandara.medicalapp.Model.GridQuestion;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.MCQRevision;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.StudyRevision;
import com.kandara.medicalapp.Model.SubTopic;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.HtmlCleaner;
import com.kandara.medicalapp.Util.OnSwipeTouchListener;
import com.kandara.medicalapp.Util.UtilDialog;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class FlashCardActivity extends AppCompatActivity {

    AppCompatImageView btnExit;
    AppCompatImageView btnGrid;
    AppCompatImageView navLeft, navRight, addToRevision, icMoreInfo;
    int currentQuestionNumber = 0;
    int totalQuestionNumbers = 0;


    private ImageView image;
    private TextView tvQuestion;
    private WebView wvAnswer;
    private LinearLayout options;
    CatLoadingView mView;
    boolean isRevision = false;

    ArrayList<String> selectedSubTopics;
    static String GROUP_URL = "https://www.facebook.com/groups/1500955180122044/";

    Study currentStudy;

    RelativeLayout progressView;

    RelativeLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        progressView = findViewById(R.id.mcqProgressView);
        mainView = findViewById(R.id.mainView);


        mainView.setOnTouchListener(new OnSwipeTouchListener(FlashCardActivity.this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (currentQuestionNumber > 0) {
                    currentQuestionNumber--;
                    getData();
                }
                toggleLeftRightIcon();
            }

            public void onSwipeLeft() {


                if (currentQuestionNumber >= (AppConstants.FREE_USERS_DATA_LIMIT - 1) && !AccountManager.isUserPremium(getApplicationContext())) {
                    UtilDialog.showLimitReachedDialog(FlashCardActivity.this);
                } else {
                    if (currentQuestionNumber < totalQuestionNumbers - 1) {
                        currentQuestionNumber++;
                        getData();
                    }
                    toggleLeftRightIcon();
                }
            }

            public void onSwipeBottom() {
            }

        });

        icMoreInfo = findViewById(R.id.icMoreInfo);
        selectedSubTopics = getIntent().getStringArrayListExtra("subTopics");
        icMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlagDialog flagDialog = new FlagDialog(FlashCardActivity.this);
                flagDialog.setQuestionId(currentStudy.getStudyId());
                flagDialog.setActivity(FlashCardActivity.this);
                flagDialog.show();
            }
        });
        if (getIntent().hasExtra("revision")) {
            isRevision = true;
        }

        if (getIntent().hasExtra("questionNumber")) {
            currentQuestionNumber = getIntent().getIntExtra("questionNumber", 1);
        }
        mView = new CatLoadingView();
        updateToolbarAndStatusBar();
        mainView = findViewById(R.id.mainView);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navLeft = findViewById(R.id.icLeft);
        navRight = findViewById(R.id.icRight);
        tvQuestion = findViewById(R.id.tvComment);
        wvAnswer = findViewById(R.id.tvAnswer);
        addToRevision = findViewById(R.id.icAddtoRevision);
        options = findViewById(R.id.options);
        image = findViewById(R.id.image);
        navRight.setVisibility(View.INVISIBLE);
        mainView.setVisibility(View.INVISIBLE);
        btnExit.setVisibility(View.INVISIBLE);
        btnGrid = findViewById(R.id.btnGrid);
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "";
                for (int i = 0; i < selectedSubTopics.size(); i++) {
                    if (i != selectedSubTopics.size() - 1) {
                        query += "subCategory=" + selectedSubTopics.get(i).toUpperCase() + "&";
                    } else {
                        query += "subCategory=" + selectedSubTopics.get(i).toUpperCase();
                    }
                }
                progressView.setVisibility(View.VISIBLE);
                String tag_string_req = "req_study";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_STUDY + "?fields=_id,sid&" + query.replaceAll(" ", "%20")+"&sortBy=questionNumber",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressView.setVisibility(View.GONE);
                                final ArrayList<Study> studyArrayList = new ArrayList<>();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    JSONArray dataArray = dataObject.getJSONArray("docs");
                                    totalQuestionNumbers = dataObject.getInt("total");
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                        final Study study = new Study();
                                        study.setId(eachDataJsonObject.getInt("sid"));
                                        study.setQuestionNumber(i + 1);
                                        study.setStudyId(eachDataJsonObject.getString("_id"));
                                        studyArrayList.add(study);

                                    }
                                    final QuestionSelectDialog questionSelectDialog = new QuestionSelectDialog(FlashCardActivity.this);
                                    questionSelectDialog.setActivity(FlashCardActivity.this);
                                    questionSelectDialog.setQuestionArray(studyArrayList);
                                    questionSelectDialog.setOnQuestionSelected(new QuestionGridSelectAdapter.OnQuestionSelected() {
                                        @Override
                                        public void QuestionSelected(int questionNumber) {
                                            currentQuestionNumber = questionNumber - 1;
                                            if(currentQuestionNumber>=(AppConstants.FREE_USERS_DATA_LIMIT-1) && !AccountManager.isUserPremium(getApplicationContext())) {
                                                UtilDialog.showLimitReachedDialog(FlashCardActivity.this);
                                            } else {
                                                toggleLeftRightIcon();
                                                getData();
                                                questionSelectDialog.dismiss();
                                            }
                                        }
                                    });
                                    questionSelectDialog.show();
                                } catch (JSONException e) {
                                    Log.e("Error", e.getMessage());
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
        });

        toggleLeftRightIcon();
        getData();

        if (isRevision) {
            btnGrid.setVisibility(View.INVISIBLE);
        }
    }

    private void getData() {
        if (!isRevision) {
            retrieveDataWithoutRevision();
        } else {
            retrieveDataWithRevision();
        }
    }

    private void retrieveDataWithRevision() {
        List<StudyRevision> revisions = SugarRecord.listAll(StudyRevision.class);
        totalQuestionNumbers = revisions.size();
        StudyRevision currentRevision = revisions.get(currentQuestionNumber);
        getRevisionFromId(currentRevision.getQuestionId());
        toggleLeftRightIcon();
    }


    private void getRevisionFromId(String studyId) {
        Log.e("IDDDD", studyId + "");
        progressView.setVisibility(View.VISIBLE);
        String tag_string_req = "get_user_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/study?limit=1&page=1&_id=" + studyId,
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
                                    study.setImageUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                }
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                String question = eachDataJsonObject.getString("question");
                                question=HtmlCleaner.cleanThis(question);
                                String answer = eachDataJsonObject.getJSONArray("answers").getString(0);
                                answer = HtmlCleaner.cleanThis(answer);
                                study.setQuestion(question);
                                study.setQuestionNumber(eachDataJsonObject.getInt("questionNumber"));
                                study.setAnswer(answer);
                                currentStudy = study;
                                handleAfterData();
                                break;
                            }
                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressView.setVisibility(View.GONE);
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }


    private void retrieveDataWithoutRevision() {
        String query = "";
        for (int i = 0; i < selectedSubTopics.size(); i++) {
            if (i != selectedSubTopics.size() - 1) {
                query += "subCategory=" + selectedSubTopics.get(i).toUpperCase() + "&";
            } else {
                query += "subCategory=" + selectedSubTopics.get(i).toUpperCase();
            }
        }
        progressView.setVisibility(View.VISIBLE);
        String tag_string_req = "req_study";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_STUDY + "?limit=1&page=" + (currentQuestionNumber + 1) + "&" + query.replaceAll(" ", "%20")+"&sortBy=questionNumber",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressView.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            totalQuestionNumbers = dataObject.getInt("total");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final Study study = new Study();
                                study.setId(eachDataJsonObject.getInt("sid"));
                                study.setStudyId(eachDataJsonObject.getString("_id"));
                                if (eachDataJsonObject.has("imageUrl")) {
                                    study.setImageUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                }
                                study.setSubcategory(eachDataJsonObject.getString("subCategory"));
                                study.setCategory(eachDataJsonObject.getString("category"));
                                String question = eachDataJsonObject.getString("question");
                                question=HtmlCleaner.cleanThis(question);
                                String answer = eachDataJsonObject.getJSONArray("answers").getString(0);
                                answer = HtmlCleaner.cleanThis(answer);
                                study.setQuestion(question);
                                study.setQuestionNumber(eachDataJsonObject.getInt("questionNumber"));
                                study.setAnswer(answer);
                                currentStudy = study;
                                handleAfterData();
                            }
                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
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


    public void populateView(final Study study) {
        wvAnswer.setVisibility(View.GONE);
        if (Select.from(StudyRevision.class).where(Condition.prop("question_id").eq(study.getStudyId())).count() != 0) {
            addToRevision.setImageResource(R.drawable.ic_remove_from_revision);
        } else {
            addToRevision.setImageResource(R.drawable.ic_add_to_revision);
        }
        image.setVisibility(View.GONE);
        image.setImageResource(0);
        final StudyRevision revision = new StudyRevision();
        revision.setQuestionId(study.getStudyId());
        revision.setId(study.getId());
        revision.setIsStudy(1);
        addToRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Select.from(StudyRevision.class).where(Condition.prop("question_id").eq(study.getStudyId())).count() != 0) {

                    SugarRecord.deleteAll(StudyRevision.class, "question_id='"+revision.getQuestionId()+"'");
                    addToRevision.setImageResource(R.drawable.ic_add_to_revision);
                    Toast.makeText(getApplicationContext(), "Removed from revision", Toast.LENGTH_SHORT).show();
                } else {
                    revision.save();
                    addToRevision.setImageResource(R.drawable.ic_remove_from_revision);
                    Toast.makeText(getApplicationContext(), "Added to MCQ Revision", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (URLUtil.isValidUrl(study.getImageUrl())) {
            Picasso.with(getApplicationContext()).load(study.getImageUrl()).into(image);
        }
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvAnswer.getVisibility() == View.GONE) {
                    wvAnswer.setVisibility(View.VISIBLE);
                    GridQuestion gridQuestion = new GridQuestion();
                    gridQuestion.setQuestionId(study.getStudyId());
                    gridQuestion.save();
                }
                if (image.getVisibility() == View.GONE) {
                    image.setVisibility(View.VISIBLE);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvQuestion.setText(Html.fromHtml("<strong>" + (currentQuestionNumber + 1) + ". </strong>" + study.getQuestion(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvQuestion.setText(Html.fromHtml("<strong>" + (currentQuestionNumber + 1) + ". </strong>" + study.getQuestion()));
        }


        String answer = study.getAnswer();
        answer = answer.replaceAll("font-size: 10.0pt; ", "");

        answer = "\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "*{\n" +
                "  font-size:15px;\n" +
                " font-family: Arial;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" + answer + "</body>\n" +
                "</html>";
        wvAnswer.getSettings().setJavaScriptEnabled(true);
        wvAnswer.setBackgroundColor(Color.TRANSPARENT);
        wvAnswer.loadDataWithBaseURL("", answer, "text/html", "UTF-8", "");
    }

    private void handleAfterData() {
        populateView(currentStudy);
        mainView.setVisibility(View.VISIBLE);
        btnExit.setVisibility(View.VISIBLE);
        navLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionNumber > 0) {
                    currentQuestionNumber--;
                    getData();
                }
                toggleLeftRightIcon();
            }
        });
        navRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentQuestionNumber >= (AppConstants.FREE_USERS_DATA_LIMIT - 1) && !AccountManager.isUserPremium(getApplicationContext())) {
                    UtilDialog.showLimitReachedDialog(FlashCardActivity.this);
                } else {
                    if (currentQuestionNumber < totalQuestionNumbers - 1) {
                        currentQuestionNumber++;
                        getData();
                    }
                    toggleLeftRightIcon();
                }
            }
        });

    }

    private void toggleLeftRightIcon() {
        if (currentQuestionNumber == totalQuestionNumbers - 1) {
            navRight.setVisibility(View.INVISIBLE);
        } else {
            navRight.setVisibility(View.VISIBLE);
        }
        if (currentQuestionNumber == 0) {
            navLeft.setVisibility(View.INVISIBLE);
        } else {
            navLeft.setVisibility(View.VISIBLE);
        }
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

}
