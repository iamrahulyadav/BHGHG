package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;

import java.util.ArrayList;

/**
 * Created by abina on 2/11/2018.
 */


public class StudyDataAdapter extends ArrayAdapter<Study> {

    TextView tvQuestion;
    LinearLayout linAnswers;

    public StudyDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent) {
        //supply the layout for your card
        Study study = getItem(position);
        tvQuestion = (contentView.findViewById(R.id.tvComment));
        linAnswers=contentView.findViewById(R.id.linAnswers);
        tvQuestion.setText(study.getQuestion());


        return contentView;
    }


}
