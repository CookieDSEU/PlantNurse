package com.plantnurse.plantnurse.Network;

import com.plantnurse.plantnurse.model.UserInfo;
import com.kot32.ksimplelibrary.model.response.BaseResponse;


public class LoginResponse extends BaseResponse {
    private int responseCode;
    private UserInfo userInfo;
    public int getresponseCode() {
        return responseCode;
    }
    public String getuserName() {return userInfo.getuserName();}
    public String gettoken() {return userInfo.gettoken();}
}