package com.plantnurse.plantnurse.Network;

import com.google.gson.annotations.SerializedName;
import com.kot32.ksimplelibrary.model.response.BaseResponse;

import java.util.List;

/**
 * Created by Cookie_D on 2016/9/3.
 */
public class GetMyPlantResponse extends BaseResponse {
    @SerializedName("Index")
    public List<MyPlantResponse> response;

    public static class MyPlantResponse extends BaseResponse {
        @SerializedName("id")
        public int id;
        @SerializedName("nickname")
        public String nickname;
        @SerializedName("name")
        public String name;
        @SerializedName("species")
        public String species;
        @SerializedName("birthday")
        public int birthday;
        @SerializedName("sun")
        public int sun;
        @SerializedName("water")
        public int water;
        @SerializedName("cold")
        public int cold;
        @SerializedName("remark")
        public String remark;
        @SerializedName("pic")
        public String pic;
    }
}
