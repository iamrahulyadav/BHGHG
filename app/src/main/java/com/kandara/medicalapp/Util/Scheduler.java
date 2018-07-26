package com.kandara.medicalapp.Util;

import android.os.Handler;

/**
 * Created by abina on 5/5/2018.
 */

public class Scheduler {
    private final static int INTERVAL = 1000 * 60 * 1; //2 minutes
    Handler mHandler = new Handler();
    RepeatingTask repeatingTask;

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            repeatingTask.Do();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    public Scheduler(RepeatingTask repeatingTask) {
        this.repeatingTask = repeatingTask;
    }

    public interface RepeatingTask{
        void Do();
    }

    public void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    public void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }
}
