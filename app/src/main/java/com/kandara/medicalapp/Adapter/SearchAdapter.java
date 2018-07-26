package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.activity.FlashCardActivity;
import com.kandara.medicalapp.activity.MCQActivity;

import java.util.ArrayList;

/**
 * Created by abina on 5/6/2018.
 */

public class SearchAdapter extends BaseAdapter {

    ArrayList<?> questionList;
    Activity activity;
    String searchTxt;


    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    public SearchAdapter(ArrayList<?> questionList, Activity activity) {
        this.questionList = questionList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int i) {
        return questionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=activity.getLayoutInflater().inflate(R.layout.item_search_list, viewGroup, false);
            TextView tvQuestion = (view.findViewById(R.id.tvComment));
            WebView wvAnswer = view.findViewById(R.id.tvAnswer);
            Object object=getItem(i);
            if(object instanceof Study){
                final Study study = (Study) object;
                String question=study.getQuestion();
                String answer=study.getAnswer();
                if(searchTxt!=null){
                    question=question.replaceAll("(?i)"+searchTxt, "<span style=\"background-color: #FFFF00\">"+searchTxt+"</span>");
                    answer=answer.replaceAll("(?i)"+searchTxt, "<span style=\"background-color: #FFFF00\">"+searchTxt+"</span>");
                }
                answer=answer.replaceAll("font-size: 10.0pt; ", "");
                answer="\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<style>\n" +
                        "*{\n" +
                        "  font-size:15px;\n" +
                        " font-family: Arial;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n"+answer+"</body>\n" +
                        "</html>";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + question, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + question));
                }
                wvAnswer.setWebChromeClient(new WebChromeClient());
                wvAnswer.getSettings().setJavaScriptEnabled(true);
                wvAnswer.setBackgroundColor(Color.TRANSPARENT);
                wvAnswer.loadData(answer, "text/html; charset=utf-8", "UTF-8");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(activity, FlashCardActivity.class);
                        intent.putExtra("questionNumber", study.getQuestionNumber());
                        activity.startActivity(intent);
                    }
                });
            }else{
                wvAnswer.setVisibility(View.GONE);
                final MCQ mcq= (MCQ) object;

                String question=mcq.getQuestion();
                if(searchTxt!=null){
                    question=question.replaceAll("(?i)"+searchTxt, "<span style=\"background-color: #FFFF00\">"+searchTxt+"</span>");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvQuestion.setText(Html.fromHtml("<strong>" + (mcq.getQuestionNumber()) + ". </strong>" + question, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvQuestion.setText(Html.fromHtml("<strong>" + (mcq.getQuestionNumber()) + ". </strong>" + question));
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(activity, MCQActivity.class);
                        intent.putExtra("questionNumber", mcq.getQuestionNumber());
                        activity.startActivity(intent);
                    }
                });

            }
        }
        return view;
    }
}
