package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.BookStudyAdapter;
import com.kandara.medicalapp.Adapter.StudyRecyclerAdapter;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.SubTopic;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainStudyFragment extends Fragment {



    ListView recyclerView;

    public MainStudyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        recyclerView = view.findViewById(R.id.study_listView);
        BookStudyAdapter bookStudyAdapter = new BookStudyAdapter(JsondataUtil.getStudyTopics(getActivity()), getActivity());
        recyclerView.setAdapter(bookStudyAdapter);

        return view;
    }

}
