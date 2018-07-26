package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Dialogs.CommentDialog;
import com.kandara.medicalapp.Model.Discussion;
import com.kandara.medicalapp.Model.DiscussionAnswer;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.DataManager;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.kandara.medicalapp.activity.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abina on 2/5/2018.
 */

public class DiscussionAdapter extends BaseAdapter {
    ArrayList<Discussion> discussionArrayList;
    Activity activity;


    public DiscussionAdapter(ArrayList<Discussion> discussionArrayList, Activity activity) {
        this.discussionArrayList = discussionArrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return discussionArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return discussionArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
            view = activity.getLayoutInflater().inflate(R.layout.item_discussion, viewGroup, false);

            RoundedImageView profilePhoto;
            TextView tvUserName, tvQuestions;
            TextView optionA, optionB, optionC, optionD;

            final AppCompatImageView likeBtn, commentBtn;


            LinearLayout optionsLayout;

            profilePhoto = view.findViewById(R.id.profile_image);
            optionsLayout = view.findViewById(R.id.options);
            tvUserName = view.findViewById(R.id.tv_user_name);
            tvQuestions = view.findViewById(R.id.tvComment);
            optionA = view.findViewById(R.id.tvOptionA);
            optionB = view.findViewById(R.id.tvOptionB);
            optionC = view.findViewById(R.id.tvOptionC);
            optionD = view.findViewById(R.id.tvOptionD);


            final RelativeLayout optionRLA = view.findViewById(R.id.opnA);
            final RelativeLayout optionRLB = view.findViewById(R.id.opnB);
            final RelativeLayout optionRLC = view.findViewById(R.id.opnC);
            final RelativeLayout optionRLD = view.findViewById(R.id.opnD);

            final TextView tvOptionPercentageA = view.findViewById(R.id.tvOptionPercentageA);
            final TextView tvOptionPercentageB = view.findViewById(R.id.tvOptionPercentageB);
            final TextView tvOptionPercentageC = view.findViewById(R.id.tvOptionPercentageC);
            final TextView tvOptionPercentageD = view.findViewById(R.id.tvOptionPercentageD);

            tvOptionPercentageA.setVisibility(View.GONE);
            tvOptionPercentageB.setVisibility(View.GONE);
            tvOptionPercentageC.setVisibility(View.GONE);
            tvOptionPercentageD.setVisibility(View.GONE);

            likeBtn = view.findViewById(R.id.btnLike);
            commentBtn = view.findViewById(R.id.btnComment);

            final Discussion discussion = (Discussion) getItem(i);


            if (!discussion.getFavoritedBy().isEmpty()) {
                likeBtn.setImageResource(R.drawable.ic_love_filled);
            } else {
                likeBtn.setImageResource(R.drawable.ic_love_empty);
            }

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeBtn.setImageResource(R.drawable.ic_love_filled);
                }
            });


            tvQuestions.setText(discussion.getQuestion());
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentDialog commentDialog = new CommentDialog();
                    commentDialog.setDiscussionId(discussion.getId());
                    Log.e("Discussion ID", discussion.getId());
                    MainActivity mainActivity = (MainActivity) activity;
                    commentDialog.show(mainActivity.getSupportFragmentManager(), "");
                }
            });


            DataManager.PopulateUser(activity, tvUserName, profilePhoto, discussion.getUid());


            if (!discussion.isType()) {
                optionsLayout.setVisibility(View.GONE);
            } else {
                optionsLayout.setVisibility(View.VISIBLE);
                ArrayList<DiscussionAnswer> discussionAnswers = discussion.getAnswers();
                Log.e("Answersize", discussionAnswers.size() + "");

                if (discussionAnswers.size() == 1) {
                    optionA.setText(discussionAnswers.get(0).getOption());
                    optionRLB.setVisibility(View.GONE);
                    optionRLC.setVisibility(View.GONE);
                    optionRLD.setVisibility(View.GONE);
                } else if (discussionAnswers.size() == 2) {
                    optionA.setText(discussionAnswers.get(0).getOption());
                    optionB.setText(discussionAnswers.get(1).getOption());
                    optionRLC.setVisibility(View.GONE);
                    optionRLD.setVisibility(View.GONE);

                } else if (discussionAnswers.size() == 3) {
                    optionA.setText(discussionAnswers.get(0).getOption());
                    optionB.setText(discussionAnswers.get(1).getOption());
                    optionC.setText(discussionAnswers.get(2).getOption());
                    optionRLD.setVisibility(View.GONE);

                } else if (discussionAnswers.size() == 4) {
                    optionA.setText(discussionAnswers.get(0).getOption());
                    optionB.setText(discussionAnswers.get(1).getOption());
                    optionC.setText(discussionAnswers.get(2).getOption());
                    optionD.setText(discussionAnswers.get(3).getOption());
                } else {
                    optionRLA.setVisibility(View.GONE);
                    optionRLB.setVisibility(View.GONE);
                    optionRLC.setVisibility(View.GONE);
                    optionRLD.setVisibility(View.GONE);
                }

                int optionACount = 0, optionBCount = 0, optionCCount = 0, optionDCount = 0;

                if (discussionAnswers.size() == 1) {
                    optionACount = Integer.parseInt(discussionAnswers.get(0).getVotes());
                } else if (discussionAnswers.size() == 2) {
                    optionACount = Integer.parseInt(discussionAnswers.get(0).getVotes());
                    optionBCount = Integer.parseInt(discussionAnswers.get(1).getVotes());

                } else if (discussionAnswers.size() == 3) {
                    optionACount = Integer.parseInt(discussionAnswers.get(0).getVotes());
                    optionBCount = Integer.parseInt(discussionAnswers.get(1).getVotes());
                    optionCCount = Integer.parseInt(discussionAnswers.get(2).getVotes());

                } else if (discussionAnswers.size() == 3) {
                    optionACount = Integer.parseInt(discussionAnswers.get(0).getVotes());
                    optionBCount = Integer.parseInt(discussionAnswers.get(1).getVotes());
                    optionCCount = Integer.parseInt(discussionAnswers.get(2).getVotes());
                    optionDCount = Integer.parseInt(discussionAnswers.get(3).getVotes());
                }

                final int finalOptionACount = optionACount;
                final int finalOptionBCount = optionBCount;
                final int finalOptionCCount = optionCCount;
                final int finalOptionDCount = optionDCount;

                final int totalOptionCount = optionACount + optionBCount + optionCCount + optionDCount;

                optionRLA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        int opACount = finalOptionACount + 1;
                        int totalCount = totalOptionCount + 1;
                        optionRLA.setBackgroundResource(R.drawable.bg_options_orange);
                        optionRLB.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLC.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLD.setBackgroundResource(R.drawable.bg_options_normal);

                        DisplayPercentage(tvOptionPercentageA, tvOptionPercentageB, tvOptionPercentageC, tvOptionPercentageD, opACount, totalCount, finalOptionBCount, finalOptionCCount, finalOptionDCount);
                        //populateOptionWithVote(optionLeftA, optionLeftB, optionLeftC, optionLeftD, optionRightA, optionRightB, optionRightC, optionRightD, finalOptionACount, finalOptionBCount, finalOptionCCount, finalOptionDCount);

                    }
                });


                optionRLB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionRLB.setBackgroundResource(R.drawable.bg_options_orange);
                        optionRLA.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLC.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLD.setBackgroundResource(R.drawable.bg_options_normal);


                        int opBCount = finalOptionBCount + 1;
                        int totalCount = totalOptionCount + 1;

                        DisplayPercentage(tvOptionPercentageA, tvOptionPercentageB, tvOptionPercentageC, tvOptionPercentageD, finalOptionACount, totalCount, opBCount, finalOptionCCount, finalOptionDCount);

                        //populateOptionWithVote(optionLeftA, optionLeftB, optionLeftC, optionLeftD, optionRightA, optionRightB, optionRightC, optionRightD, finalOptionACount, finalOptionBCount, finalOptionCCount, finalOptionDCount);

                    }
                });


                optionRLC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionRLC.setBackgroundResource(R.drawable.bg_options_orange);
                        optionRLB.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLA.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLD.setBackgroundResource(R.drawable.bg_options_normal);


                        int opCCount = finalOptionCCount + 1;
                        int totalCount = totalOptionCount + 1;

                        DisplayPercentage(tvOptionPercentageA, tvOptionPercentageB, tvOptionPercentageC, tvOptionPercentageD, finalOptionACount, totalCount, finalOptionBCount, opCCount, finalOptionDCount);

                        //populateOptionWithVote(optionLeftA, optionLeftB, optionLeftC, optionLeftD, optionRightA, optionRightB, optionRightC, optionRightD, finalOptionACount, finalOptionBCount, finalOptionCCount, finalOptionDCount);

                    }
                });


                optionRLD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionRLD.setBackgroundResource(R.drawable.bg_options_orange);
                        optionRLB.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLC.setBackgroundResource(R.drawable.bg_options_normal);
                        optionRLA.setBackgroundResource(R.drawable.bg_options_normal);

                        int opDCount = finalOptionDCount + 1;
                        int totalCount = totalOptionCount + 1;

                        DisplayPercentage(tvOptionPercentageA, tvOptionPercentageB, tvOptionPercentageC, tvOptionPercentageD, finalOptionACount, totalCount, finalOptionBCount, finalOptionCCount, opDCount);

                        //populateOptionWithVote(optionLeftA, optionLeftB, optionLeftC, optionLeftD, optionRightA, optionRightB, optionRightC, optionRightD, finalOptionACount, finalOptionBCount, finalOptionCCount, finalOptionDCount);

                    }
                });
            }

        return view;
    }

    private void DisplayPercentage(TextView tvOptionPercentageA, TextView tvOptionPercentageB, TextView tvOptionPercentageC, TextView tvOptionPercentageD, int finalOptionACount, int totalOptionCount, int finalOptionBCount, int finalOptionCCount, int finalOptionDCount) {
        tvOptionPercentageA.setVisibility(View.VISIBLE);
        tvOptionPercentageB.setVisibility(View.VISIBLE);
        tvOptionPercentageC.setVisibility(View.VISIBLE);
        tvOptionPercentageD.setVisibility(View.VISIBLE);

        tvOptionPercentageA.setText((finalOptionACount /totalOptionCount)*100+" %");
        tvOptionPercentageB.setText((finalOptionBCount /totalOptionCount)*100+" %");
        tvOptionPercentageC.setText((finalOptionCCount /totalOptionCount)*100+" %");
        tvOptionPercentageD.setText((finalOptionDCount /totalOptionCount)*100+" %");
    }


}
