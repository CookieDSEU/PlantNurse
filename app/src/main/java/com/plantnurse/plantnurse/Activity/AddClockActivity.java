package com.plantnurse.plantnurse.Activity;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.NumberPicker;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.R;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import static android.R.attr.button;

/**
 * Created by Heloise on 2016/8/27.
 */

public class AddClockActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    private NumberPicker numberPicker_h;
    private NumberPicker numberPicker_m;
    private NumberPicker numberPicker_ampm;
    private boolean iseveryday_clicked=false;
    private boolean isdayoverday_clicked=false;
    private boolean isthreeday_clicked=false;
    private boolean iswater_clicked=false;
    private boolean issun_clicked=false;
    private boolean isback_clicked=false;
    private boolean isworm_clicked=false;
    private boolean ismedicine_clicked=false;
    private Button button_everyday;
    private Button button_dayoverday;
    private Button button_threeday;
    private Button button_water;
    private Button button_sun;
    private Button button_back;
    private Button button_worm;
    private Button button_medicine;

    @Override
    public int getContentLayoutID() {
        return R.layout.layout_addclock;
    }

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        numberPicker_h= (NumberPicker) findViewById(R.id.numberPicker_hour);
        numberPicker_m= (NumberPicker) findViewById(R.id.numberPicker_minute);
        numberPicker_ampm= (NumberPicker) findViewById(R.id.numberPicker_AMPM);
        String[] ampm={"上午","下午"};
        numberPicker_ampm.setDisplayedValues(ampm);
        numberPicker_ampm.setMinimumHeight(0);
        numberPicker_ampm.setMaxValue(1);
        numberPicker_h.setMinValue(0);
        numberPicker_h.setMaxValue(11);
        numberPicker_m.setMinValue(0);
        numberPicker_m.setMaxValue(59);

        setNumberPickerDividerColor(numberPicker_ampm);
        setNumberPickerDividerColor(numberPicker_h);
        setNumberPickerDividerColor(numberPicker_m);

        button_everyday= (Button) findViewById(R.id.button_everyday);
        button_dayoverday= (Button) findViewById(R.id.button_dayoverday);
        button_threeday= (Button) findViewById(R.id.button_threeday);
        button_water= (Button) findViewById(R.id.button_water);
        button_sun=(Button)findViewById(R.id.button_sun);
        button_back=(Button)findViewById(R.id.button_back);
        button_worm=(Button)findViewById(R.id.button_worm);
        button_medicine=(Button)findViewById(R.id.button_medicine);

        button_everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!iseveryday_clicked){
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    iseveryday_clicked=true;
                }else{
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    iseveryday_clicked=false;
                }
            }
        });

        button_dayoverday.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!isdayoverday_clicked){
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isdayoverday_clicked=true;
                }else{
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isdayoverday_clicked=false;
                }
            }
        });

        button_threeday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isthreeday_clicked){
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isthreeday_clicked=true;
                }else{
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isthreeday_clicked=false;
                }
            }
        });

        button_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!iswater_clicked){
                    button_water.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    iswater_clicked=true;
                }else{
                    button_water.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    iswater_clicked=false;
                }
            }
        });

        button_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!issun_clicked){
                    button_sun.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    issun_clicked=true;
                }else{
                    button_sun.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    issun_clicked=false;
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isback_clicked){
                    button_back.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isback_clicked=true;
                }else{
                    button_back.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isback_clicked=false;
                }
            }
        });

        button_worm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isworm_clicked){
                    button_worm.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isworm_clicked=true;
                }else{
                    button_worm.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isworm_clicked=false;
                }
            }
        });

        button_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ismedicine_clicked){
                    button_medicine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    ismedicine_clicked=true;
                }else{
                    button_medicine.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    ismedicine_clicked=false;
                }
            }
        });
    }

     private void setNumberPickerDividerColor(NumberPicker numberPicker) {
                NumberPicker picker = numberPicker;
                Field[] pickerFields = NumberPicker.class.getDeclaredFields();
               for (Field pf : pickerFields) {
                        if (pf.getName().equals("mSelectionDivider")) {
                            pf.setAccessible(true);
                                try {
                                        //设置分割线的颜色值
                                    pf.set(picker,new ColorDrawable(getResources().getColor(android.R.color.black)));
                                     } catch (IllegalArgumentException e) {
                                         e.printStackTrace();
                                     } catch (Resources.NotFoundException e) {
                                         e.printStackTrace();
                                     } catch (IllegalAccessException e) {
                                         e.printStackTrace();
                                     }
                                break;
                             }
                     }
            }

    @Override
    public void initController() {

    }

    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

}
