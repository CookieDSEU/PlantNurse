package com.plantnurse.plantnurse.Fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.Util;

/**
 * Created by Eason_Tao on 2016/8/22.
 */
public class MyFragment extends KSimpleBaseFragmentImpl implements IBaseAction {
    private ImageView avatarview;
    private TextView usnview;
    private MainApplication mApp;
    Bitmap avabitmap;
    @Override
    public int initLocalData() {
        mApp=(MainApplication)getActivity().getApplication();

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {

        avatarview=(ImageView)view.findViewById(R.id.ava_imv);
        usnview=(TextView)view.findViewById(R.id.usn_txv);
    }

    @Override
    public void initController() {

        if(mApp.isLogined()){
            UserInfo userInfo=(UserInfo) mApp.getUserModel();
            usnview.setText("Hi,"+userInfo.getuserName());
        }
    }

    @Override
    public void onLoadingNetworkData() {
        if(mApp.isLogined()){
            UserInfo userInfo=(UserInfo) mApp.getUserModel();
            String temp= Constants.AVATAR_URL+"?id="+userInfo.getuserName();
            avabitmap=Util.getHttpBitmap(temp);
        }

    }

    @Override
    public void onLoadedNetworkData(View contentView) {
        if(mApp.isLogined()){
            avatarview.setImageBitmap(avabitmap);
        }

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_my;
    }
}
