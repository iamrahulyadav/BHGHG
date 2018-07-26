package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kandara.medicalapp.Model.Study;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.BGTask;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.Util.OfflineDownload;
import com.kandara.medicalapp.activity.FlashCardActivity;
import com.kandara.medicalapp.activity.MainActivity;
import com.kandara.medicalapp.activity.OfflineFlashCardActivity;
import com.kandara.medicalapp.activity.OfflineStudyModeActivity;
import com.kandara.medicalapp.activity.StudyModeActivity;
import com.orm.SugarRecord;

import java.util.ArrayList;


public class OfflineBookStudyAdapter extends BaseAdapter {

    ArrayList<String> topicArrayList;
    Activity activity;

    public OfflineBookStudyAdapter(ArrayList<String> topicArrayList, Activity activity) {
        this.topicArrayList = topicArrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return topicArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return topicArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_offline_study_topics, viewGroup, false);
        TextView topic;
        final LinearLayout moreLayout;
        Button startBtn, bookmarksBtn, deleteBtn;
        RelativeLayout mainView;
        LinearLayout subchapterLayout;
        final ArrayList<String> selectedSubTopics = new ArrayList<>();
        topic = itemView.findViewById(R.id.topic_title);
        subchapterLayout = itemView.findViewById(R.id.subchapterLayout);
        moreLayout = itemView.findViewById(R.id.moreLayout);
        startBtn = itemView.findViewById(R.id.startBtn);
        bookmarksBtn = itemView.findViewById(R.id.bookmarkBtn);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
        mainView = itemView.findViewById(R.id.mainView);
        topic.setText(toFirstUpper((String) getItem(i)));
        final ArrayList<String> subTopics = JsondataUtil.getSubTopics((String) getItem(i), activity);
        for (final String subTopic : subTopics) {
            View layout2 = LayoutInflater.from(activity).inflate(R.layout.subchapter_view, subchapterLayout, false);
            TextView subTopicTitle = layout2.findViewById(R.id.subTopicTitle);
            CheckBox checkBox = layout2.findViewById(R.id.checkBox);
            subTopicTitle.setText(toFirstUpper(subTopic));
            subchapterLayout.addView(layout2);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        selectedSubTopics.add(subTopic);
                    } else {
                        selectedSubTopics.remove(subTopic);
                    }
                }
            });
        }
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreLayout.getVisibility() == View.VISIBLE) {
                    moreLayout.setVisibility(View.GONE);
                } else {
                    moreLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedSubTopics.size() > 0) {
                    Intent intent = new Intent(activity, OfflineFlashCardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("subTopics", selectedSubTopics);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Please select atleast one chapter", Toast.LENGTH_SHORT).show();

                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Download content...");
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.setProgress(0);
                progressDialog.show();
                new BGTask(new BGTask.DoInBackground() {
                    @Override
                    public void DoJob() {
                        String localQuery = "";
                        for (int i = 0; i < subTopics.size(); i++) {
                            if (i != subTopics.size() - 1) {
                                localQuery += "subcategory = '" + subTopics.get(i).toUpperCase() + "' OR ";
                            } else {
                                localQuery += "subcategory = '" + subTopics.get(i).toUpperCase() + "'";
                            }
                        }
                        SugarRecord.deleteAll(Study.class, localQuery, null);
                    }
                }, new BGTask.DoPostExecute() {
                    @Override
                    public void DoJob() {
                        progressDialog.dismiss();
                        activity.finish();
                        activity.startActivity(new Intent(activity, MainActivity.class));
                    }
                }, (AppCompatActivity) activity, false).execute();
            }
        });


        bookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedSubTopics.size() > 0) {
                    Intent intent2 = new Intent(activity, OfflineStudyModeActivity.class);
                    intent2.putExtra("subTopics", selectedSubTopics);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent2);
                } else {
                    Toast.makeText(activity, "Please select atleast one chapter", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return itemView;
    }

    public String toFirstUpper(String strng) {

        String s1 = strng.substring(0, 1).toUpperCase();
        String topicCapital = s1 + strng.substring(1).toLowerCase();
        return topicCapital;
    }
}
