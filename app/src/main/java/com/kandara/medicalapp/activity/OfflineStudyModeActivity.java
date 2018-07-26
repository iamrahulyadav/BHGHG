package com.kandara.medicalapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import com.kandara.medicalapp.Adapter.StudyModeSSSSAdapter;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.SubchapterList;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.View.headerlistview.HeaderListView;
import com.orm.SugarRecord;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OfflineStudyModeActivity extends AppCompatActivity {


    HeaderListView listView;
    EditText searchEditText;

    ArrayList<SubchapterList> studyList;
    ArrayList<SubchapterList> searchList;


    ArrayList<Study> studyArrayList;

    ArrayList<Study> searchQueryList;
    AppCompatImageView searchIcon;

    ArrayList<String> selectedSubTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studymode);
        updateToolbarAndStatusBar();
        studyArrayList = new ArrayList<>();
        selectedSubTopics=getIntent().getStringArrayListExtra("subTopics");

        Log.e("WWWW", selectedSubTopics.toString());
        listView = findViewById(R.id.studyListView);
        searchEditText = findViewById(R.id.searchEdittext);
        searchIcon = findViewById(R.id.searchIcon);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTxt = searchEditText.getText().toString();
                if (!searchTxt.isEmpty()) {
                    getSearchStudyData(searchTxt);
                } else {
                    getStudyData();
                }
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchTxt = searchEditText.getText().toString();
                    if (!searchTxt.isEmpty()) {
                        getSearchStudyData(searchTxt);
                    } else {
                        getStudyData();
                    }
                    return true;
                }
                return false;
            }
        });
        getStudyData();

    }


    public void updateToolbarAndStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.main_bg_color));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void getStudyData() {
        new BGTask(new BGTask.DoInBackground() {
            @Override
            public void DoJob() {

                /*
                Condition[] conditions=new Condition[selectedSubTopics.size()];

                for(int i=0; i<selectedSubTopics.size(); i++){
                    conditions[i]=Condition.prop("subcategory").eq(selectedSubTopics.get(i));
                }

                studyArrayList= (ArrayList<Study>) Select.from(Study.class).where(Condition.prop("subcategory").eq("PROTOZOAL INFECTIONS")).list();

*/
                String query="";
                for(int i=0; i<selectedSubTopics.size(); i++){
                    if(i!=selectedSubTopics.size()-1){
                        query+="subcategory = '"+selectedSubTopics.get(i).toUpperCase()+"' OR ";
                    }else{
                        query+="subcategory = '"+selectedSubTopics.get(i).toUpperCase()+"'";
                    }
                }

                Log.e("QUERYY", query);

                studyArrayList = (ArrayList<Study>) SugarRecord.findWithQuery(Study.class, "Select * from Study where "+query);
                Collections.reverse(studyArrayList);

            }
        }, new BGTask.DoPostExecute() {
            @Override
            public void DoJob() {

                Collections.sort(studyArrayList, new CustomComparator());
                populateHashMap();
                StudyModeSSSSAdapter studyModeSSSSAdapter=new StudyModeSSSSAdapter(studyList, OfflineStudyModeActivity.this);
                listView.setAdapter(studyModeSSSSAdapter);

            }
        }, OfflineStudyModeActivity.this, true).execute(new Void[0]);
    }


    public class CustomComparator implements Comparator<Study> {
        @Override
        public int compare(Study study1, Study study2) {
            return study1.getQuestionNumber() - study2.getQuestionNumber();
        }
    }

    private void getSearchStudyData(final String searchTxt) {


        new BGTask(new BGTask.DoInBackground() {
            @Override
            public void DoJob() {
                searchQueryList = new ArrayList<>();
                for (Study study : studyArrayList) {
                    if (StringUtils.containsIgnoreCase(study.getQuestion(), searchTxt) || StringUtils.containsIgnoreCase(study.getAnswer(), searchTxt)) {
                        searchQueryList.add(study);
                    }
                }
            }
        }, new BGTask.DoPostExecute() {
            @Override
            public void DoJob() {

                Collections.sort(studyArrayList, new CustomComparator());
                populateHashMap();
                StudyModeSSSSAdapter studyModeSSSSAdapter=new StudyModeSSSSAdapter(searchList, OfflineStudyModeActivity.this);
                studyModeSSSSAdapter.setSearchTxt(searchTxt);
                listView.setAdapter(studyModeSSSSAdapter);

            }
        }, OfflineStudyModeActivity.this, true).execute(new Void[0]);
    }

    private void populateHashMap() {
        studyList = new ArrayList<>();
        searchList = new ArrayList<>();

        ArrayList<String> subChapterArrayList = new ArrayList<>();
        for (Study study : studyArrayList) {
            if (!subChapterArrayList.contains(study.getSubcategory())) {
                subChapterArrayList.add(study.getSubcategory());
            }
        }

        for (String subchaptername : subChapterArrayList) {
            SubchapterList subchapterList = new SubchapterList(subchaptername);
            for (Study study : studyArrayList) {
                if (study.getSubcategory().equalsIgnoreCase(subchaptername)) {
                    subchapterList.addStudy(study);
                }
            }
            studyList.add(subchapterList);
        }

        if(searchQueryList!=null) {
            for (String subchaptername : subChapterArrayList) {
                SubchapterList subchapterList = new SubchapterList(subchaptername);
                for (Study study : searchQueryList) {
                    if (study.getSubcategory().equalsIgnoreCase(subchaptername)) {
                        subchapterList.addStudy(study);
                    }
                }
                searchList.add(subchapterList);
            }
        }
    }


}
