package com.plantnurse.plantnurse.Network;


import com.google.gson.annotations.SerializedName;
import com.kot32.ksimplelibrary.model.response.BaseResponse;

import java.util.List;

/**
 * Created by Cookie_D on 2016/8/23.
 */
public class WeatherAPI extends BaseResponse {
    @SerializedName("HeWeather data service 3.0")
    public List<WeatherResponse> response;
}
