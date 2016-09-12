package com.plantnurse.plantnurse.utils;

import android.widget.Toast;

import com.plantnurse.plantnurse.MainApplication;

/**
 * Created by Cookie_D on 2016/8/11.
 */
public class ToastUtil {
    /**
     * 工具类，直接显示Toast
     */
    public static void showShort(String msg) {
        Toast.makeText(MainApplication.getmAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(MainApplication.getmAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
