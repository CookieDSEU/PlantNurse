package com.plantnurse.plantnurse.Fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.AlarmListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmFragment extends KSimpleBaseFragmentImpl implements IBaseAction {
    private RecyclerView recyclerView;
    private AlarmListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Button addBtn;
    AlarmInfo info;
    private MainApplication mApp;
    ArrayList<HashMap<String, String>> alarmList;

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        addBtn = (Button) view.findViewById(R.id.button_addalarm);

        //获取数据库内容
        info = new AlarmInfo(getActivity());
        alarmList = info.getAlarmList();

        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //设置适配器
        adapter = new AlarmListAdapter(AlarmFragment.this, alarmList);
        recyclerView.setAdapter(adapter);
        mApp = (MainApplication) getActivity().getApplication();
    }

    @Override
    public void initController() {
        //新建闹钟
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mApp.isLogined()) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), AddAlarmActivity.class);
                    intent.putExtra("alarm_Id", 0);
                /* 启动一个新的Activity */
                    AlarmFragment.this.startActivity(intent);

                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录")
                            .show();
                }

            }
        });
    }

    //返回触动
    @Override
    public void onResume() {
        super.onResume();
        //返回便刷新列表
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_alarm;
    }
}

