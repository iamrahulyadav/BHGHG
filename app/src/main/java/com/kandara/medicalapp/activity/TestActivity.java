package com.kandara.medicalapp.activity;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.Adapter.TestAdapter;
import com.kandara.medicalapp.App.MedicalApplication;
import com.kandara.medicalapp.Dialogs.QuestionSelectDialog;
import com.kandara.medicalapp.Model.GridQuestion;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.MCQRevision;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.Test;
import com.kandara.medicalapp.Model.TestScoreItem;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.Util.Scheduler;
import com.kandara.medicalapp.Util.UtilDialog;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity {

    /**
     * BEFORE QUIZ STUFFS
     */
    private String testid;
    private LinearLayout beforeStartLayout;
    private Button startBtn;
    private TextView tvTestTime, tvTestDesc, tvTestTitle;
    private Test currentTest;
    private boolean testStarted = false;
    private Scheduler scheduler;


    /**
     * QUIZ STUFFS
     */
    private RelativeLayout testLayout;
    private AppCompatImageView navLeft, navRight;
    private Button submitBtn;
    private ArrayList<MCQ> mcqArrayList;
    private MCQ mcq;
    private int currentIndex = 0;
    private int rightAnswerpos;
    private TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private RelativeLayout optionALayout, optionBLayout, optionCLayout, optionDLayout;
    private boolean questionSelected = false;
    private TextView tvTimeRemaining;
    private HashMap<Integer, Integer> previousAttempts;
    private HashMap<Integer, Integer> previousRightAnswerPositions;
    private CountDownTimer countDownTimer;
    private long testStartTime = 0;

    /**
     * AFTER QUIZ STUFFS
     */

    private RelativeLayout afterTestLayout;
    private TextView tvScore, tvTotal;
    private Button exitBtn;

    AppCompatImageView btnGrid;
    private RelativeLayout progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        updateToolbarAndStatusBar();
        initBeforeQuiz();

    }

    private void showScore() {
        testLayout.setVisibility(View.GONE);
        afterTestLayout.setVisibility(View.VISIBLE);
        int score = 0;
        for (int i = 0; i < currentTest.getMcqArrayList().size(); i++) {
            if (previousAttempts.containsKey(i)) {
                int myChoice = previousAttempts.get(i);
                int correctChoice = previousRightAnswerPositions.get(i);
                if (myChoice == correctChoice) {
                    score++;
                }
            }
        }
        tvScore.setText(score + "");
        tvTotal.setText(currentTest.getMcqArrayList().size() + "");
        postFinalScore(score);
    }


    private void showPreviousScore(int score, int total) {
        beforeStartLayout.setVisibility(View.GONE);
        testLayout.setVisibility(View.GONE);
        afterTestLayout.setVisibility(View.VISIBLE);
        tvScore.setText(score + "");
        tvTotal.setText(total + "");
    }

    private void initAfterQuiz() {
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initQuiz() {
        initBeforeQuiz();
        previousAttempts = new HashMap<>();
        previousRightAnswerPositions = new HashMap<>();
        mcqArrayList = new ArrayList<>();
        countDownTimer = new CountDownTimer(currentTest.getTestDuration(), 1000) {
            @Override
            public void onTick(long l) {
                long millis = Long.parseLong((testStartTime+currentTest.getTestDuration() - Calendar.getInstance().getTimeInMillis()) + "");
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tvTimeRemaining.setText(hms);
            }

            @Override
            public void onFinish() {
                showScore();
            }
        };
        countDownTimer.start();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAfterQuiz();
                showScore();
            }
        });
        mcqArrayList = currentTest.getMcqArrayList();
        PopulateMCQ();
    }

    private void initBeforeQuiz() {
        btnGrid = findViewById(R.id.btnGrid);
        afterTestLayout = findViewById(R.id.afterTestLayout);
        progressView = findViewById(R.id.progressView);
        tvScore = findViewById(R.id.tvScore);
        tvTotal = findViewById(R.id.tvTotal);
        exitBtn = findViewById(R.id.exitBtn);
        testLayout = findViewById(R.id.testLayout);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        optionALayout = findViewById(R.id.optionALayout);
        optionBLayout = findViewById(R.id.optionBLayout);
        optionCLayout = findViewById(R.id.optionCLayout);
        optionDLayout = findViewById(R.id.optionDLayout);
        navLeft = findViewById(R.id.icLeft);
        navRight = findViewById(R.id.icRight);
        navRight.setVisibility(View.VISIBLE);
        tvQuestion = findViewById(R.id.tvComment);
        submitBtn = findViewById(R.id.submitBtn);
        tvTimeRemaining = findViewById(R.id.tvTimeRemaining);
        testid = getIntent().getStringExtra("tid");
        beforeStartLayout = findViewById(R.id.beforeStartLayout);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setEnabled(true);
        tvTestTime = findViewById(R.id.test_time);
        tvTestDesc = findViewById(R.id.test_desc);
        tvTestTitle = findViewById(R.id.test_title);
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ArrayList<Study> studyArrayList = new ArrayList<>();
                int i = 0;
                for (MCQ mcq : mcqArrayList) {
                    final Study study = new Study();
                    study.setId(i);
                    study.setQuestionNumber(i + 1);
                    study.setStudyId(currentTest.getTid()+currentTest.getMcqArrayList().get(i).getQuestion());
                    studyArrayList.add(study);
                    i++;
                }
                final QuestionSelectDialog questionSelectDialog = new QuestionSelectDialog(TestActivity.this);
                questionSelectDialog.setActivity(TestActivity.this);
                questionSelectDialog.setQuestionArray(studyArrayList);
                questionSelectDialog.setOnQuestionSelected(new QuestionGridSelectAdapter.OnQuestionSelected() {
                    @Override
                    public void QuestionSelected(int questionNumber) {
                        currentIndex = questionNumber - 1;
                        PopulateMCQ();
                        questionSelectDialog.dismiss();
                    }
                });
                questionSelectDialog.show();
            }
        });
        getTest();
    }

    private void postInitialScore() {

        JSONObject params = new JSONObject();
        try {
            params.put("_sid", AccountManager.getUserId(getApplicationContext()));
            params.put("timeTaken", 0);
            params.put("score", 0);
        } catch (JSONException e) {
        }
        String tag_string_req = "post_study";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_TEST_POSTSCORE + "/" + currentTest.getTid(), params, new Response.Listener<JSONObject>() {
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

    private void postFinalScore(int score) {

        JSONObject params = new JSONObject();
        try {
            params.put("_sid", AccountManager.getUserId(getApplicationContext()));

            long millis = Long.parseLong((testStartTime+currentTest.getTestDuration() - Calendar.getInstance().getTimeInMillis()) + "");
            params.put("timeTaken", currentTest.getTestDuration()-millis);
            params.put("score", score);
        } catch (JSONException e) {
        }
        String tag_string_req = "post_study";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_TEST_POSTSCORE + "/" + currentTest.getTid(), params, new Response.Listener<JSONObject>() {
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

    private void updateTime() {
        double currentTime = Calendar.getInstance().getTimeInMillis();
        double milliseconds = currentTest.getTestStartTime() - currentTime;
        if (milliseconds > 0) {
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
            if (hours == 0) {
                if (minutes < 1) {
                    enableStartTest();
                } else {
                    disableStartTest();
                }
                tvTestTime.setText("Starts in  " + minutes + " Min");
            } else {
                disableStartTest();
                tvTestTime.setText("Starts in  " + hours + " Hours " + minutes + " Min");
            }
        } else {
            double timeelapsed = currentTime - currentTest.getTestStartTime();
            int hours = (int) ((timeelapsed / (1000 * 60 * 60)) % 24);
            int minutes = (int) ((timeelapsed / (1000 * 60)) % 60);
            if (minutes <= 30 && hours < 1) {
                enableStartTest();
                tvTestTime.setText("Test has started for " + minutes + " min. Start soon or you will miss out.");
            } else if (minutes > 30) {
                disableStartTest();
                tvTestTime.setText("Test Closed");
            } else if (timeelapsed > currentTest.getTestDuration()) {
                disableStartTest();
                tvTestTime.setText("Test has already finished");
            } else {
                disableStartTest();
                tvTestTime.setText("Test has already finished");
            }

        }
    }

    public void getTest() {
        progressView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.URL_TEST + "/" + testid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TEST", response);
                        progressView.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject testJsonObject = jsonObject.getJSONObject("data");
                            Test test = new Test();
                            test.setTid(testJsonObject.getString("_id"));
                            test.setName(testJsonObject.getString("name"));
                            test.setDescription(testJsonObject.getString("desc"));
                            test.setTestDuration(testJsonObject.getLong("totalTime"));
                            test.setTestStartTime(testJsonObject.getLong("startTime"));


                            JSONArray questionArray = testJsonObject.getJSONArray("questions");
                            for (int i = 0; i < questionArray.length(); i++) {

                                JSONObject eachDataJsonObject = questionArray.getJSONObject(i);
                                final MCQ mcq = new MCQ();
                                if (eachDataJsonObject.has("imageUrl")) {
                                    mcq.setPhotoUrl(AppConstants.MAIN_URL + eachDataJsonObject.getString("imageUrl"));
                                }
                                mcq.setRightAnswerDesc(eachDataJsonObject.getString("rightAnswerDesc"));
                                mcq.setRightAnswer(eachDataJsonObject.getString("rightAnswer"));
                                mcq.setCat(eachDataJsonObject.getString("category"));
                                mcq.setQuestion(eachDataJsonObject.getString("question"));
                                mcq.setYear(eachDataJsonObject.getString("subCategory").split("SSEPPE")[1]);
                                mcq.setBoard(eachDataJsonObject.getString("subCategory").split("SSEPPE")[0]);
                                JSONArray wrongAnswerArray = eachDataJsonObject.getJSONArray("wrongAnswers");
                                mcq.setWrongAnswer1(wrongAnswerArray.getString(0));
                                mcq.setWrongAnswer2(wrongAnswerArray.getString(1));
                                mcq.setWrongAnswer3(wrongAnswerArray.getString(2));
                                test.addMCQ(mcq);
                            }


                            JSONArray testScoreArray = testJsonObject.getJSONArray("testScores");
                            if(testScoreArray.length()>0){
                                TestScoreItem testScoreItem=new TestScoreItem();
                                for(int m=0; m<testScoreArray.length(); m++){
                                    JSONObject scoreObject=testScoreArray.getJSONObject(m);
                                    testScoreItem.setSid(scoreObject.getString("_sid"));
                                    Log.e("SID", scoreObject.getString("_sid"));
                                    Log.e("CSID", AccountManager.getUserId(getApplicationContext()));
                                    testScoreItem.setTimeTaken(scoreObject.getDouble("timeTaken"));
                                    testScoreItem.setScore(scoreObject.getInt("score"));
                                    if(testScoreItem.getSid().equalsIgnoreCase(AccountManager.getUserId(getApplicationContext()))){
                                        initAfterQuiz();
                                        showPreviousScore(testScoreItem.getScore(), questionArray.length());
                                        break;
                                    }
                                }
                            }

                            currentTest = test;

                            tvTestTitle.setText(currentTest.getName());
                            tvTestDesc.setText(currentTest.getDescription());
                            scheduler = new Scheduler(new Scheduler.RepeatingTask() {
                                @Override
                                public void Do() {
                                    updateTime();
                                }
                            });
                            updateTime();
                            scheduler.startRepeatingTask();

                            startBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    testStartTime = Calendar.getInstance().getTimeInMillis();
                                    initQuiz();
                                    postInitialScore();
                                    scheduler.stopRepeatingTask();
                                    beforeStartLayout.setVisibility(View.GONE);
                                    testLayout.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (JSONException e) {
                            Log.e("Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressView.setVisibility(View.GONE);
                        error.printStackTrace();
                    }
                });
        MedicalApplication.getInstance().addToRequestQueue(stringRequest, "test");
    }

    private void disableStartTest() {
        startBtn.setEnabled(true);
        startBtn.setBackgroundResource(R.drawable.bg_test_start_disabled);
    }

    private void enableStartTest() {
        startBtn.setEnabled(true);
        startBtn.setBackgroundResource(R.drawable.bg_test_start_enabled);
    }


    private void PopulateMCQ() {
        questionSelected = false;
        mcq = mcqArrayList.get(currentIndex);
        if (previousRightAnswerPositions.containsKey(currentIndex)) {
            rightAnswerpos = previousRightAnswerPositions.get(currentIndex);
        } else {
            rightAnswerpos = new Random().nextInt(4);
        }


        switch (rightAnswerpos) {
            case 0:
                tvOptionA.setText(mcq.getRightAnswer());
                tvOptionB.setText(mcq.getWrongAnswer1());
                tvOptionC.setText(mcq.getWrongAnswer2());
                tvOptionD.setText(mcq.getWrongAnswer3());
                break;


            case 1:
                tvOptionB.setText(mcq.getRightAnswer());
                tvOptionA.setText(mcq.getWrongAnswer1());
                tvOptionC.setText(mcq.getWrongAnswer2());
                tvOptionD.setText(mcq.getWrongAnswer3());
                break;


            case 2:
                tvOptionC.setText(mcq.getRightAnswer());
                tvOptionB.setText(mcq.getWrongAnswer1());
                tvOptionA.setText(mcq.getWrongAnswer2());
                tvOptionD.setText(mcq.getWrongAnswer3());
                break;


            case 3:
                tvOptionD.setText(mcq.getRightAnswer());
                tvOptionB.setText(mcq.getWrongAnswer1());
                tvOptionC.setText(mcq.getWrongAnswer2());
                tvOptionA.setText(mcq.getWrongAnswer3());
                break;
        }
        resetOptions();
        if (previousAttempts.containsKey(currentIndex)) {
            switch (previousAttempts.get(currentIndex)) {
                case 0:
                    tvOptionA.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionALayout.setBackgroundResource(R.drawable.bg_options_purple);
                    break;
                case 1:
                    tvOptionB.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionBLayout.setBackgroundResource(R.drawable.bg_options_purple);
                    break;
                case 2:
                    tvOptionC.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionCLayout.setBackgroundResource(R.drawable.bg_options_purple);
                    break;
                case 3:
                    tvOptionD.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionDLayout.setBackgroundResource(R.drawable.bg_options_purple);
                    break;
            }
        }
        optionALayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionALayout, 0, tvOptionA);
            }
        });
        optionBLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionBLayout, 1, tvOptionB);
            }
        });
        optionCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionCLayout, 2, tvOptionC);
            }
        });
        optionDLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionDLayout, 3, tvOptionD);
            }
        });
        tvQuestion.setText((currentIndex + 1) + ". " + mcq.getQuestion());
        navLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex > 0) {
                    currentIndex--;
                    PopulateMCQ();
                }
                toggleLeftRightIcon();
            }
        });
        navRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex < mcqArrayList.size() - 1) {
                    currentIndex++;
                    PopulateMCQ();
                }
                toggleLeftRightIcon();
            }
        });

    }

    private void toggleLeftRightIcon() {
        if (currentIndex >= mcqArrayList.size() - 1) {
            navRight.setVisibility(View.INVISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
        } else {
            navRight.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.GONE);
        }
        if (currentIndex == 0) {
            navLeft.setVisibility(View.INVISIBLE);
        } else {
            navLeft.setVisibility(View.VISIBLE);
        }
    }


    private void highlightRightWrong(final View selectedOption, final int currentPos, final TextView tvOption) {


        GridQuestion gridQuestion = new GridQuestion();
        gridQuestion.setQuestionId(currentTest.getTid()+currentTest.getMcqArrayList().get(currentIndex).getQuestion());
        gridQuestion.save();
        if (!questionSelected) {
            questionSelected = true;
            selectedOption.setBackgroundResource(R.drawable.bg_options_purple);
            tvOption.setTextColor(getResources().getColor(R.color.colorWhite));
            previousAttempts.put(currentIndex, currentPos);
            previousRightAnswerPositions.put(currentIndex, rightAnswerpos);
        }
    }


    private void resetOptions() {

        tvOptionA.setTextColor(getResources().getColor(R.color.colorDarkGray));
        optionALayout.setBackgroundResource(R.drawable.bg_options_normal);
        tvOptionB.setTextColor(getResources().getColor(R.color.colorDarkGray));
        optionBLayout.setBackgroundResource(R.drawable.bg_options_normal);
        tvOptionC.setTextColor(getResources().getColor(R.color.colorDarkGray));
        optionCLayout.setBackgroundResource(R.drawable.bg_options_normal);
        tvOptionD.setTextColor(getResources().getColor(R.color.colorDarkGray));
        optionDLayout.setBackgroundResource(R.drawable.bg_options_normal);
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
