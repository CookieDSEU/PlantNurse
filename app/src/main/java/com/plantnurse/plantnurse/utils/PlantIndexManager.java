package com.plantnurse.plantnurse.utils;

import com.plantnurse.plantnurse.Network.GetIndexResponse;

/**
 * Created by Cookie_D on 2016/9/1.
 */
public class PlantIndexManager {
    public static GetIndexResponse mPlantIndex;
    public static GetIndexResponse getPlantIndex(){
        return mPlantIndex;
    }
    private PlantIndexManager(){

    }
    public static void setPlantIndex(GetIndexResponse a){
        mPlantIndex=a;
    }

}
