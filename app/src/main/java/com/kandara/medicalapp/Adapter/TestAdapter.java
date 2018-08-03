package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kandara.medicalapp.Model.Test;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.activity.TestActivity;

import java.util.ArrayList;

/**
 * Created by abina on 4/25/2018.
 */

public class TestAdapter extends BaseAdapter {

    ArrayList<Test> testArrayList;
    Activity activity;

    public TestAdapter(ArrayList<Test> testArrayList, Activity activity) {
        this.testArrayList = testArrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return testArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return testArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View cv, ViewGroup viewGroup) {
        if(cv==null) {
            cv = activity.getLayoutInflater().inflate(R.layout.item_test, viewGroup, false);

            final Test test = testArrayList.get(i);

            TextView tvTitle = cv.findViewById(R.id.test_title);
            TextView tvDesc = cv.findViewById(R.id.test_desc);
            TextView tvTime = cv.findViewById(R.id.test_time);

            int minutes = (int) ((test.getTestDuration() / (1000 * 60)) % 60);
            tvTitle.setText(test.getName());
            tvDesc.setText(test.getDescription());
            tvTime.setText(minutes+" Min");

            Button startBtn = cv.findViewById(R.id.startBtn);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity, TestActivity.class);
                    intent.putExtra("tid", test.getTid());
                    activity.startActivity(intent);
                }
            });
        }
        return cv;
    }
}
