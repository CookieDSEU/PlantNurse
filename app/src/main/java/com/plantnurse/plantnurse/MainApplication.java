package com.plantnurse.plantnurse;

import android.content.Context;
import android.widget.Toast;

import com.kot32.ksimplelibrary.KSimpleApplication;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.LoginTask;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.model.response.BaseResponse;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.model.UserInfo;
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
        UserInfo usermodel=(UserInfo)getUserModel();
        String username = usermodel.getuserName();
        String token = usermodel.gettoken();
        HashMap<String, String> loginParams = new HashMap<>();
        /*从缓存中取出用户名密码并放入*/
            loginParams.put("userName", username);
            loginParams.put("token", token);
            return new LoginTask(getTaskTag(), this,
                    LoginResponse.class, loginParams, Constants.SIGNIN_URL, NetworkTask.GET) {
                @Override
                public boolean isLoginSucceed(BaseResponse baseResponse) {
                    LoginResponse loginResponse = (LoginResponse) baseResponse;
                    if (loginResponse.getresponseCode() == 1) {
                    /*自动登录后主动刷新缓存*/
                        UserInfo ui=new UserInfo();
                        ui.setuserName(loginResponse.getuserName());
                        ui.setProvince(loginResponse.getprovince());
                        ui.setcareer(loginResponse.getcareer());
                        ui.setcity(loginResponse.getcity());
                        ui.settoken(loginResponse.gettoken());
                        PreferenceManager.setLocalUserModel(ui);
                        setUserModel(ui);
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
