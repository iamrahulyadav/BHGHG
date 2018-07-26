package com.kandara.medicalapp.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kandara.medicalapp.Model.Comment;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.DataManager;
import com.kandara.medicalapp.View.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abina on 2/28/2018.
 */

public class CommentAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Comment> commentArrayList;

    public CommentAdapter(Activity activity, ArrayList<Comment> commentArrayList) {
        this.activity = activity;
        this.commentArrayList = commentArrayList;
    }

    @Override
    public int getCount() {
        return commentArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=activity.getLayoutInflater().inflate(R.layout.item_comment, viewGroup, false);
        RoundedImageView profileImg=view.findViewById(R.id.profile_image);
        TextView tvProfileName=view.findViewById(R.id.tv_user_name);
        TextView tvComment=view.findViewById(R.id.tvComment);

        Comment comment= (Comment) getItem(i);


        DataManager.PopulateUser(activity, tvProfileName, profileImg, comment.getUid());
        tvComment.setText(comment.getComment());
        return view;
    }
}
