package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Model.LeaderBoardModel;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ravitmg on 14/01/18.
 */

public class LeaderBoardRecyclerAdapter extends RecyclerView.Adapter<LeaderBoardRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<LeaderBoardModel> leaderBoardModelModelList;

    public LeaderBoardRecyclerAdapter(Context context, List<LeaderBoardModel> leaderBoardModelModelList) {
        this.context = context;
        this.leaderBoardModelModelList = leaderBoardModelModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_model, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LeaderBoardModel lead = leaderBoardModelModelList.get(position);
        holder.tvPosition.setText(lead.getPosition()+"");
        holder.tvPoints.setText(lead.getPoints()+" Pts");
        holder.tvName.setText(lead.getName());
        Picasso.with(context).load(lead.getProfilePhoto()).placeholder(R.drawable.ic_default_profile_photo).into(holder.imgProfilePhoto);
        if (lead.getPosition()==1) {
            holder.imgProfilePhoto.setBorderWidth(2.0f);
            holder.imgProfilePhoto.setBorderColor(context.getResources().getColor(R.color.colorWhite));
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.colorLeaderboardFirstEnd));
            holder.mainBg.setBackgroundResource(R.drawable.bg_leaderboard_first);
            holder.tvPosition.setBackgroundResource(R.drawable.circle_leaderboard_first);
        } else if (lead.getPosition()==2) {
            holder.imgProfilePhoto.setBorderWidth(2.0f);
            holder.imgProfilePhoto.setBorderColor(context.getResources().getColor(R.color.colorWhite));
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.mainBg.setBackgroundResource(R.drawable.bg_leaderboard_second);
            holder.tvPosition.setBackgroundResource(R.drawable.circle_leaderboard_second);
        } else if (lead.getPosition()==3) {
            holder.imgProfilePhoto.setBorderWidth(2.0f);
            holder.imgProfilePhoto.setBorderColor(context.getResources().getColor(R.color.colorWhite));
            holder.tvName.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.tvPoints.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.colorLeaderboardThirdEnd));
            holder.mainBg.setBackgroundResource(R.drawable.bg_leaderboard_third);
            holder.tvPosition.setBackgroundResource(R.drawable.circle_leaderboard_third);
        } else {

            holder.tvPosition.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
            holder.mainBg.setBackgroundResource(R.drawable.bg_leaderboard_normal);
            holder.tvPosition.setBackgroundResource(R.drawable.circle_leaderboard_normal);
        }

    }

    @Override
    public int getItemCount() {
        return leaderBoardModelModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPoints, tvPosition;
        RoundedImageView imgProfilePhoto;
        RelativeLayout mainBg;

        ViewHolder(View itemView) {
            super(itemView);
            imgProfilePhoto = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvPoints = itemView.findViewById(R.id.tv_points);
            tvPosition = itemView.findViewById(R.id.tv_position);
            mainBg = itemView.findViewById(R.id.constraint_leaderboard_model);
        }
    }
}
