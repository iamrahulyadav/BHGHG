package com.kandara.medicalapp.activity;

import android.os.Build;
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
import com.kandara.medicalapp.Dialogs.MoreInfoDialog;
import com.kandara.medicalapp.Dialogs.QuestionSelectDialog;
import com.kandara.medicalapp.Model.GridQuestion;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.MCQAttempt;
import com.kandara.medicalapp.Model.MCQRevision;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.AppConstants;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.OnSwipeTouchListener;
import com.kandara.medicalapp.Util.UtilDialog;
import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCQActivity extends AppCompatActivity {

    AppCompatImageView btnExit;
    AppCompatImageView btnGrid;

    AppCompatImageView navLeft, navRight;
    boolean isRevision = false;

    MCQ currentMCQ;
    int currentQuestionNumber = 0;

    int totalQuestionNumber = 0;
    int rightAnswerpos;

    private AppCompatImageView btnAddToRevision, btnMoreInfo, btnFeedback;
    private TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private RelativeLayout optionALayout, optionBLayout, optionCLayout, optionDLayout;

    private boolean questionSelected = false;


    ArrayList<MCQAttempt> mcqAttempts;

    String filterQuery;
    RelativeLayout progressView;

    RelativeLayout mainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);
        updateToolbarAndStatusBar();
        progressView = findViewById(R.id.mcqProgressView);
        mainView = findViewById(R.id.mainView);
        mcqAttempts = new ArrayList<>();

        if (getIntent().hasExtra("questionNumber")) {
            currentQuestionNumber = getIntent().getIntExtra("questionNumber", 1);
        }
        if (getIntent().hasExtra("filterquery")) {
            filterQuery = getIntent().getStringExtra("filterquery");
        }

        tvQuestion = findViewById(R.id.tvComment);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);

        mainView.setOnTouchListener(new OnSwipeTouchListener(MCQActivity.this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (currentQuestionNumber > 0) {
                    currentQuestionNumber--;
                    getMCQ();
                }
                toggleLeftRightIcon();
            }

            public void onSwipeLeft() {



                if (currentQuestionNumber >= (AppConstants.FREE_USERS_DATA_LIMIT - 1) && !AccountManager.isUserPremium(getApplicationContext())) {
                    UtilDialog.showLimitReachedDialog(MCQActivity.this);
                } else {
                    if (currentQuestionNumber < totalQuestionNumber - 1) {
                        currentQuestionNumber++;
                        getMCQ();
                    }
                    toggleLeftRightIcon();
                }
            }

            public void onSwipeBottom() {
            }

        });

        btnMoreInfo = findViewById(R.id.btnMoreInfo);
        btnAddToRevision = findViewById(R.id.btnAddtoRevision);
        btnFeedback = findViewById(R.id.btnFeedback);


        optionALayout = findViewById(R.id.optionALayout);
        optionBLayout = findViewById(R.id.optionBLayout);
        optionCLayout = findViewById(R.id.optionCLayout);
        optionDLayout = findViewById(R.id.optionDLayout);

        btnExit = findViewById(R.id.btnExit);
        btnGrid = findViewById(R.id.btnGrid);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlagDialog flagDialog = new FlagDialog(MCQActivity.this);
                flagDialog.setQuestionId(currentMCQ.getMcqId());
                flagDialog.setActivity(MCQActivity.this);
                flagDialog.show();
            }
        });
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setVisibility(View.VISIBLE);
                String tag_string_req = "get_user_data";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/mcq?fields=_id,mid&" + filterQuery,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressView.setVisibility(View.GONE);
                                Log.e("MCQ Response", response.toString());
                                try {
                                    ArrayList<MCQ> mcqArrayList = new ArrayList<>();
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    JSONArray dataArray = dataObject.getJSONArray("docs");
                                    totalQuestionNumber = dataObject.getInt("total");
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                        final MCQ mcq = new MCQ();
                                        mcq.setId(eachDataJsonObject.getInt("mid"));
                                        mcq.setMcqId(eachDataJsonObject.getString("_id"));
                                        mcq.setQuestionNumber(i + 1);
                                        mcqArrayList.add(mcq);
                                    }
                                    final QuestionSelectDialog questionSelectDialog = new QuestionSelectDialog(MCQActivity.this);
                                    questionSelectDialog.setActivity(MCQActivity.this);
                                    questionSelectDialog.setQuestionArray(mcqArrayList);
                                    questionSelectDialog.setOnQuestionSelected(new QuestionGridSelectAdapter.OnQuestionSelected() {
                                        @Override
                                        public void QuestionSelected(int questionNumber) {
                                            currentQuestionNumber = questionNumber - 1;
                                            if (currentQuestionNumber >= (AppConstants.FREE_USERS_DATA_LIMIT - 1) && !AccountManager.isUserPremium(getApplicationContext())) {
                                                UtilDialog.showLimitReachedDialog(MCQActivity.this);
                                            } else {
                                                getMCQ();
                                                questionSelectDialog.dismiss();
                                                toggleLeftRightIcon();
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
                                progressView.setVisibility(View.GONE);
                            }
                        });
                MedicalApplication.getInstance().addToRequestQueue(stringRequest, tag_string_req);


            }
        });

        btnMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentMCQ.getRightAnswerDesc().isEmpty()) {
                    MoreInfoDialog moreInfoDialog = new MoreInfoDialog(MCQActivity.this);
                    moreInfoDialog.setMoreInfo(currentMCQ.getRightAnswerDesc());
                    moreInfoDialog.show();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navLeft = findViewById(R.id.icLeft);
        navRight = findViewById(R.id.icRight);


        if (getIntent().hasExtra("revision")) {
            isRevision = true;
        }
        if (isRevision) {
            btnGrid.setVisibility(View.INVISIBLE);
        }

        getMCQ();

        navLeft.setVisibility(View.INVISIBLE);
        navRight.setVisibility(View.VISIBLE);

    }

    private MCQAttempt getCurrentMCQAttempt() {
        for (MCQAttempt mcqAttempt : mcqAttempts) {
            if (mcqAttempt.getMcqId().equalsIgnoreCase(currentMCQ.getMcqId())) {
                return mcqAttempt;
            }
        }
        return null;
    }

    private void PopulateMCQ() {
        questionSelected = false;
        MCQAttempt mcqAttempt = getCurrentMCQAttempt();
        if (mcqAttempt == null) {
            rightAnswerpos = new Random().nextInt(4);
        } else {
            rightAnswerpos = mcqAttempt.getRightAnswerPos();
        }
        final MCQRevision revision = new MCQRevision();
        revision.setId(currentMCQ.getId());
        revision.setQuestionId(currentMCQ.getMcqId());
        revision.setIsStudy(0);
        if (Select.from(MCQRevision.class).where(Condition.prop("question_id").eq(currentMCQ.getMcqId())).count() != 0) {
            btnAddToRevision.setImageResource(R.drawable.ic_remove_from_revision);
        } else {
            btnAddToRevision.setImageResource(R.drawable.ic_add_to_revision);
        }


        btnAddToRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Select.from(MCQRevision.class).where(Condition.prop("question_id").eq(currentMCQ.getMcqId())).count() != 0) {

                    SugarRecord.deleteAll(MCQRevision.class, "question_id='" + revision.getQuestionId() + "'");
                    btnAddToRevision.setImageResource(R.drawable.ic_add_to_revision);
                    Toast.makeText(getApplicationContext(), "Removed from revision", Toast.LENGTH_SHORT).show();
                } else {
                    revision.save();
                    btnAddToRevision.setImageResource(R.drawable.ic_remove_from_revision);
                    Toast.makeText(getApplicationContext(), "Added to MCQ Revision", Toast.LENGTH_SHORT).show();
                }
            }
        });


        switch (rightAnswerpos) {
            case 0:
                tvOptionA.setText(currentMCQ.getRightAnswer());
                tvOptionB.setText(currentMCQ.getWrongAnswer1());
                tvOptionC.setText(currentMCQ.getWrongAnswer2());
                tvOptionD.setText(currentMCQ.getWrongAnswer3());
                break;


            case 1:
                tvOptionB.setText(currentMCQ.getRightAnswer());
                tvOptionA.setText(currentMCQ.getWrongAnswer1());
                tvOptionC.setText(currentMCQ.getWrongAnswer2());
                tvOptionD.setText(currentMCQ.getWrongAnswer3());
                break;


            case 2:
                tvOptionC.setText(currentMCQ.getRightAnswer());
                tvOptionB.setText(currentMCQ.getWrongAnswer1());
                tvOptionA.setText(currentMCQ.getWrongAnswer2());
                tvOptionD.setText(currentMCQ.getWrongAnswer3());
                break;


            case 3:
                tvOptionD.setText(currentMCQ.getRightAnswer());
                tvOptionB.setText(currentMCQ.getWrongAnswer1());
                tvOptionC.setText(currentMCQ.getWrongAnswer2());
                tvOptionA.setText(currentMCQ.getWrongAnswer3());
                break;
        }

        resetOptions();

        if (mcqAttempt != null) {
            if (mcqAttempt.getRightAnswerPos() == mcqAttempt.getUserSelectedPos()) {
                switch (mcqAttempt.getUserSelectedPos()) {
                    case 0:
                        tvOptionA.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionALayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                    case 1:
                        tvOptionB.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionBLayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                    case 2:
                        tvOptionC.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionCLayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                    case 3:
                        tvOptionD.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionDLayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                }

            } else {
                switch (mcqAttempt.getUserSelectedPos()) {
                    case 0:
                        tvOptionA.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionALayout.setBackgroundResource(R.drawable.bg_options_wrong);
                        break;
                    case 1:
                        tvOptionB.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionBLayout.setBackgroundResource(R.drawable.bg_options_wrong);
                        break;
                    case 2:
                        tvOptionC.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionCLayout.setBackgroundResource(R.drawable.bg_options_wrong);
                        break;
                    case 3:
                        tvOptionD.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionDLayout.setBackgroundResource(R.drawable.bg_options_wrong);
                        break;
                }
                switch (mcqAttempt.getRightAnswerPos()) {
                    case 0:
                        tvOptionA.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionALayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                    case 1:
                        tvOptionB.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionBLayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                    case 2:
                        tvOptionC.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionCLayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                    case 3:
                        tvOptionD.setTextColor(getResources().getColor(R.color.colorWhite));
                        optionDLayout.setBackgroundResource(R.drawable.bg_options_correct);
                        break;
                }
            }
        }

        optionALayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionALayout, rightAnswerpos, 0, tvOptionA);
            }
        });
        optionBLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionBLayout, rightAnswerpos, 1, tvOptionB);
            }
        });
        optionCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionCLayout, rightAnswerpos, 2, tvOptionC);
            }
        });
        optionDLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOptions();
                highlightRightWrong(optionDLayout, rightAnswerpos, 3, tvOptionD);
            }
        });
        tvQuestion.setText((currentQuestionNumber + 1) + ". " + currentMCQ.getQuestion());
        navLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionNumber > 0) {
                    currentQuestionNumber--;
                    getMCQ();
                }
                toggleLeftRightIcon();
            }
        });
        navRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionNumber >= (AppConstants.FREE_USERS_DATA_LIMIT - 1) && !AccountManager.isUserPremium(getApplicationContext())) {
                    UtilDialog.showLimitReachedDialog(MCQActivity.this);
                } else {
                    if (currentQuestionNumber < totalQuestionNumber - 1) {
                        currentQuestionNumber++;
                        getMCQ();
                    }
                    toggleLeftRightIcon();
                }
            }
        });

    }

    private void toggleLeftRightIcon() {
        if (currentQuestionNumber >= totalQuestionNumber - 1) {
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

    public void getMCQ() {
        if (isRevision) {
            PopulateMCQwithRevision();
        } else {
            PopulateMCQwithOutRevision();
        }
    }


    private void PopulateMCQwithRevision() {
        List<MCQRevision> revisions = SugarRecord.listAll(MCQRevision.class);
        totalQuestionNumber = revisions.size();
        MCQRevision currentRevision = revisions.get(currentQuestionNumber);
        getRevisionFromId(currentRevision.getQuestionId());
        toggleLeftRightIcon();
    }


    private void getRevisionFromId(String mcqId) {
        Log.e("IDDDD", mcqId + "");
        progressView.setVisibility(View.VISIBLE);
        String tag_string_req = "get_user_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/mcq?limit=1&page=1&_id=" + mcqId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressView.setVisibility(View.GONE);
                        Log.e("MCQ Response", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final MCQ mcq = new MCQ();
                                mcq.setId(eachDataJsonObject.getInt("mid"));
                                mcq.setMcqId(eachDataJsonObject.getString("_id"));
                                mcq.setQuestionNumber(i + 1);
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
                                currentMCQ = mcq;
                                PopulateMCQ();
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


    private void PopulateMCQwithOutRevision() {

        progressView.setVisibility(View.VISIBLE);
        String tag_string_req = "get_user_data";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.MAIN_URL+"/api/mcq?limit=1&page=" + (currentQuestionNumber + 1) + "&" + filterQuery.replaceAll(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressView.setVisibility(View.GONE);
                        Log.e("MCQ Response", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            JSONArray dataArray = dataObject.getJSONArray("docs");

                            totalQuestionNumber = dataObject.getInt("total");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject eachDataJsonObject = dataArray.getJSONObject(i);
                                final MCQ mcq = new MCQ();
                                mcq.setId(eachDataJsonObject.getInt("mid"));
                                mcq.setMcqId(eachDataJsonObject.getString("_id"));
                                mcq.setQuestionNumber(i + 1);
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
                                currentMCQ = mcq;
                                PopulateMCQ();
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


    private void highlightRightWrong(final View selectedOption, final int rightAnswerpos, final int currentPos, final TextView tvOption) {
        final GridQuestion gridQuestion = new GridQuestion();
        gridQuestion.setQuestionId(currentMCQ.getMcqId());
        if (!questionSelected) {
            questionSelected = true;
            selectedOption.setBackgroundResource(R.drawable.bg_options_orange);
            tvOption.setTextColor(getResources().getColor(R.color.colorWhite));
            AlphaAnimation blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            blinkanimation.setDuration(100); // duration - half a second
            blinkanimation.setInterpolator(new AccelerateDecelerateInterpolator()); // do not alter animation rate
            blinkanimation.setRepeatCount(3); // Repeat animation infinitely
            blinkanimation.setRepeatMode(Animation.REVERSE);
            blinkanimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    MCQActivity.this.highlightRightWrong(rightAnswerpos, currentPos, selectedOption, gridQuestion);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            selectedOption.startAnimation(blinkanimation);
            MCQAttempt mcqAttempt = new MCQAttempt();
            mcqAttempt.setMcqId(currentMCQ.getMcqId());
            mcqAttempt.setRightAnswerPos(rightAnswerpos);
            mcqAttempt.setUserSelectedPos(currentPos);
            if (!mcqAttempts.contains(mcqAttempt)) {
                mcqAttempts.add(mcqAttempt);
            }

        }

    }

    private void highlightRightWrong(int rightAnswerpos, int currentPos, View selectedOption, GridQuestion gridQuestion) {
        if (rightAnswerpos == currentPos) {
            gridQuestion.setCorrect(true);
            selectedOption.setBackgroundResource(R.drawable.bg_options_correct);
        } else {
            gridQuestion.setCorrect(false);
            selectedOption.setBackgroundResource(R.drawable.bg_options_wrong);
            switch (rightAnswerpos) {
                case 0:
                    tvOptionA.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionALayout.setBackgroundResource(R.drawable.bg_options_correct);
                    break;
                case 1:
                    tvOptionB.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionBLayout.setBackgroundResource(R.drawable.bg_options_correct);
                    break;
                case 2:
                    tvOptionC.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionCLayout.setBackgroundResource(R.drawable.bg_options_correct);
                    break;
                case 3:
                    tvOptionD.setTextColor(getResources().getColor(R.color.colorWhite));
                    optionDLayout.setBackgroundResource(R.drawable.bg_options_correct);
                    break;
            }
        }
        gridQuestion.save();
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
