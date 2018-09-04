package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kandara.medicalapp.Model.TeamMemberItem;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by abina on 8/2/2018.
 */

public class TeamMemberAdapter extends BaseAdapter {

    ArrayList<TeamMemberItem> teamMemberItemArrayList;
    Activity activity;

    public TeamMemberAdapter(ArrayList<TeamMemberItem> teamMemberItemArrayList, Activity activity) {
        this.teamMemberItemArrayList = teamMemberItemArrayList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return teamMemberItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return teamMemberItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=activity.getLayoutInflater().inflate(R.layout.item_team_member, viewGroup, false);
        RoundedImageView memberPhoto=view.findViewById(R.id.memberPhoto);
        TextView tvName=view.findViewById(R.id.tvName);
        TextView tvPosition=view.findViewById(R.id.tvPosition);
        TextView tvEmail=view.findViewById(R.id.tvEmail);

        final TeamMemberItem teamMemberItem=teamMemberItemArrayList.get(i);
        memberPhoto.setImageResource(teamMemberItem.getPhoto());
        tvName.setText(teamMemberItem.getName());
        tvPosition.setText(teamMemberItem.getPosition());
        tvEmail.setText(teamMemberItem.getEmail());
        return view;
    }
}
