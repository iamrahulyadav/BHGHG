package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;

import java.util.List;

/**
 * Created by ravitmg on 12/01/18.
 */

public class StudyActRecyclerAdapter extends RecyclerView.Adapter<StudyActRecyclerAdapter.ViewHolder> {
    Context context;
    List<Topic> topicList;

    public StudyActRecyclerAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_act_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        holder.title.setText(topic.getTitle());

    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_study_topic);
        }
    }
}