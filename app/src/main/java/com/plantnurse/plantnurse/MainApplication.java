package com.plantnurse.plantnurse;

import android.content.Context;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.pushclient.PushManager;
import com.kot32.ksimplelibrary.KSimpleApplication;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.LoginTask;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.model.response.BaseResponse;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;

import java.util.HashMap;

import cn.smssdk.SMSSDK;

/**
 * Created by Cookie_D on 2016/8/13.
 */
public class MainApplication extends KSimpleApplication {

    public static Context mAppContext = null;

    @Override
    public void initLocalPreference(HashMap<String, ?> dataMap) {

    }

    @Override
    public void onInitLocalUserModelFailed() {

    }

    //继承自SimpleTaskApplication，登录方法
    @Override
    public SimpleTask getLoginTask() {
        UserInfo usermodel = (UserInfo) getUserModel();
        String username = usermodel.getuserName();
        String token = usermodel.gettoken();
        HashMap<String, String> loginParams = new HashMap<>();
        //从缓存中取出用户名密码并放入
        loginParams.put("userName", username);
        loginParams.put("token", token);
        return new LoginTask(getTaskTag(), this,
                LoginResponse.class, loginParams, Constants.SIGNIN_URL, NetworkTask.GET) {
            @Override
            public boolean isLoginSucceed(BaseResponse baseResponse) {
                //自动登录成功后
                LoginResponse loginResponse = (LoginResponse) baseResponse;
                if (loginResponse.getresponseCode() == 1) {
                    //自动登录后主动刷新model
                    UserInfo ui = new UserInfo();
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
                Toast.makeText(getApplicationContext(), "自动登录失败", Toast.LENGTH_LONG).show();
                logout();
            }

        };
    }

    @Override
    public void startInit() {
        //获取应用的上下文
        mAppContext = getApplicationContext();
        //推送服务初始化
        PushManager.startWork(this, "57ce7788");
        //语音识别服务初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57ce7788");
        //短信服务初始化
        SMSSDK.initSDK(this, "170705d821cf4", "1b1438cc5ec2303e764bfe9dbe93dab0");
    }

    public static Context getmAppContext() {
        return mAppContext;
    }

}
