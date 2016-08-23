package com.plantnurse.plantnurse.Fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.WeatherManager;


public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction{

    private TextView temp;
    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
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

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_park;
    }
}
