package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kandara.medicalapp.Model.BannerTopic;
import com.kandara.medicalapp.Model.News;
import com.kandara.medicalapp.Model.SliderItem;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;

import java.util.List;

/**
 * Created by ravitmg on 9/01/18.
 */

public class CustomPagerAdapter extends PagerAdapter {
    Context context;
    List<SliderItem> sliderItemList;

    public CustomPagerAdapter(Context context, List<SliderItem> sliderItemList) {
        this.context = context;
        this.sliderItemList = sliderItemList;
    }

    @Override
    public int getCount() {
        return sliderItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final SliderItem sliderItem=sliderItemList.get(position);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_model, container, false);
        TextView title = itemView.findViewById(R.id.tv_topic);
        TextView content = itemView.findViewById(R.id.tv_content);
        title.setText(sliderItem.getTopic());
        content.setText(sliderItem.getContent());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderItem.getClickListener().onClick();
            }
        });
        container.addView(itemView);
        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
