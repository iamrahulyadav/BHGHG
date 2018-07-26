package com.kandara.medicalapp.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.Dialogs.QuestionSelectDialog;
import com.kandara.medicalapp.Model.GridQuestion;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.MCQRevision;
import com.kandara.medicalapp.Model.Test;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.Util.Scheduler;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

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
    private int testid;
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
    private LinearLayout btnAddToRevision, btnMoreInfo;
    private TextView tvAddToRevision;
    private TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    private RelativeLayout optionALayout, optionBLayout, optionCLayout, optionDLayout;
    private boolean questionSelected = false;
    private TextView tvTimeRemaining;
    private HashMap<Integer, Integer> previousAttempts;
    private HashMap<Integer, Integer> previousRightAnswerPositions;
    private CountDownTimer countDownTimer;

    /**
     * AFTER QUIZ STUFFS
     */

    private RelativeLayout afterTestLayout;
    private TextView tvScore, tvTotal;
    private Button exitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initBeforeQuiz();
        initQuiz();
        initAfterQuiz();

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
        previousAttempts = new HashMap<>();
        previousRightAnswerPositions = new HashMap<>();
        mcqArrayList = new ArrayList<>();
        long duration=currentTest.getTestStartTime()+currentTest.getTestDuration()-Calendar.getInstance().getTimeInMillis();
        if(duration>100) {
            countDownTimer = new CountDownTimer(duration, 1000) {
                @Override
                public void onTick(long l) {
                    long millis = Long.parseLong((currentTest.getTestStartTime() + currentTest.getTestDuration() - Calendar.getInstance().getTimeInMillis()) + "");
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
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showScore();
            }
        });
        mcqArrayList = currentTest.getMcqArrayList();
        PopulateMCQ();
    }

    private void initBeforeQuiz() {

        afterTestLayout = findViewById(R.id.afterTestLayout);
        tvScore = findViewById(R.id.tvScore);
        tvTotal = findViewById(R.id.tvTotal);
        exitBtn = findViewById(R.id.exitBtn);
        testLayout = findViewById(R.id.testLayout);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        btnMoreInfo = findViewById(R.id.btnMoreInfo);
        btnAddToRevision = findViewById(R.id.btnAddtoRevision);
        tvAddToRevision = findViewById(R.id.tvAddTorevision);
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
        testid = getIntent().getIntExtra("id", 0);
        beforeStartLayout = findViewById(R.id.beforeStartLayout);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setEnabled(true);
        tvTestTime = findViewById(R.id.test_time);
        tvTestDesc = findViewById(R.id.test_desc);
        tvTestTitle = findViewById(R.id.test_title);
        currentTest = JsondataUtil.getTestById(getApplicationContext(), testid);
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
                scheduler.stopRepeatingTask();
                beforeStartLayout.setVisibility(View.GONE);
                testLayout.setVisibility(View.VISIBLE);
            }
        });
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
            if (minutes <= 30 && hours<1) {
                enableStartTest();
                tvTestTime.setText("Test has started for " + minutes + " min. Start soon or you will miss out.");
            } else if (minutes > 30) {
                disableStartTest();
                tvTestTime.setText("Test has been running for " + hours + "hours "+minutes+" min. You cant start now.");
            } else if (timeelapsed > currentTest.getTestDuration()) {
                disableStartTest();
                tvTestTime.setText("Test has already finished");
            } else {
                disableStartTest();
                tvTestTime.setText("Test has already finished");
            }

        }
    }

    private void disableStartTest() {
        startBtn.setEnabled(false);
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

        final MCQRevision revision = new MCQRevision();
        revision.setQuestionId(mcq.getMcqId());
        revision.setIsStudy(0);
        if (Select.from(MCQRevision.class).where(Condition.prop("id").eq(mcq.getId())).count()!=0) {
            tvAddToRevision.setText("Revision -");
        } else {
            tvAddToRevision.setText("Revision +");
        }


        btnAddToRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Select.from(MCQRevision.class).where(Condition.prop("id").eq(mcq.getId())).count()!=0) {
                    revision.delete();
                    tvAddToRevision.setText("Revision +");
                    Toast.makeText(getApplicationContext(), "Removed from revision", Toast.LENGTH_SHORT).show();
                } else {
                    revision.save();
                    tvAddToRevision.setText("Revision -");
                    Toast.makeText(getApplicationContext(), "Added to Revision", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
        gridQuestion.setQuestionId(mcq.getMcqId());
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
}
