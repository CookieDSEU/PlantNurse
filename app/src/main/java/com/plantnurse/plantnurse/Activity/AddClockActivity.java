package com.plantnurse.plantnurse.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.R;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Heloise on 2016/8/27.
 */

public class AddClockActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    private NumberPicker numberPicker_h;
    private NumberPicker numberPicker_m;
    private NumberPicker numberPicker_ampm;

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
        numberPicker_h.setMaxValue(23);
        numberPicker_m.setMinValue(0);
        numberPicker_m.setMaxValue(59);
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
