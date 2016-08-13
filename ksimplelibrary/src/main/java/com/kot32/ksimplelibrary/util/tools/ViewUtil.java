package com.kot32.ksimplelibrary.util.tools;

/**
 * Created by kot32 on 15/12/26.
 */
public class ViewUtil {
    private ViewUtil() {
    }

    public static int generateViewId() {
        String dyIDString = System.nanoTime() + "";
        int dyID = Integer.parseInt((dyIDString).substring(dyIDString.length() - 4, dyIDString.length()));
        return dyID;
    }
}
