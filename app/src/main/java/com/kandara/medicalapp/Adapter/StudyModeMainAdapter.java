package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.kandara.medicalapp.Model.SubchapterList;
import com.kandara.medicalapp.R;

import java.util.ArrayList;


public class StudyModeMainAdapter extends BaseAdapter {

    Activity activity;

    ArrayList<SubchapterList> studyList;

    public StudyModeMainAdapter(Activity activity, ArrayList<SubchapterList> studyList) {
        this.activity = activity;
        this.studyList = studyList;
    }

    String searchTxt;





    public void setSearchTxt(String searchTxt) {
        this.searchTxt = searchTxt;
    }

    @Override
    public int getCount() {
        return studyList.size();
    }

    @Override
    public Object getItem(int i) {
        return studyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View cv, ViewGroup viewGroup) {

        cv =activity. getLayoutInflater().inflate(R.layout.item_main_study_mode, viewGroup, false);
        TextView tvTitle=cv.findViewById(R.id.tvComment);
        ListView listView=cv.findViewById(R.id.studyListView);

        SubchapterList subchapterList=studyList.get(i);
        tvTitle.setText(subchapterList.getSubChapterName());

        StudyModeAdapter studyModeAdapter = new StudyModeAdapter(subchapterList.getStudyArrayList(), activity);
        listView.setAdapter(studyModeAdapter);
        return cv;
    }
}
