package com.plantnurse.plantnurse.utils;

import com.plantnurse.plantnurse.Network.GetIndexResponse;
import com.plantnurse.plantnurse.Network.GetMyPlantResponse;
import com.plantnurse.plantnurse.Network.GetMyStarResponse;
import com.plantnurse.plantnurse.Network.WeatherResponse;

/**
 * Created by Cookie_D on 2016/8/23.
 */
public class DataManager {
    private DataManager() {
    }

    public static WeatherResponse mWeatherResponse;
    public static GetIndexResponse mPlantIndex;
    public static GetMyPlantResponse myPlantResponse;
    public static GetMyStarResponse myStarResponse;
    public static boolean isAvatarChanged_drawer = false;
    public static boolean isIsAvatarChanged_myfragment = false;
    public static boolean isMyPlantChanged = false;
    public static boolean isFirstEnterParkFragment = true;
    public static boolean isCityChanged = false;
    public static boolean isMyPlantPicChanged = false;

    public static GetMyStarResponse getMyStar() {
        return myStarResponse;
    }

    public static void setMyStar(GetMyStarResponse a) {
        myStarResponse = a;
    }

    public static GetMyPlantResponse getMyPlant() {
        return myPlantResponse;
    }

    public static void setMyPlant(GetMyPlantResponse myPlantResponse) {
        DataManager.myPlantResponse = myPlantResponse;
    }

    public static WeatherResponse getWeatherInfo() {
        return mWeatherResponse;
    }

    public static void setWeatherInfo(WeatherResponse a) {
        mWeatherResponse = a;
    }

    public static GetIndexResponse getPlantIndex() {
        return mPlantIndex;
    }

    public static void setPlantIndex(GetIndexResponse a) {
        mPlantIndex = a;
    }


}
