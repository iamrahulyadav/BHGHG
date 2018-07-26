package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kandara.medicalapp.Adapter.CustomPagerAdapter;
import com.kandara.medicalapp.Adapter.RecentNewsRecyclerAdapter;
import com.kandara.medicalapp.Adapter.StudyActRecyclerAdapter;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.Indicator.DotsIndicator;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.kandara.medicalapp.activity.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RoundedImageView imgTopic;
    ArrayList<Topic> newsArrayList;
    ViewPager topicsViewPager;
    CustomPagerAdapter customPagerAdapter;
    DotsIndicator dotsIndicator;

    Button btnExploreButton;
    Button btnUpgrade;
    Button btnDiscussion;
    Button btnExploreMCQ;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        newsArrayList = JsondataUtil.getBannerTopics(getContext());
        customPagerAdapter = new CustomPagerAdapter(getContext(), newsArrayList);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
        btnExploreMCQ=view.findViewById(R.id.btnExploreMCQ);
        topicsViewPager = view.findViewById(R.id.topicsViewPager);
        imgTopic = view.findViewById(R.id.img_topic);
        btnExploreButton = view.findViewById(R.id.btnExploreStudy);
        btnUpgrade = view.findViewById(R.id.btnUpgrade);
        btnDiscussion = view.findViewById(R.id.btnDiscussion);
        topicsViewPager.setAdapter(customPagerAdapter);
        topicsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String url = newsArrayList.get(position).getPhotoUrl();
                Picasso.with(getActivity()).load(url).into(imgTopic);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotsIndicator.setViewPager(topicsViewPager);
        btnExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.NavigateDrawer(1);
            }
        });

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        btnDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.NavigateDrawer(8);

            }
        });

        btnExploreMCQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.NavigateDrawer(3);

            }
        });

        return view;
    }

}
