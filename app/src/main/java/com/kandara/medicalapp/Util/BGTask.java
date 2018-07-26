package com.kandara.medicalapp.Util;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.kandara.medicalapp.View.catloadinglibrary.CatLoadingView;

/**
 * Created by abina on 5/3/2018.
 */

public class BGTask extends AsyncTask {


    CatLoadingView mView;
    boolean showProgress;
    public interface DoInBackground{
        void DoJob();
    }


    public interface DoPostExecute{
        void DoJob();
    }

    DoInBackground doInBackground;
    DoPostExecute doPostExecute;

    public BGTask(DoInBackground doInBackground, DoPostExecute doPostExecute, AppCompatActivity activity, boolean showPrpgress) {
        this.doInBackground = doInBackground;
        this.doPostExecute = doPostExecute;
        this.showProgress=showPrpgress;
        mView = new CatLoadingView();
        if(showProgress) {
            mView.show(activity.getSupportFragmentManager(), "");
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        doInBackground.DoJob();

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        doPostExecute.DoJob();
        if(showProgress) {
            mView.dismiss();
        }
        super.onPostExecute(o);
    }

}
