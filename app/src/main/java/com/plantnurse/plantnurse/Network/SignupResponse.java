package com.plantnurse.plantnurse.Network;

import com.kot32.ksimplelibrary.model.response.BaseResponse;
import com.plantnurse.plantnurse.model.UserInfo;

/**
 * Created by Cookie_D on 2016/8/5.
 */
public class SignupResponse extends BaseResponse {
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
}
