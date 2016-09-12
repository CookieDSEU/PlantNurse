package com.plantnurse.plantnurse.Network;

import com.plantnurse.plantnurse.model.UserInfo;
import com.kot32.ksimplelibrary.model.response.BaseResponse;

/**
 * Created by Cookie_D on 2016/8/13.
 */
public class LoginResponse extends BaseResponse {
    private int responseCode;
    private UserInfo userInfo;

    public int getresponseCode() {
        return responseCode;
    }

    public String getuserName() {
        return userInfo.getuserName();
    }

    public String gettoken() {
        return userInfo.gettoken();
    }

    public String getcity() {
        return userInfo.getcity();
    }

    public String getcareer() {
        return userInfo.getcareer();
    }

    public String getprovince() {
        return userInfo.getprovince();
    }
}