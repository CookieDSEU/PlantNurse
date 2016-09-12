package com.kot32.ksimplelibrary.activity.t.base;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kot32.ksimplelibrary.activity.e.KSimpleBaseActivity;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;

/**
 * Created by kot32 on 15/11/4.
 * KSimpleBaseActivity 的实现类
 */
public abstract class KSimpleBaseActivityImpl extends KSimpleBaseActivity {

    @Override
    public IBaseAction getIBaseActivityAction() {

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
    public View getCustomContentView(View v) {
        return null;
    }


    /*Toast mToast;
    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }*/
    
    Toast mToast;
    public void showToast(int resId) {
        
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), resId,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public static void showLog(String msg) {
        Log.i("KSimpleLibrary", msg);
    }

}
