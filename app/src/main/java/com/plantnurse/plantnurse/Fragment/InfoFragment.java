package com.plantnurse.plantnurse.Fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.R;

/**
 * Created by Eason_Tao on 2016/8/22.
 */
public class InfoFragment extends KSimpleBaseFragmentImpl implements IBaseAction {
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
        return R.layout.activity_login;
    }
}
