package com.plantnurse.plantnurse.Network;

import com.google.gson.annotations.SerializedName;
import com.kot32.ksimplelibrary.model.response.BaseResponse;

import java.util.List;

/**
 * Created by Cookie_D on 2016/9/1.
 */
public class GetIndexResponse extends BaseResponse {
    @SerializedName("Index")
    public List<IndexResponse> response;

    public static class IndexResponse extends BaseResponse {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
    }
}
