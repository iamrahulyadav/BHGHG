package com.kandara.medicalapp.Model;

import com.kandara.medicalapp.View.FilterView;

import java.util.ArrayList;

/**
 * Created by abina on 4/22/2018.
 */

public class Filter {

    ArrayList<ChapterItem> chapterItemArrayList;
    ArrayList<BoardItem> boardItemArrayList;

    public Filter() {
        chapterItemArrayList=new ArrayList<>();
        boardItemArrayList=new ArrayList<>();
    }

    public void addChapter(ChapterItem chapterItem){
        chapterItemArrayList.add(chapterItem);
    }

    public void addUniversity(BoardItem boardItem){
        boardItemArrayList.add(boardItem);
    }

    public void removeChapter(ChapterItem chapterItem){
        chapterItemArrayList.remove(chapterItem);
    }

    public void removeUniversity(BoardItem boardItem){
        boardItemArrayList.remove(boardItem);
    }

    public ArrayList<ChapterItem> getChapterItemArrayList() {
        return chapterItemArrayList;
    }

    public ArrayList<BoardItem> getSelectedUnivs() {
        return boardItemArrayList;
    }
}
