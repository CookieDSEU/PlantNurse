package com.plantnurse.plantnurse.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kot32.ksimplelibrary.util.common.adapter.SimpleBaseAdapter;
import com.plantnurse.plantnurse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eason_Tao on 2016/8/26.
 */
public class WeatherAdapter extends SimpleBaseAdapter<String>{

    private  List<Map<String,Object>> mData;

    public  WeatherAdapter(Context context,List<String> data){
        super(context,data);

        mData = getData();
    }
    @Override
    public int getItemResource() {
        return R.layout.listview_weather;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView weather_img = holder.getView(R.id.list_image);
        TextView weather_date = holder.getView(R.id.list_date);
        TextView weather_tmp = holder.getView(R.id.list_tmp);

        weather_img.setBackgroundResource((Integer) mData.get(position).get("img"));
        weather_date.setText((String) mData.get(position).get("date"));
        weather_tmp.setText((String)mData.get(position).get("min")+"~"+mData.get(position).get("max")+"℃");


        return convertView;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int image_code;
        Map<String, Object> map;
        for (int i = 0; i < 3; i++) {
            map = new HashMap<String, Object>();
            map.put("date", WeatherManager.getWeatherInfo().dailyForecast.get(i).date);
            map.put("tmp_min", WeatherManager.getWeatherInfo().dailyForecast.get(i).tmp.min);
            map.put("tmp_max", WeatherManager.getWeatherInfo().dailyForecast.get(i).tmp.max);
            image_code = Integer.parseInt(WeatherManager.getWeatherInfo().dailyForecast.get(i).cond.codeD);
            switch (image_code) {
                case 100:
                    map.put("img", R.drawable.p1);
                    break;
                case 101:
                    map.put("img", R.drawable.cloudy);
                    break;
                case 102:
                    map.put("img", R.drawable.cloudy);
                    break;
                case 103:
                    map.put("img", R.drawable.cloudy_2);
                    break;
                case 104:
                    map.put("img", R.drawable.cloudy_3);
                    break;
                case 300:
                    map.put("img", R.drawable.rainy_2);
                    break;
                case 302:
                    map.put("img", R.drawable.thunder);
                    break;
                case 305:
                    map.put("img", R.drawable.rainy_3);
                    break;
                case 306:
                    map.put("img", R.drawable.rainy_3);
                    break;
                case 307:
                    map.put("img", R.drawable.rainy);
                    break;
                case 400:
                    map.put("img", R.drawable.snow);
                    break;
                case 406:
                    map.put("img", R.drawable.snow_2);
                    break;
            }
            list.add(map);
        }
        return list;
    }
}
