package com.kandara.medicalapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.MCQRevision;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.StudyRevision;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.View.FilterView;
import com.kandara.medicalapp.activity.FlashCardActivity;
import com.kandara.medicalapp.activity.MCQActivity;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

/**
 * A simple {@link Fragment} subclass.
 */
public class RevisionFragment extends Fragment {

    TextView tvTotalQuestion;
    Handler handler;
    Button startBtn;

    TextView tvTotalQuestion2;
    Handler handler2;
    Button startBtn2;

    public RevisionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revision, container, false);
        initMCQ(view);
        initStudy(view);
        return view;
    }

    private void initMCQ(View view) {
        startBtn = view.findViewById(R.id.startBtn);
        tvTotalQuestion = view.findViewById(R.id.totalQuestions);


        tvTotalQuestion.setText("Total Questions : " + Select.from(MCQRevision.class).list().size());
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return false;
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SugarRecord.count(MCQRevision.class) > 0) {
                    Intent intent = new Intent(getActivity(), MCQActivity.class);
                    intent.putExtra("isStudy", false);
                    intent.putExtra("revision", true);
                    startActivity(intent);
                }
            }
        });
    }

    private void initStudy(View view) {

        tvTotalQuestion2 = view.findViewById(R.id.totalQuestions2);


        tvTotalQuestion2.setText("Total Questions : " + Select.from(StudyRevision.class).list().size());
        startBtn2 = view.findViewById(R.id.startBtn2);
        handler2 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return false;
            }
        });
        startBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SugarRecord.count(StudyRevision.class) > 0) {
                    Intent intent = new Intent(getContext(), FlashCardActivity.class);
                    intent.putExtra("revision", true);
                    startActivity(intent);
                }
            }
        });
    }

}
