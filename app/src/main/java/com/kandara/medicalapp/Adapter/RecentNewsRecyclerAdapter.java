package com.kandara.medicalapp.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kandara.medicalapp.Model.News;
import com.kandara.medicalapp.R;

import java.util.List;

/**
 * Created by ravitmg on 12/01/18.
 */

public class RecentNewsRecyclerAdapter extends RecyclerView.Adapter<RecentNewsRecyclerAdapter.ViewHolder> {
    Context context;
    List<News> recentNewsList;

    public RecentNewsRecyclerAdapter(Context context, List<News> recentNewsList) {
        this.context = context;
        this.recentNewsList = recentNewsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentnews_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News recentNews = recentNewsList.get(position);
        holder.newstitle.setText(recentNews.getTitle());
    }

    @Override
    public int getItemCount() {
        return recentNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView newstitle;
        ConstraintLayout constraintLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            newstitle = itemView.findViewById(R.id.tv_news_title);
            constraintLayout = itemView.findViewById(R.id.constraint_news_title);
        }
    }
}
