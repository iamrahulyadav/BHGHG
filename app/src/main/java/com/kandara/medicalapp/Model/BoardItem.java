package com.kandara.medicalapp.Model;

/**
 * Created by abina on 4/22/2018.
 */

public class BoardItem {


    String boardName;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
