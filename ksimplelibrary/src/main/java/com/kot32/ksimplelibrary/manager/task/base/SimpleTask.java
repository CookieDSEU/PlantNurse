package com.kot32.ksimplelibrary.manager.task.base;

import android.os.AsyncTask;

/**
 * Created by kot32 on 15/11/3.
 */
public class SimpleTask extends AsyncTask {

    public static final int TAG_CANCEL_ON_ACTIVITY_FINISHED = 0;

    public static final int TAG_NOT_CANCEL = 1;

    private String taskTag;

    private int cancelFlag;

    public SimpleTask(String taskTag) {
        this(taskTag, TAG_CANCEL_ON_ACTIVITY_FINISHED);
    }

    public SimpleTask(String taskTag,int cancelFlag) {
        this.taskTag = taskTag;
        this.cancelFlag=cancelFlag;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SimpleTaskManager.startNewTask(this);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        SimpleTaskManager.removeTask(this);
    }

    public String getTaskTag() {
        return taskTag;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        SimpleTaskManager.removeTask(this);
    }

    public int getCancelFlag() {
        return cancelFlag;
    }
}
