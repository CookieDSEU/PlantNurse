package com.plantnurse.plantnurse.utils;

/**
 * Created by Cookie_D on 2016/8/11.
 */
public class DoubleClickExit {
    /**
     * 双击退出检测, 阈值 1000ms
     */
    public static long lastClick = 0L;
    private static final int THRESHOLD = 2000;// 1000ms

    public static boolean check() {
        long now = System.currentTimeMillis();
        boolean b = now - lastClick < THRESHOLD;
        lastClick = now;
        return b;
    }
}
