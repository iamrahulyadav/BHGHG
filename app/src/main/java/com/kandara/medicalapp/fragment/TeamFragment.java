package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kandara.medicalapp.Adapter.TeamMemberAdapter;
import com.kandara.medicalapp.Model.TeamMemberItem;
import com.kandara.medicalapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {



    ArrayList<TeamMemberItem> teamMemberItemArrayList;
    ListView recyclerView;
    public TeamFragment() {
        // Required empty public constructor
    }

    public void setTeamMemberItemArrayList(ArrayList<TeamMemberItem> teamMemberItemArrayList) {
        this.teamMemberItemArrayList = teamMemberItemArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_team, container, false);
        recyclerView = view.findViewById(R.id.study_listView);
        TeamMemberAdapter teamMemberAdapter=new TeamMemberAdapter(teamMemberItemArrayList, getActivity());
        recyclerView.setAdapter(teamMemberAdapter);
        return view;
    }

}
