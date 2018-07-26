package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kandara.medicalapp.Model.GridQuestion;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.View.SquareRelativeLayout;
import com.kandara.medicalapp.fragment.MCQFragment;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abina on 5/1/2018.
 */

public class QuestionGridSelectAdapter extends BaseAdapter {

    ArrayList<?> questionsArray;
    Activity activity;

    OnQuestionSelected onQuestionSelected;

    public QuestionGridSelectAdapter(ArrayList<?> questionsArray, Activity activity) {
        this.questionsArray = questionsArray;
        this.activity = activity;
    }

    public OnQuestionSelected getOnQuestionSelected() {
        return onQuestionSelected;
    }

    public void setOnQuestionSelected(OnQuestionSelected onQuestionSelected) {
        this.onQuestionSelected = onQuestionSelected;
    }

    @Override
    public int getCount() {
        return questionsArray.size();
    }

    @Override
    public Object getItem(int i) {
        return questionsArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View cv, ViewGroup viewGroup) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_grid_select_question, viewGroup, false);
        final SquareRelativeLayout linSquareQuestion = view.findViewById(R.id.linQuestion);
        TextView tvQuestionNumber = view.findViewById(R.id.tvQuestionNumber);
        final Object object = questionsArray.get(i);
        if (object instanceof MCQ) {
            final MCQ mcq = (MCQ) object;
            tvQuestionNumber.setText(mcq.getQuestionNumber() + "");
            if(isRead(mcq.getMcqId())) {
                if(isCorrect(mcq.getMcqId())) {
                    linSquareQuestion.setBackgroundResource(R.drawable.bg_mcq_correct);
                }else{
                    linSquareQuestion.setBackgroundResource(R.drawable.bg_mcq_incorrect);
                }
                tvQuestionNumber.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            }else{
                linSquareQuestion.setBackgroundResource(R.drawable.bg_white_rounded);
                tvQuestionNumber.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
            }
            linSquareQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onQuestionSelected.QuestionSelected(mcq.getQuestionNumber());
                }
            });
        } else if (object instanceof Study) {
            final Study study = (Study) object;
            tvQuestionNumber.setText(study.getQuestionNumber() + "");
            if(isRead(study.getStudyId())) {
                linSquareQuestion.setBackgroundResource(R.drawable.bg_study_start);
                tvQuestionNumber.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            }else{
                linSquareQuestion.setBackgroundResource(R.drawable.bg_white_rounded);
                tvQuestionNumber.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
            }
            linSquareQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onQuestionSelected.QuestionSelected(study.getQuestionNumber());
                }
            });
        }

        return view;
    }

    public interface OnQuestionSelected {
        void QuestionSelected(int questionNumber);
    }

    public boolean isRead(String id){
        GridQuestion question=Select.from(GridQuestion.class).where(Condition.prop("question_id").eq(id)).first();
        if(question==null){
            return false;
        }else{
            return true;
        }
    }



    public boolean isCorrect(String id){
        GridQuestion question=Select.from(GridQuestion.class).where(Condition.prop("question_id").eq(id)).first();
        if(question==null){
            return false;
        }else{
            return question.isCorrect();
        }
    }
}
