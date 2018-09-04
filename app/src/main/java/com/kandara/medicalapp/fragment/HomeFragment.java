package com.kandara.medicalapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kandara.medicalapp.Adapter.CustomPagerAdapter;
import com.kandara.medicalapp.Adapter.RecentNewsRecyclerAdapter;
import com.kandara.medicalapp.Adapter.StudyActRecyclerAdapter;
import com.kandara.medicalapp.Model.BannerTopic;
import com.kandara.medicalapp.Model.SliderItem;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.Indicator.DotsIndicator;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.Util.UtilDialog;
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
    ArrayList<SliderItem> sliderItemArrayListM;
    ViewPager topicsViewPager;
    CustomPagerAdapter customPagerAdapter;
    DotsIndicator dotsIndicator;

    Button btnExploreButton;
    Button btnDiscussion;
    Button btnExploreMCQ;

    LinearLayout upgradeLayout;
    Button btnUpgrade;

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
        sliderItemArrayListM=new ArrayList<>();
        sliderItemArrayListM.add(new SliderItem("Upgrade today to Premium user to get following additional features: ", "1.\tFull content access. \n" +
                "2.\tAbility to download the contents for offline view.\n" +
                "3.\tSearch option.", new SliderItem.ClickListener() {
            @Override
            public void onClick() {
                UtilDialog.showUpgradeDialog(getActivity());
            }
        }, "https://images.pexels.com/photos/355934/pexels-photo-355934.jpeg?auto=compress&cs=tinysrgb&h=350"));
        sliderItemArrayListM.add(new SliderItem("PG Access", "MD/MS Entrance Guide Book", new SliderItem.ClickListener() {
            @Override
            public void onClick() {
                startActivity(getOpenFacebookIntent(getContext()));
            }
        }, "https://scontent.fktm3-1.fna.fbcdn.net/v/t1.0-9/15400462_1262817427072969_4386424617494395300_n.png?_nc_cat=0&oh=9ac38b195e90bd1b0bb1b9b2a1b6c777&oe=5C14087E"));
        sliderItemArrayListM.add(new SliderItem("Your advertisement here", "Contact us in smartpgnepal@gmail.com for your advertisement", new SliderItem.ClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "smartpgnepal@gmail.com" });
                startActivity(Intent.createChooser(intent, ""));
            }
        }, "https://camblycontent.files.wordpress.com/2017/02/advertising-word-block.jpg?w=700"));
        customPagerAdapter = new CustomPagerAdapter(getContext(), sliderItemArrayListM);
        upgradeLayout=view.findViewById(R.id.upgradeLayout);
        btnUpgrade=view.findViewById(R.id.btnUpgrade);
        if(AccountManager.isUserPremium(getActivity())){
            upgradeLayout.setVisibility(View.GONE);
        }

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilDialog.showUpgradeDialog(getActivity());
            }
        });
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
                String url = sliderItemArrayListM.get(position).getPhotoUrl();
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

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/113921451962578"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/<user_name_here>"));
        }
    }

}
