package com.plantnurse.plantnurse.model;

import com.kot32.ksimplelibrary.model.domain.BaseUserModel;

/**
 * Created by Cookie_D on 2016/8/12.
 */
public class UserInfo implements BaseUserModel {
    private String userName;
    private String province;
    private String city;
    private String career;
    private String token;

    public String getuserName() {
        return userName;
    }

    public String getprovince() {
        return province;
    }

    public String getcity() {
        return city;
    }

    public String getcareer() {
        return career;
    }

    public String gettoken() {
        return token;
    }

    public void setuserName(String n) {
        userName = n;
    }

    public void setProvince(String p) {
        province = p;
    }

    public void setcity(String c) {
        city = c;
    }

    public void setcareer(String n) {
        career = n;
    }

    public void settoken(String n) {
        token = n;
    }
}