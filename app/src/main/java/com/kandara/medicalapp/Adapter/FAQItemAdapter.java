package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kandara.medicalapp.Model.FAQItem;
import com.kandara.medicalapp.R;

import java.util.ArrayList;

/**
 * Created by abina on 8/2/2018.
 */

public class FAQItemAdapter extends BaseAdapter {

    ArrayList<FAQItem> faqItems;

    Activity activity;

    public FAQItemAdapter(ArrayList<FAQItem> faqItems, Activity activity) {
        this.faqItems = faqItems;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return faqItems.size();
    }

    @Override
    public Object getItem(int i) {
        return faqItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = activity.getLayoutInflater().inflate(R.layout.item_faq, viewGroup, false);
        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        TextView tvAnswer = view.findViewById(R.id.tvAnswer);

        FAQItem faqItem=faqItems.get(i);
        tvQuestion.setText(faqItem.getQuestion());
        tvAnswer.setText(faqItem.getAnswer());
        return view;
    }
}
