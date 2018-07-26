package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Created by abina on 4/30/2018.
 */

public class StudyModeAdapter extends BaseAdapter {

    ArrayList<Study> studyArrayList;
    Activity activity;
    String searchTxt;

    public StudyModeAdapter(ArrayList<Study> studyArrayList, Activity activity) {
        this.studyArrayList = studyArrayList;
        this.activity = activity;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    @Override
    public int getCount() {
        return studyArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return studyArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View cv, ViewGroup viewGroup) {
            cv = activity.getLayoutInflater().inflate(R.layout.item_study_mode_list, viewGroup, false);
            TextView tvQuestion = (cv.findViewById(R.id.tvComment));
            WebView wvAnswer = cv.findViewById(R.id.tvAnswer);
            Study study = studyArrayList.get(i);

            ImageView image = cv.findViewById(R.id.image);


            if (URLUtil.isValidUrl(study.getImageUrl())) {
                Picasso.with(activity).load(study.getImageUrl()).into(image);
            }
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
            //wvAnswer.loadDataWithBaseURL("", answer, "text/html", "UTF-8", "");

        return cv;
    }
}
