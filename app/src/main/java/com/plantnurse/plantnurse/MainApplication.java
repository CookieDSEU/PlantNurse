package com.plantnurse.plantnurse;

import android.content.Context;
import android.widget.Toast;

import com.kot32.ksimplelibrary.KSimpleApplication;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.task.LoginTask;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.model.response.BaseResponse;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.utils.Constants;

import java.util.HashMap;


public class MainApplication extends KSimpleApplication  {

    private ACache mCache;
    public static Context mAppContext = null;
    @Override
    public void initLocalPreference(HashMap<String, ?> dataMap) {

    }

    @Override
    public void onInitLocalUserModelFailed() {

    }

    @Override
    public SimpleTask getLoginTask() {
        mCache = ACache.get(this);/*检查缓存中是否有用户名密码*/
        String username = mCache.getAsString("userName");
        String token = mCache.getAsString("token");
        HashMap<String, String> loginParams = new HashMap<>();
        /*从缓存中取出用户名密码并放入*/
        if (username != null) {
            loginParams.put("userName", username);
            loginParams.put("token", token);
        }
        else{
            loginParams.put("userName", "blank");
            loginParams.put("token", "blank");
        }
            return new LoginTask(getTaskTag(), this,
                    LoginResponse.class, loginParams, Constants.SIGNIN_URL, NetworkTask.GET) {
                @Override
                public boolean isLoginSucceed(BaseResponse baseResponse) {
                    LoginResponse loginResponse = (LoginResponse) baseResponse;
                    if (loginResponse.getresponseCode() == 1) {
                    /*自动登录后主动刷新缓存*/
                        mCache.put("userName", loginResponse.getuserName(), 7 * ACache.TIME_DAY);
                        mCache.put("token", loginResponse.gettoken(), 7 * ACache.TIME_DAY);
                        Toast.makeText(getApplicationContext(), "自动登录成功", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onConnectFailed(NetworkExecutor.NetworkResult result) {

                }

            };


    }

    @Override
    public void startInit() {

        mAppContext = getApplicationContext();
    }
    public static Context getmAppContext() {
        return mAppContext;
    }

}
