package com.plantnurse.plantnurse.Fragment;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.Activity.AddClockActivity;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.ToastUtil;

/**
 * Created by Heloise on 2016/8/26.
 */

public class ClockFragment extends KSimpleBaseFragmentImpl implements IBaseAction {
    private Button button_add;
    public int initLocalData() {
        return 0;
    }
    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_clock;
    }

    @Override
    public void initView(ViewGroup view) {
        button_add= (Button) view.findViewById(R.id.button_addclock);

        button_add.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.e("test","点击添加按钮");
                Intent intent=new Intent(getActivity(), AddClockActivity.class);
                startActivity(intent);
            }
            });
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
