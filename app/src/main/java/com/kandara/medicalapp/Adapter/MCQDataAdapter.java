package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.R;

import java.util.Random;

/**
 * Created by abina on 2/11/2018.
 */


public class MCQDataAdapter extends ArrayAdapter<MCQ> {

    TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD;
    RelativeLayout optionALayout, optionBLayout, optionCLayout, optionDLayout;

    public MCQDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, final View view, ViewGroup parent) {
        //supply the layout for your card

        MCQ mcq = getItem(position);

        final int rightAnswerpos = new Random().nextInt(4);
        tvQuestion = (view.findViewById(R.id.tvComment));
        tvOptionA = view.findViewById(R.id.tvOptionA);
        tvOptionB = view.findViewById(R.id.tvOptionB);
        tvOptionC = view.findViewById(R.id.tvOptionC);
        tvOptionD = view.findViewById(R.id.tvOptionD);

        optionALayout = view.findViewById(R.id.optionALayout);
        optionBLayout = view.findViewById(R.id.optionBLayout);
        optionCLayout = view.findViewById(R.id.optionCLayout);
        optionDLayout = view.findViewById(R.id.optionDLayout);

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

        optionALayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                highlightRightWrong(optionALayout, rightAnswerpos, 0);
            }
        });

        optionBLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highlightRightWrong(optionBLayout, rightAnswerpos, 1);
            }
        });


        optionBLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highlightRightWrong(optionCLayout, rightAnswerpos, 2);
            }
        });


        optionCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                highlightRightWrong(optionDLayout, rightAnswerpos, 3);
            }
        });


        tvQuestion.setText(mcq.getQuestion());

        return view;
    }

    private void highlightRightWrong(final View selectedOption, final int rightAnswerpos, final int currentPos) {

        selectedOption.setBackgroundResource(R.drawable.bg_options_orange);
        AlphaAnimation blinkanimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        blinkanimation.setDuration(300); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(3); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        blinkanimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rightAnswerpos == currentPos) {
                    selectedOption.setBackgroundResource(R.drawable.bg_options_correct);
                } else {
                    selectedOption.setBackgroundResource(R.drawable.bg_options_wrong);
                    switch (rightAnswerpos) {
                        case 0:
                            optionALayout.setBackgroundResource(R.drawable.bg_options_correct);
                            break;
                        case 1:
                            optionBLayout.setBackgroundResource(R.drawable.bg_options_correct);
                            break;
                        case 2:
                            optionCLayout.setBackgroundResource(R.drawable.bg_options_correct);
                            break;
                        case 3:
                            optionDLayout.setBackgroundResource(R.drawable.bg_options_correct);
                            break;
                    }
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        selectedOption.startAnimation(blinkanimation);


    }

}
