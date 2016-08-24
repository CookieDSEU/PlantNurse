package com.plantnurse.plantnurse.Fragment;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.WeatherManager;


public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction{
    //view
    private TextView text_Tmp;
    private ImageView image_Weather;
    //data
    private String now_Tmp;
    private int now_Cond;
    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        text_Tmp = (TextView) view.findViewById(R.id.text_temp);
        image_Weather = (ImageView) view.findViewById(R.id.image_weather);
    }

    @Override
    public void initController() {
        now_Tmp = WeatherManager.getWeatherInfo().now.tmp;
        now_Cond = Integer.parseInt(WeatherManager.getWeatherInfo().now.cond.code);

        text_Tmp.setText(now_Tmp+"℃");

        switch (now_Cond){
            case 100:
                image_Weather.setImageResource(R.drawable.sunny);
                break;
            case 101:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 305:
                image_Weather.setImageResource(R.drawable.rainy);
                break;
            }


    }

    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_park;
    }
}
