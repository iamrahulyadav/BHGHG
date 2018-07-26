package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kandara.medicalapp.Model.BannerTopic;
import com.kandara.medicalapp.Model.News;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;

import java.util.List;

/**
 * Created by ravitmg on 9/01/18.
 */

public class CustomPagerAdapter extends PagerAdapter {
    Context context;
    List<BannerTopic> mainViewPagerList;

    public CustomPagerAdapter(Context context, List<BannerTopic> mainViewPagerList) {
        this.context = context;
        this.mainViewPagerList = mainViewPagerList;
    }


    @Override
    public int getCount() {
        return mainViewPagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BannerTopic mainViewPager = mainViewPagerList.get(position);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_model, container, false);
        TextView title = itemView.findViewById(R.id.tv_topic);
        TextView content = itemView.findViewById(R.id.tv_content);
        title.setText(mainViewPager.getTitle());
        container.addView(itemView);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
