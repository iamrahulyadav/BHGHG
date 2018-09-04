package com.kandara.medicalapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kandara.medicalapp.Model.BoardItem;
import com.kandara.medicalapp.Model.ChapterItem;
import com.kandara.medicalapp.Model.Filter;
import com.kandara.medicalapp.Model.YearItem;
import com.kandara.medicalapp.R;
import com.kandara.medicalapp.Util.DataManager;
import com.kandara.medicalapp.Util.JsondataUtil;
import com.kandara.medicalapp.View.FilterView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MCQFragment extends Fragment {


    Button btnStartMCQ;
    FilterView filterView;
    TextView tvTotalQuestion;

    public MCQFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mcq, container, false);
        btnStartMCQ = view.findViewById(R.id.startMCQBtn);
        tvTotalQuestion = view.findViewById(R.id.tvTotalQuestions);
        filterView = view.findViewById(R.id.filterView);
        filterView.setOnAnyItemSelected(new FilterView.OnAnyItemSelected() {
            @Override
            public void OnSelected() {
                DataManager.populateTotal(tvTotalQuestion, getQuery(filterView.getFilter()));
            }
        });
        btnStartMCQ.setEnabled(true);
        btnStartMCQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.startMCQ(getActivity(), getQuery(filterView.getFilter()));
            }
        });
        return view;
    }


    private String getQuery(Filter filter) {
        ArrayList<String> filterCats = new ArrayList<>();
        for (ChapterItem chapterItem : filter.getChapterItemArrayList()) {
            filterCats.add(chapterItem.getChaptername().toUpperCase());
        }
        ArrayList<BoardItem> boardItems = filter.getSelectedUnivs();
        ArrayList<String> boardYearQueryList = new ArrayList<>();
        for (BoardItem boardItem : boardItems) {
            if(!boardItem.getBoardName().equalsIgnoreCase("All")) {
                boardYearQueryList.add(boardItem.getBoardName().replaceFirst(" ", "SSEPPE").replace(" ", "%20"));
            }else{
                boardYearQueryList.add(boardItem.getBoardName().toUpperCase());
            }
        }
        if (filterCats.contains("ALL")) {

            ArrayList<String> topics=JsondataUtil.getStudyTopics(getContext());
            String catFilterQuery = "";
            for (int i = 0; i < topics.size(); i++) {
                if (i != filter.getChapterItemArrayList().size() - 1) {
                    catFilterQuery += "category=" + topics.get(i).toUpperCase() + "&";
                } else {
                    catFilterQuery += "category=" + topics.get(i).toUpperCase() + "";
                }
            }
            return catFilterQuery;
        }


        if (boardYearQueryList.contains("ALL")) {

            ArrayList<String> boards=new ArrayList<>();
            boards.add("AIPGME 2012");
            boards.add("AIPGME 2013/14");
            boards.add("AIPGME 2015/16");
            boards.add("BPKIHS 72");
            boards.add("BPKIHS 2073");
            boards.add("BPKIHS 2074");
            boards.add("IOM 2070");
            boards.add("IOM 2071");
            boards.add("IOM 2072");
            boards.add("IOM 2074 (A) ");
            boards.add("IOM 2074 (B)");
            boards.add("KU 2070");
            boards.add("KU 2071");
            boards.add("KU 2072");
            boards.add("KU 2073");
            boards.add("KU 2074");
            boards.add("NAMS 2070");
            boards.add("NAMS 2071");
            boards.add("NAMS 2072");
            boards.add("NAMS 2073");
            boards.add("NAMS 2074");
            boards.add("PSC 2070 (A)");
            boards.add("PSC 2070 (B)");
            boards.add("PSC 2071 (A)");
            boards.add("PSC 2071(B)");
            boards.add("PSC 2072");
            boards.add("PSC 2073 (A)");
            boards.add("PSC 2073 (B)");
            boards.add("PSC 2074");
            String boardFilterQuery = "";
            for (int i = 0; i < boards.size(); i++) {
                if (i != boards.size() - 1) {
                    boardFilterQuery += "subCategory=" + boards.get(i) .replaceFirst(" ", "SSEPPE")+ "&";
                } else {
                    boardFilterQuery += "subCategory=" + boards.get(i) .replaceFirst(" ", "SSEPPE");
                }
            }
            return boardFilterQuery;
        }
        String catFilterQuery = "";
        String boardFilterQuery = "";
        for (int i = 0; i < filter.getChapterItemArrayList().size(); i++) {
            if (i != filter.getChapterItemArrayList().size() - 1) {
                catFilterQuery += "category=" + filter.getChapterItemArrayList().get(i).getChaptername().toUpperCase() + "&";
            } else {
                catFilterQuery += "category=" + filter.getChapterItemArrayList().get(i).getChaptername().toUpperCase() + "";
            }
        }
        for (int i = 0; i < boardYearQueryList.size(); i++) {
            if (i != boardYearQueryList.size() - 1) {
                boardFilterQuery += "subCategory=" + boardYearQueryList.get(i) + "&";
            } else {
                boardFilterQuery += "subCategory=" + boardYearQueryList.get(i);
            }
        }
        Log.e("gafsgsfg", boardYearQueryList.toString());
        String query = "";
        if (!catFilterQuery.isEmpty()) {
            query += catFilterQuery;
        }
        if (!boardFilterQuery.isEmpty()) {
            if (query.isEmpty()) {

                query += boardFilterQuery;
            } else {
                query += "&" + boardFilterQuery;
            }
        }
        Log.e("QUERYYY", query);
        return query;
    }
}
