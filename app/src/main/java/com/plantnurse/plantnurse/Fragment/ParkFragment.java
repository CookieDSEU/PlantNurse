package com.plantnurse.plantnurse.Fragment;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.WeatherManager;

/**
 * Created by Eason_Tao on 2016/8/26.
 */
public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction{
    //view
    private TextView text_Tmp;
    private ImageView image_Weather;
    private TextView text_City;
    private ImageButton left_Button;
    private ImageButton right_Button;
    //data
    private String now_Tmp;
    private String city;
    private int now_Cond;
    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        text_Tmp = (TextView) view.findViewById(R.id.text_temp);
        text_City = (TextView) view.findViewById(R.id.text_city);
        image_Weather = (ImageView) view.findViewById(R.id.image_weather);
        left_Button = (ImageButton) view.findViewById(R.id.button_left);
        right_Button = (ImageButton) view.findViewById(R.id.button_right);
    }

    @Override
    public void initController() {
        now_Tmp = WeatherManager.getWeatherInfo().now.tmp;
        now_Cond = Integer.parseInt(WeatherManager.getWeatherInfo().now.cond.code);
        city = WeatherManager.getWeatherInfo().basic.city;

        left_Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundResource(R.drawable.left3_pressed);
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    v.setBackgroundResource(R.drawable.left_3);
                }
                return false;
            }
        });

        right_Button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    v.setBackgroundResource(R.drawable.right3_pressed);
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    v.setBackgroundResource(R.drawable.right_3);
                }
                return false;
            }
        });

        text_Tmp.setText(now_Tmp+"℃");
        text_City.setText(city);

        switch (now_Cond){
            case 100:
                image_Weather.setImageResource(R.drawable.sunny);
                break;
            case 101:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 102:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 103:
                image_Weather.setImageResource(R.drawable.cloudy_2);
                break;
            case 104:
                image_Weather.setImageResource(R.drawable.cloudy_3);
                break;
            case 300:
                image_Weather.setImageResource(R.drawable.rainy_2);
                break;
            case 302:
                image_Weather.setImageResource(R.drawable.thunder);
                break;
            case 305:
                image_Weather.setImageResource(R.drawable.rainy_3);
                break;
            case 306:
                image_Weather.setImageResource(R.drawable.rainy_3);
                break;
            case 307:
                image_Weather.setImageResource(R.drawable.rainy);
                break;
            case 400:
                image_Weather.setImageResource(R.drawable.snow);
                break;
            case 406:
                image_Weather.setImageResource(R.drawable.snow_2);
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
