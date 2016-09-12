package com.kot32.ksimplelibrary;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;

import java.util.HashMap;

/**
 * Created by kot32 on 15/11/7.
 */
public abstract class KSimpleApplication extends Application {

    private Object userModel;

    private SimpleTask loginTask;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {

        startInit();
        PreferenceManager.init(this);
        //载入个性化配置
        initLocalPreference((HashMap<String, ?>) PreferenceManager.getAllPreference());
        //初始化用户数据(登录)
        initUserInfo();
    }

    public Object getUserModel() {
        return userModel;
    }

    public void setUserModel(Object userModel) {
        this.userModel = userModel;
    }

    /**
     * 根据载入的本地偏好设置数据进行不同操作
     *
     * @param dataMap
     */
    public abstract void initLocalPreference(HashMap<String, ?> dataMap);

    /**
     * 加载本地用户数据失败时调用
     */
    public abstract void onInitLocalUserModelFailed();

    /**
     * 用户登录逻辑
     */
    public abstract SimpleTask getLoginTask();

    /**
     * 初始化
     */
    public abstract void startInit();

    /**
     * 是否登录
     */
    public boolean isLogined() {
        return userModel != null;
    }

    protected void initUserInfo() {
        //开始登录,先检查本地，再请求输入，再请求网络
        userModel = PreferenceManager.getLocalUserModel();
        if (userModel == null) {
            Log.e("警告", "本地加载用户数据文件失败");
            onInitLocalUserModelFailed();
        } else {
            SimpleTaskManager.startNewTask(getLoginTask());
        }

        Fresco.initialize(this);
    }

    public String getTaskTag() {
        return this.getClass().getSimpleName();
    }

    //注销
    /**
     * Created by Cookie_D on 2016/8/26.
     */
    public void logout() {
        userModel = null;
        //PreferenceManager.setLocalUserModel(new BaseUserModel(){});
        PreferenceManager.deleteLocalUserModel();
    }

}
