package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.activity.FlashCardActivity;
import com.kandara.medicalapp.activity.StudyModeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravitmg on 9/01/18.
 */

public class StudyRecyclerAdapter extends RecyclerView.Adapter<StudyRecyclerAdapter.ViewHolder> {
    Context context;
    List<Topic> studyObjectList;

    public StudyRecyclerAdapter(Context context, ArrayList<Topic> studyObjectList) {
        this.context = context;
        this.studyObjectList = studyObjectList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_topics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topic studyObject = studyObjectList.get(position);
        holder.topic.setText(studyObject.getTitle());
    }

    @Override
    public int getItemCount() {
        return studyObjectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView topic, content;
        LinearLayout moreLayout;
        Button startBtn, bookmarksBtn;
        RelativeLayout mainView;

        ViewHolder(View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic_title);
            moreLayout = itemView.findViewById(R.id.moreLayout);
            startBtn = itemView.findViewById(R.id.startBtn);
            bookmarksBtn = itemView.findViewById(R.id.bookmarkBtn);
            mainView=itemView.findViewById(R.id.mainView);
            mainView.setOnClickListener(this);
            startBtn.setOnClickListener(this);
            bookmarksBtn.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.mainView:
                    if(moreLayout.getVisibility()==View.VISIBLE){
                        moreLayout.setVisibility(View.GONE);
                    }else{
                        moreLayout.setVisibility(View.VISIBLE);
                    }
                    return;
                case R.id.startBtn:
                    Intent intent=new Intent(context.getApplicationContext(), FlashCardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(topic.getText().toString().equalsIgnoreCase("Anatomy")) {
                        context.getApplicationContext().startActivity(intent);
                    }else{
                        Toast.makeText(context, "Content not available", Toast.LENGTH_SHORT).show();
                    }
                    return;


                case R.id.bookmarkBtn:
                    Intent intent2=new Intent(context.getApplicationContext(), StudyModeActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(topic.getText().toString().equalsIgnoreCase("Anatomy")) {
                    context.getApplicationContext().startActivity(intent2);
                    }else{
                        Toast.makeText(context, "Content not available", Toast.LENGTH_SHORT).show();
                    }
                    return;
            }
        }
    }
}
