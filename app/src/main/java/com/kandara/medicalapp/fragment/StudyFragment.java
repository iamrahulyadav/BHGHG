package com.kandara.medicalapp.fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudyFragment extends Fragment {


    List<Study> studies;
    private LinearLayout btnAddToRevision, btnMoreInfo, options;
    private ImageView image;
    private TextView tvQuestion;
    private TextView tvAddToRevision;
    private WebView wvAnswer;
    private Study study;
    private Handler handler;
    private int WAITING_TIME = 1000;
    private boolean questionSelected = false;
    int answerindex = 0;


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public StudyFragment() {
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_main, container, false);
        studies = SugarRecord.listAll(Study.class);
        tvQuestion = (view.findViewById(R.id.tvComment));
        wvAnswer = view.findViewById(R.id.tvAnswer);

        btnMoreInfo = view.findViewById(R.id.btnMoreInfo);
        btnAddToRevision = view.findViewById(R.id.btnAddtoRevision);
        tvAddToRevision = view.findViewById(R.id.tvAddTorevision);
        options = view.findViewById(R.id.options);
        image = view.findViewById(R.id.image);

        if(contains(studies, study)){
            tvAddToRevision.setText("Revision -");
        }else{
            tvAddToRevision.setText("Revision +");
        }

        btnAddToRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contains(studies, study)){
                    SugarRecord.deleteAll(Study.class, "question = ?", study.getQuestion());
                    studies = SugarRecord.listAll(Study.class);
                    tvAddToRevision.setText("Revision +");
                    Toast.makeText(getActivity(), "Removed from revision", Toast.LENGTH_SHORT).show();
                }else {
                    study.save();
                    studies = SugarRecord.listAll(Study.class);
                    tvAddToRevision.setText("Revision -");
                    Toast.makeText(getActivity(), "Added to Revision", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (URLUtil.isValidUrl(study.getImageUrl())) {
            Picasso.with(getContext()).load(study.getImageUrl()).into(image);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvAnswer.getVisibility() == View.GONE) {
                    wvAnswer.setVisibility(View.VISIBLE);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + study.getQuestion(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + study.getQuestion()));
        }
        wvAnswer.getSettings().setJavaScriptEnabled(true);
        wvAnswer.setBackgroundColor(Color.TRANSPARENT);
        wvAnswer.loadDataWithBaseURL("", study.getAnswer(), "text/html", "UTF-8", "");
        return view;
    }

    public boolean contains(List<Study> objects, Study object){
        for(Study newObject:objects){
            if(newObject.getQuestion().equalsIgnoreCase(object.getQuestion())){
                return true;
            }
        }
        return false;
    }

    public void scale(float scaleX) {
        getView().setScaleY(scaleX);
        getView().setScaleX(scaleX);
        getView().setAlpha(scaleX);
        getView().invalidate();
    }


}
