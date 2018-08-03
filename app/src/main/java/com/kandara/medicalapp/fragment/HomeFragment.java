package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.os.Handler;
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
import com.kandara.medicalapp.Model.BannerTopic;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.Indicator.DotsIndicator;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.kandara.medicalapp.activity.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RoundedImageView imgTopic;
    ArrayList<BannerTopic> newsArrayList;
    ViewPager topicsViewPager;
    CustomPagerAdapter customPagerAdapter;
    DotsIndicator dotsIndicator;

    Button btnExploreButton;
    Button btnUpgrade;
    Button btnDiscussion;
    Button btnExploreMCQ;


    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

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

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                topicsViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        dotsIndicator.setViewPager(topicsViewPager);
        btnExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.NavigateDrawer(1);
            }
        });


        btnDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity= (MainActivity) getActivity();
                mainActivity.NavigateDrawer(6);

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
