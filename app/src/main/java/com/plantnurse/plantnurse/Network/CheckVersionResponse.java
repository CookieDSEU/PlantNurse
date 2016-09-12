package com.plantnurse.plantnurse.Network;

import com.kot32.ksimplelibrary.model.response.BaseResponse;

/**
 * Created by Cookie_D on 2016/8/29.
 */
public class CheckVersionResponse extends BaseResponse {
    private int responseCode;
    private String updatelogtitle;
    private String updatelogmsg;

    public int getResponseCode() {
        return responseCode;
    }

    public String getUpdateLogTitle() {
        return updatelogtitle;
    }

    public String getUpdateLogMsg() {
        return updatelogmsg;
    }
}
