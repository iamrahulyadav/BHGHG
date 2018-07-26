package com.kandara.medicalapp.View;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kandara.medicalapp.Adapter.CheckItemSelectAdapter;
import com.kandara.medicalapp.Model.BoardItem;
import com.kandara.medicalapp.Model.ChapterItem;
import com.kandara.medicalapp.Model.Filter;
import com.kandara.medicalapp.Model.Topic;
import com.kandara.medicalapp.Model.YearItem;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.AccountManager;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.fragment.MCQFragment;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abina on 5/5/2018.
 */

public class FilterView extends RelativeLayout {

    Filter filter;

    public Filter getFilter() {
        return filter;
    }

    public enum University {KU, TU, ALL}


    ListView boardList;
    AppCompatImageView universityArrow;
    RelativeLayout universitySelector;


    AppCompatImageView chapterArrow;
    RelativeLayout chapterSelector;
    ListView chapterList;


    OnAnyItemSelected onAnyItemSelected;

    public void setOnAnyItemSelected(OnAnyItemSelected onAnyItemSelected) {
        this.onAnyItemSelected = onAnyItemSelected;
    }

    public interface OnAnyItemSelected {
        void OnSelected();
    }


    public FilterView(Context context) {
        super(context);
        init();
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.filter_view, this);
        filter = new Filter();
        initUniversitySelector();
        initChapterSelector();
    }



    private void initChapterSelector() {
        chapterArrow = findViewById(R.id.chapterArrow);
        chapterSelector = findViewById(R.id.chapterSelector);
        chapterList = findViewById(R.id.chapterList);

        chapterSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chapterList.getVisibility() == View.GONE) {
                    chapterList.setVisibility(View.VISIBLE);
                    boardList.setVisibility(View.GONE);
                    chapterArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    chapterList.setVisibility(View.GONE);
                    chapterArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });



        ArrayList<ChapterItem> adapterItemArrayList=new ArrayList<>();
        ArrayList<String> topics=new ArrayList<>();
        topics.add("All");
        topics.addAll(JsondataUtil.getStudyTopics(getContext()));
        for(String topic:topics){
            ChapterItem chapterItem=new ChapterItem();
            chapterItem.setChaptername(topic);
            chapterItem.setChecked(false);
            adapterItemArrayList.add(chapterItem);
        }

        CheckItemSelectAdapter chapterSelectDataAdapter = new CheckItemSelectAdapter((Activity) getContext(), adapterItemArrayList);
        chapterSelectDataAdapter.setOnItemSelected(new CheckItemSelectAdapter.OnItemSelected() {
            @Override
            public void OnSelected(Object object) {
                filter.addChapter((ChapterItem) object);
                onAnyItemSelected.OnSelected();
            }
        });

        chapterSelectDataAdapter.setOnItemUnSelected(new CheckItemSelectAdapter.OnItemUnSelected() {
            @Override
            public void OnUnSelected(Object object) {
                filter.removeChapter((ChapterItem) object);
                onAnyItemSelected.OnSelected();
            }
        });
        chapterList.setAdapter(chapterSelectDataAdapter);
    }

    private void initUniversitySelector() {
        universityArrow = findViewById(R.id.universityArrow);
        universitySelector = findViewById(R.id.universitySelector);
        boardList = findViewById(R.id.boardList);
        universitySelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boardList.getVisibility() == View.GONE) {
                    boardList.setVisibility(View.VISIBLE);
                    chapterList.setVisibility(View.GONE);
                    universityArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    boardList.setVisibility(View.GONE);
                    universityArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });


        ArrayList<BoardItem> adapterItemArrayList=new ArrayList<>();
        String boards[]={"BPKIHS 2015", "BPKIHS 2016", "BPKIHS 2017", "IOM 2007", "IOM 2009", "IOM 2010", "IOM 2011", "IOM 2012", "IOM 2013", "IOM 2014", "IOM 2015", "IOM 2016", "IOM 2017", "KU 2009","KU 2010", "KU 2011", "KU 2012", "KU 2013", "KU 2014", "KU 2015", "KU 2016", "KU 2017", "NAMS 2009", "NAMS 2010", "NAMS 2011", "NAMS 2012", "NAMS 2013", "NAMS 2014", "NAMS 2015", "NAMS 2016", "NAMS 2017", "PSC 2005", "PSC 2008", "PSC 2011", "PSC 2012", "PSC 2013", "PSC 2014", "PSC 2015", "PSC 2016", "PSC 2017"};
        for(String board:boards){
            BoardItem boardItem=new BoardItem();
            boardItem.setChecked(false);
            boardItem.setBoardName(board);
            adapterItemArrayList.add(boardItem);
        }

        CheckItemSelectAdapter chapterSelectDataAdapter = new CheckItemSelectAdapter((Activity) getContext(), adapterItemArrayList);
        chapterSelectDataAdapter.setOnItemSelected(new CheckItemSelectAdapter.OnItemSelected() {
            @Override
            public void OnSelected(Object object) {
                filter.addUniversity((BoardItem) object);
                onAnyItemSelected.OnSelected();
            }
        });
        chapterSelectDataAdapter.setOnItemUnSelected(new CheckItemSelectAdapter.OnItemUnSelected() {
            @Override
            public void OnUnSelected(Object object) {
                filter.removeUniversity((BoardItem) object);
                onAnyItemSelected.OnSelected();
            }
        });
        boardList.setAdapter(chapterSelectDataAdapter);
    }

}


