package com.kot32.ksimplelibrary.manager.task.base;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by kot32 on 15/10/18.
 */
public class SimpleTaskManager {

    private static HashMap<String, Queue<SimpleTask>> tasks = new HashMap<>();

    public static void startNewTask(SimpleTask task) {
        Queue taskQueue = tasks.get(task.getTaskTag());
        if (taskQueue == null) {
            taskQueue = new LinkedList();
            tasks.put(task.getTaskTag(), taskQueue);
        }
        taskQueue.add(task);
        if (task.getStatus() != AsyncTask.Status.RUNNING) {
            task.execute();
        }
    }

    public static void removeTask(SimpleTask task) {
        task.cancel(false);
        Queue taskQueue = tasks.get(task.getTaskTag());
        if (taskQueue != null)
            taskQueue.remove(task);
    }

    public static void removeTasksWithTag(String tag) {


        Queue<SimpleTask> taskQueue = tasks.get(tag);
        if (taskQueue != null) {
            Iterator iterator = taskQueue.iterator();
            while (iterator.hasNext()) {
                SimpleTask task = (SimpleTask) iterator.next();
                if (task.getCancelFlag() == SimpleTask.TAG_CANCEL_ON_ACTIVITY_FINISHED) {
                    task.cancel(false);
                    iterator.remove();
                }
            }
        }
    }


}
