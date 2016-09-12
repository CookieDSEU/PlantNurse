package com.plantnurse.plantnurse.Activity;

import android.view.View;
import android.view.ViewGroup;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.KSplashActivity;
import com.plantnurse.plantnurse.R;

/**
 * Created by Cookie_D on 2016/8/25.
 */
public class SplashActivity extends KSplashActivity implements IBaseAction {

    public int getLogoImageResource() {
        return R.drawable.logo_samll;
    }

    @Override
    public Class getNextActivityClass() {
        return MainActivity.class;
    }

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
        return 0;
    }

    @Override
    public void init() {
    }

}
