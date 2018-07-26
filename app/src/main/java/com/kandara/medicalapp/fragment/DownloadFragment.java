package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.BookStudyAdapter;
import com.kandara.medicalapp.Adapter.OfflineBookStudyAdapter;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {



    ListView recyclerView;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        recyclerView = view.findViewById(R.id.study_listView);
        ArrayList<String> studyTopics=JsondataUtil.getStudyTopics(getActivity());
        ArrayList<String> downloadStudyTopics=new ArrayList<>();
        for(String topic:studyTopics){

            final ArrayList<String> subTopics = JsondataUtil.getSubTopics(topic, getContext());
            String localQuery = "";
            for (int k = 0; k < subTopics.size(); k++) {
                if (k != subTopics.size() - 1) {
                    localQuery += "subcategory = '" + subTopics.get(k).toUpperCase() + "' OR ";
                } else {
                    localQuery += "subcategory = '" + subTopics.get(k).toUpperCase() + "'";
                }
            }
            int totalSaved = (int) SugarRecord.count(Study.class, localQuery, null);
            if (totalSaved != 0) {
                downloadStudyTopics.add(topic);
            }
        }
        OfflineBookStudyAdapter bookStudyAdapter = new OfflineBookStudyAdapter(downloadStudyTopics, getActivity());
        recyclerView.setAdapter(bookStudyAdapter);

        return view;
    }

}
