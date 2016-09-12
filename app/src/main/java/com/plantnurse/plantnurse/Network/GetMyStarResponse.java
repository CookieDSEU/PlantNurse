package com.plantnurse.plantnurse.Network;

import com.google.gson.annotations.SerializedName;
import com.kot32.ksimplelibrary.model.response.BaseResponse;

import java.util.List;

/**
 * Created by Cookie_D on 2016/9/5.
 */

public class GetMyStarResponse extends BaseResponse {
    @SerializedName("Index")
    public List<GetMyStarResponse.MyStarResponse> response;

    public static class MyStarResponse extends BaseResponse {
        @SerializedName("name")
        public String name;
        @SerializedName("date")
        public String date;
        @SerializedName("plant_id")
        public int plant_id;
    }

}
