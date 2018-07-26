package com.kandara.medicalapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.GridView;

import com.kandara.medicalapp.Adapter.QuestionGridSelectAdapter;
import com.kandara.medicalapp.R;

import java.util.ArrayList;

/**
 * Created by abina on 5/1/2018.
 */

public class QuestionSelectDialog extends Dialog {

    private ArrayList<?> questionArray;
    private Activity activity;
    private Dialog dialog;
    private GridView gridView;
    private QuestionGridSelectAdapter.OnQuestionSelected onQuestionSelected;

    public void setOnQuestionSelected(QuestionGridSelectAdapter.OnQuestionSelected onQuestionSelected) {
        this.onQuestionSelected = onQuestionSelected;
    }

    public void setQuestionArray(ArrayList<?> questionArray) {
        this.questionArray = questionArray;
    }

    public QuestionSelectDialog(@NonNull Context context) {
        super(context);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.question_select_dialog);
        gridView=findViewById(R.id.questionGridView);
        QuestionGridSelectAdapter adapter=new QuestionGridSelectAdapter(questionArray, activity);
        adapter.setOnQuestionSelected(onQuestionSelected);
        gridView.setAdapter(adapter);
    }

}
