package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.activity.FlashCardActivity;
import com.kandara.medicalapp.activity.MCQActivity;
import com.kandara.medicalapp.activity.StudyModeActivity;

import java.util.ArrayList;

/**
 * Created by abina on 5/6/2018.
 */

public class StudySearchAdapter extends RecyclerView.Adapter<StudySearchAdapter.ViewHolder> {

    ArrayList<?> questionList;
    Activity activity;
    String searchTxt;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvQuestion;
        WebView wvAnswer;

        ViewHolder(View itemView) {
            super(itemView);


            tvQuestion = itemView.findViewById(R.id.tvComment);
            wvAnswer = itemView.findViewById(R.id.tvAnswer);

            tvQuestion.setOnClickListener(this);
            wvAnswer.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tvAnswer:
                    Intent intent=new Intent(activity, FlashCardActivity.class);
                    intent.putExtra("questionNumber", ((Study)questionList.get((int) getItemId())).getQuestionNumber());
                    activity.startActivity(intent);
                    return;

            }
        }
    }


    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    public StudySearchAdapter(ArrayList<?> questionList, Activity activity) {
        this.questionList = questionList;
        this.activity = activity;
    }


    @Override
    public StudySearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new StudySearchAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(StudySearchAdapter.ViewHolder holder, int position) {

        final Study study = (Study) questionList.get(position);
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
            holder.tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + question, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + question));
        }
        holder.wvAnswer.setWebChromeClient(new WebChromeClient());
        holder.wvAnswer.getSettings().setJavaScriptEnabled(true);
        holder.wvAnswer.setBackgroundColor(Color.TRANSPARENT);
        holder.wvAnswer.loadData(answer, "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

}
