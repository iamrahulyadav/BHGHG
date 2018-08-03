package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.OfflineBookStudyAdapter;
import com.kandara.medicalapp.Adapter.TeamMemberAdapter;
import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.Model.TeamMemberItem;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {



    ArrayList<TeamMemberItem> teamMemberItemArrayList;
    ListView recyclerView;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        recyclerView = view.findViewById(R.id.study_listView);
        teamMemberItemArrayList=new ArrayList<>();
        teamMemberItemArrayList.add(new TeamMemberItem("Abinash Neupane", false, R.drawable.ic_addphoto, "Developer", "abinash.neupane123@gmail.com"));

        TeamMemberAdapter teamMemberAdapter=new TeamMemberAdapter(teamMemberItemArrayList, getActivity());
        recyclerView.setAdapter(teamMemberAdapter);
        return view;
    }

}
