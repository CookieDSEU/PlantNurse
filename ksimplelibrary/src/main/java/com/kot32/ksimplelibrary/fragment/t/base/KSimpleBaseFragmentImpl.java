package com.kot32.ksimplelibrary.fragment.t.base;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.e.KSimpleBaseFragment;

/**
 * Created by kot32 on 15/11/4.
 */
public abstract class KSimpleBaseFragmentImpl extends KSimpleBaseFragment {


    @Override
    public IBaseAction getIBaseAction() {
        if (!(this instanceof IBaseAction)) {
            Log.e("警告", "尚未实现IBaseAction接口");
            return new IBaseAction() {
                @Override
                public int initLocalData() {
                    return IBaseAction.LOAD_NETWORK_DATA_AND_SHOW;
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
                public void onLoadedNetworkData(View view) {

                }

                @Override
                public int getContentLayoutID() {
                    return android.R.layout.activity_list_item;
                }
            };
        }

        return (IBaseAction) this;
    }

    @Override
    public View getCustomContentView(View oldContentView) {
        return null;
    }

}
