package com.plantnurse.plantnurse.utils;
import com.plantnurse.plantnurse.Network.WeatherResponse;
/**
 * Created by Cookie_D on 2016/8/23.
 */
public class WeatherManager {
    public static WeatherResponse mWeatherResponse;
    public static WeatherResponse getWeatherInfo(){
        return mWeatherResponse;
    }
    private WeatherManager(){

    }
    public static void setWeatherInfo(WeatherResponse a){
        mWeatherResponse=a;
    }

}
