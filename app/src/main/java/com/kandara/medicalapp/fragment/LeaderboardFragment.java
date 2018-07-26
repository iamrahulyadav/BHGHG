package com.kandara.medicalapp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.Adapter.LeaderBoardRecyclerAdapter;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.JsondataUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment {
    RecyclerView recyclerView;
    TextView alltime, thismonth, thisweek;

    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        recyclerView = view.findViewById(R.id.rvLeaderboard);
        LeaderBoardRecyclerAdapter leaderboardRecyclerAdapter = new LeaderBoardRecyclerAdapter(getActivity(), JsondataUtil.getLeaderboard(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(leaderboardRecyclerAdapter);


        alltime = view.findViewById(R.id.tv_alltime);
        thismonth = view.findViewById(R.id.tv_thismonth);
        thisweek = view.findViewById(R.id.tv_thisweek);

        alltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alltime.setBackgroundResource(R.drawable.capsule_bg_purple);
                alltime.setTextColor(getResources().getColor(R.color.colorWhite));

                thismonth.setBackgroundResource(R.drawable.capsule_bg_gray);
                thismonth.setTextColor(getResources().getColor(R.color.colorDarkGray));

                thisweek.setBackgroundResource(R.drawable.capsule_bg_gray);
                thisweek.setTextColor(getResources().getColor(R.color.colorDarkGray));
            }
        });

        thismonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alltime.setBackgroundResource(R.drawable.capsule_bg_gray);
                alltime.setTextColor(getResources().getColor(R.color.colorDarkGray));

                thismonth.setBackgroundResource(R.drawable.capsule_bg_purple);
                thismonth.setTextColor(getResources().getColor(R.color.colorWhite));

                thisweek.setBackgroundResource(R.drawable.capsule_bg_gray);
                thisweek.setTextColor(getResources().getColor(R.color.colorDarkGray));
            }
        });

        thisweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alltime.setBackgroundResource(R.drawable.capsule_bg_gray);
                alltime.setTextColor(getResources().getColor(R.color.colorDarkGray));

                thismonth.setBackgroundResource(R.drawable.capsule_bg_gray);
                thismonth.setTextColor(getResources().getColor(R.color.colorDarkGray));

                thisweek.setBackgroundResource(R.drawable.capsule_bg_purple);
                thisweek.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });
        return view;
    }

}
