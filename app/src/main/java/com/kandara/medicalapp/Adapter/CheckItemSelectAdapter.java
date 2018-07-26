package com.kandara.medicalapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Model.BoardItem;
import com.kandara.medicalapp.Model.ChapterItem;
import com.kandara.medicalapp.Model.MCQ;
import com.kandara.medicalapp.Model.YearItem;
import com.kandara.medicalapp.R;

import java.time.Year;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by abina on 2/11/2018.
 */


public class CheckItemSelectAdapter extends BaseAdapter {


    Activity activity;
    ArrayList<?> chapterItemArrayList;

    public CheckItemSelectAdapter(Activity activity, ArrayList<?> chapterItemArrayList) {
        this.activity = activity;
        this.chapterItemArrayList = chapterItemArrayList;
    }

    public interface OnItemSelected {
        void OnSelected(Object object);
    }


    public interface OnItemUnSelected {
        void OnUnSelected(Object object);
    }

    OnItemSelected onItemSelected;
    OnItemUnSelected onItemUnSelected;

    public void setOnItemSelected(OnItemSelected onItemSelected) {
        this.onItemSelected = onItemSelected;
    }


    public void setOnItemUnSelected(OnItemUnSelected onItemUnSelected) {
        this.onItemUnSelected = onItemUnSelected;
    }

    @Override
    public int getCount() {
        return chapterItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return chapterItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View cv, ViewGroup parent) {
        //supply the layout for your card
        View view = activity.getLayoutInflater().inflate(R.layout.item_list_chapter, parent, false);

        if(getItem(position) instanceof ChapterItem) {
            final ChapterItem item = (ChapterItem) getItem(position);
            final AppCompatCheckBox checkBox;
            checkBox = view.findViewById(R.id.checkbox);
            if(position>0) {
                checkBox.setText((position) + ". " + item.getChaptername());
            }else{

                checkBox.setText(item.getChaptername());
            }
            if (item.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        item.setChecked(true);
                        onItemSelected.OnSelected(item);
                    } else {
                        item.setChecked(false);
                        onItemUnSelected.OnUnSelected(item);
                    }
                }
            });
        }else if(getItem(position) instanceof YearItem){
            final YearItem item = (YearItem) getItem(position);
            final AppCompatCheckBox checkBox;
            checkBox = view.findViewById(R.id.checkbox);
            checkBox.setText(item.getYear()+"");
            if (item.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        item.setChecked(true);
                        onItemSelected.OnSelected(item);
                    } else {
                        item.setChecked(false);
                        onItemUnSelected.OnUnSelected(item);
                    }
                }
            });
        }else if(getItem(position) instanceof BoardItem){
            final BoardItem item = (BoardItem) getItem(position);
            final AppCompatCheckBox checkBox;
            checkBox = view.findViewById(R.id.checkbox);
            checkBox.setText(item.getBoardName()+"");
            if (item.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        item.setChecked(true);
                        onItemSelected.OnSelected(item);
                    } else {
                        item.setChecked(false);
                        onItemUnSelected.OnUnSelected(item);
                    }
                }
            });
        }

        return view;
    }


}
