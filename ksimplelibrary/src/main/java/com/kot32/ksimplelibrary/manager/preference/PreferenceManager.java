package com.kot32.ksimplelibrary.manager.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.model.domain.BaseUserModel;

import java.util.Map;
import java.util.Set;

/**
 * Created by kot32 on 15/11/8.
 * 封装的 SharePreference ,提供一些快速访问 SharePreference 的方法，以及用一些通用的用户数据
 */
public class PreferenceManager {

    private PreferenceManager() {
    }

    private static SharedPreferences systemUtil;

    private static ACache cache;

    private static final String DEFAULT_PREFERENCE_KEY = "default";
    private static final String KEY_LOCAL_USER_MODEL = "local_user_model";

    private static final int DEFAULT_SAVE_TIME = 604800;


    public static void init(Context context) {
        systemUtil = context.getApplicationContext().getSharedPreferences(DEFAULT_PREFERENCE_KEY, Context.MODE_PRIVATE);
        cache = ACache.get(context.getApplicationContext());
    }

    /**
     * 判断是否为第一次进入APP
     *
     * @return
     */
    public static boolean isFirstInitAPP() {
        boolean b = systemUtil.getBoolean("first_init_app", true);
        if (b) {
            addBooleanPreference("first_init_app", false);
        }
        return b;
    }

    /**
     * 从本地读取保存好的用户名和密码
     *
     * @return
     */
    public static BaseUserModel getLocalUserModel() {
        checkIfNull();
        BaseUserModel model = (BaseUserModel) cache.getAsObject(KEY_LOCAL_USER_MODEL);
        if (model == null) {
            Log.e("警告", "尚未在本地保存用户信息");
        }
        return model;
    }

    public static void setLocalUserModel(BaseUserModel userModel) {
        checkIfNull();
        cache.put(KEY_LOCAL_USER_MODEL, userModel, DEFAULT_SAVE_TIME);
    }

    private static void checkIfNull() {
        if (systemUtil == null || cache == null) {
            Log.e("警告", "未初始化偏好设置管理器");
            return;
        }
    }

    public static void addBooleanPreference(String key, boolean b) {
        checkIfNull();
        SharedPreferences.Editor editor = systemUtil.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    public static void addFloatPreference(String key, float f) {
        checkIfNull();
        SharedPreferences.Editor editor = systemUtil.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    public static void addIntPreference(String key, int i) {
        checkIfNull();
        SharedPreferences.Editor editor = systemUtil.edit();
        editor.putInt(key, i);
        editor.commit();
    }

    public static void addLongPreference(String key, long l) {
        checkIfNull();
        SharedPreferences.Editor editor = systemUtil.edit();
        editor.putLong(key, l);
        editor.commit();
    }

    public static void addStringPreference(String key, String s) {
        checkIfNull();
        SharedPreferences.Editor editor = systemUtil.edit();
        editor.putString(key, s);
        editor.commit();
    }

    public static void addStringSetPreference(String key, Set<String> stringSet) {
        checkIfNull();
        SharedPreferences.Editor editor = systemUtil.edit();
        editor.putStringSet(key, stringSet);
        editor.commit();
    }

    public static Object getPreference(String key, Object defaultValue) {
        checkIfNull();
        Object value = systemUtil.getAll().get(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static Map<String, ?> getAllPreference() {
        checkIfNull();
        return systemUtil.getAll();
    }


}
