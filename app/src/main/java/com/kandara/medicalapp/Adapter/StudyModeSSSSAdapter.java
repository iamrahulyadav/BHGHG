package com.kandara.medicalapp.Adapter;
/**
 * Created by abina on 4/30/2018.
 */
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.SubchapterList;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.UtilDialog;
import com.kandara.medicalapp.View.headerlistview.SectionAdapter;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class StudyModeSSSSAdapter extends SectionAdapter {


    ArrayList<SubchapterList> studyList;
    Activity activity;
    String searchTxt;

    public StudyModeSSSSAdapter(ArrayList<SubchapterList> studyList, Activity activity) {
        this.studyList = studyList;
        this.activity = activity;
    }

    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    @Override
    public int numberOfSections() {
        return studyList.size();
    }

    @Override
    public int numberOfRows(int section) {
        if (section >= 0) {
            return studyList.get(section).getStudyArrayList().size();
        }
        return 0;
    }

    @Override
    public Object getRowItem(int section, int row) {
        return studyList.get(section).getStudyArrayList().get(row);
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    @Override
    public View getRowView(int section, int row, View cv, ViewGroup parent) {
        cv = activity.getLayoutInflater().inflate(R.layout.item_study_mode_list, parent, false);
        TextView tvQuestion = (cv.findViewById(R.id.tvComment));
        WebView wvAnswer = cv.findViewById(R.id.tvAnswer);
        Study study = studyList.get(section).getStudyArrayList().get(row);

        ImageView image = cv.findViewById(R.id.image);
        if (URLUtil.isValidUrl(study.getImageUrl())) {
            Picasso.with(activity).load(study.getImageUrl()).into(image);
        }
        String question = study.getQuestion();
        String answer = study.getAnswer();
        if (searchTxt != null) {
            question = question.replaceAll("(?i)" + searchTxt, "<span style=\"background-color: #FFFF00\">" + searchTxt + "</span>");
            answer = answer.replaceAll("(?i)" + searchTxt, "<span style=\"background-color: #FFFF00\">" + searchTxt + "</span>");
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + question, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvQuestion.setText(Html.fromHtml("<strong>" + (study.getQuestionNumber()) + ". </strong>" + question));
        }
        if(study.getSubcategory().equalsIgnoreCase("Premium Account")){
            tvQuestion.setVisibility(View.GONE);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilDialog.showUpgradeDescDialog(activity);
                }
            });
        }
        wvAnswer.setWebChromeClient(new WebChromeClient());
        wvAnswer.getSettings().setJavaScriptEnabled(true);
        wvAnswer.setBackgroundColor(Color.TRANSPARENT);
        wvAnswer.loadData(answer, "text/html; charset=utf-8", "UTF-8");
        //wvAnswer.loadDataWithBaseURL("", answer, "text/html", "UTF-8", "");
        return cv;
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    @Override
    public int getSectionHeaderItemViewType(int section) {
        return section % 2;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        convertView = activity.getLayoutInflater().inflate(R.layout.item_study_mode_title, parent, false);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(studyList.get(section).getSubChapterName());

        return convertView;
    }

    @Override
    public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
        super.onRowItemClick(parent, view, section, row, id);
    }
}
