package com.kandara.medicalapp.Util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kandara.medicalapp.R;

/**
 * Created by abina on 1/24/2018.
 */

public class NavDrawerAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final int layoutResourceId;
    private String data[] = null;
    private int selected=0;

    public NavDrawerAdapter(Context context, int layoutResourceId, String [] data, int selected)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.selected=selected;
    }

    public void setSelected(int posiSelected){
        this.selected=posiSelected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View v = inflater.inflate(layoutResourceId, parent, false);

        LinearLayout linearLayout=v.findViewById(R.id.navIndicator);
        TextView textView = v.findViewById(R.id.tvTitle);

        String choice = data[position];

        textView.setText(choice);

        if(position==selected){
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            textView.setTextColor(context.getResources().getColor(R.color.navTvSelectedColor));
        }else{
            linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            textView.setTextColor(context.getResources().getColor(R.color.navTvNormalColor));
        }

        return v;
    }
}
