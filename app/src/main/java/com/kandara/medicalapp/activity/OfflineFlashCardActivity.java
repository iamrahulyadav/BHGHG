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

import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.Dialogs.FlagDialog;
import com.kandara.medicalapp.Dialogs.QuestionSelectDialog;
import com.kandara.medicalapp.Model.GridQuestion;
import com.kandara.medicalapp.Model.MCQRevision;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.StudyRevision;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OfflineFlashCardActivity extends AppCompatActivity {

    AppCompatImageView btnExit;
    AppCompatImageView btnGrid;
    AppCompatImageView navLeft, navRight, addToRevision, icMoreInfo;
    RelativeLayout mainView;
    int currentQuestionNumber = 1;


    ArrayList<Study> studyArrayList;
    private ImageView image;
    private TextView tvQuestion;
    private WebView wvAnswer;
    private LinearLayout options;
    CatLoadingView mView;
    boolean isRevision = false;

    ArrayList<String> selectedSubTopics;
    static String GROUP_URL = "https://www.facebook.com/groups/1500955180122044/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_card);
        icMoreInfo = findViewById(R.id.icMoreInfo);
        selectedSubTopics = getIntent().getStringArrayListExtra("subTopics");
        icMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlagDialog flagDialog = new FlagDialog(OfflineFlashCardActivity.this);
                flagDialog.setQuestionId(studyArrayList.get(currentQuestionNumber).getStudyId());
                flagDialog.setActivity(OfflineFlashCardActivity.this);
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
        studyArrayList = new ArrayList<>();
        updateToolbarAndStatusBar();
        mainView = findViewById(R.id.mainView);

        mainView.setOnTouchListener(new OnSwipeTouchListener(OfflineFlashCardActivity.this) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (currentQuestionNumber > 1) {
                    currentQuestionNumber--;
                    Study currentStudy = studyArrayList.get(currentQuestionNumber - 1);
                    populateView(currentStudy);
                }
                toggleLeftRightIcon();
            }

            public void onSwipeLeft() {
                if (currentQuestionNumber < studyArrayList.size()) {
                    currentQuestionNumber++;
                    Study currentStudy = studyArrayList.get(currentQuestionNumber - 1);
                    populateView(currentStudy);
                }
                toggleLeftRightIcon();
            }

            public void onSwipeBottom() {
            }

        });
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
                if (!studyArrayList.isEmpty()) {
                    final QuestionSelectDialog questionSelectDialog = new QuestionSelectDialog(OfflineFlashCardActivity.this);
                    questionSelectDialog.setActivity(OfflineFlashCardActivity.this);
                    questionSelectDialog.setQuestionArray(studyArrayList);
                    questionSelectDialog.setOnQuestionSelected(new QuestionGridSelectAdapter.OnQuestionSelected() {
                        @Override
                        public void QuestionSelected(int questionNumber) {
                            currentQuestionNumber = questionNumber;
                            toggleLeftRightIcon();
                            Study currentStudy = studyArrayList.get(questionNumber - 1);
                            populateView(currentStudy);
                            questionSelectDialog.dismiss();
                        }
                    });
                    questionSelectDialog.show();
                }
            }
        });

        toggleLeftRightIcon();
        if (!isRevision) {
            new BGTask(new BGTask.DoInBackground() {
                @Override
                public void DoJob() {

                    String query = "";

                    for (int i = 0; i < selectedSubTopics.size(); i++) {
                        if (i != selectedSubTopics.size() - 1) {
                            query += "subcategory = '" + selectedSubTopics.get(i).toUpperCase() + "' OR ";
                        } else {
                            query += "subcategory = '" + selectedSubTopics.get(i).toUpperCase() + "'";
                        }
                    }

                    studyArrayList = (ArrayList<Study>) SugarRecord.findWithQuery(Study.class, "Select * from Study where " + query);
                }
            }, new BGTask.DoPostExecute() {
                @Override
                public void DoJob() {
                    handleAfterData();

                }
            }, OfflineFlashCardActivity.this, true).execute(new Void[0]);
        } else {
            new BGTask(new BGTask.DoInBackground() {
                @Override
                public void DoJob() {
                    List<StudyRevision> revisions = SugarRecord.listAll(StudyRevision.class);
                    studyArrayList = new ArrayList<>();
                    for (StudyRevision revision : revisions) {
                        studyArrayList.add(Select.from(Study.class).where(Condition.prop("study_id").eq(revision.getQuestionId())).first());
                    }
                }
            }, new BGTask.DoPostExecute() {
                @Override
                public void DoJob() {
                    handleAfterData();
                }
            }, OfflineFlashCardActivity.this, true).execute(new Void[0]);
        }
    }


    public void populateView(final Study study) {
        wvAnswer.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        if (Select.from(StudyRevision.class).where(Condition.prop("id").eq(study.getId())).count() != 0) {
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
                if (Select.from(StudyRevision.class).where(Condition.prop("id").eq(study.getId())).count() != 0) {
                    revision.delete();
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
            tvQuestion.setText(Html.fromHtml("<strong>" + (currentQuestionNumber) + ". </strong>" + study.getQuestion(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvQuestion.setText(Html.fromHtml("<strong>" + (currentQuestionNumber) + ". </strong>" + study.getQuestion()));
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
        Study currentStudy = studyArrayList.get(currentQuestionNumber - 1);
        populateView(currentStudy);
        navLeft.setVisibility(View.INVISIBLE);
        navRight.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.VISIBLE);
        btnExit.setVisibility(View.VISIBLE);
        navLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionNumber > 1) {
                    currentQuestionNumber--;
                    Study currentStudy = studyArrayList.get(currentQuestionNumber - 1);
                    populateView(currentStudy);
                }
                toggleLeftRightIcon();
            }
        });
        navRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionNumber < studyArrayList.size()) {
                    currentQuestionNumber++;
                    Study currentStudy = studyArrayList.get(currentQuestionNumber - 1);
                    populateView(currentStudy);
                }
                toggleLeftRightIcon();
            }
        });

    }

    private void toggleLeftRightIcon() {
        if (currentQuestionNumber == studyArrayList.size()) {
            navRight.setVisibility(View.INVISIBLE);
        } else {
            navRight.setVisibility(View.VISIBLE);
        }
        if (currentQuestionNumber == 1) {
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
