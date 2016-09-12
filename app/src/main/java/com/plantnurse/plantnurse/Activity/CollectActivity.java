package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.GetMyStarResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CollectPlantModel;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CollectAdapter;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Heloise on 2016/9/3.
 */

public class CollectActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    private RecyclerView collectlist;
    private static CollectAdapter adapter; // 收藏的适配器
    private List<CollectPlantModel> sourceDateList;
    private Toolbar toolbar;

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        collectlist = (RecyclerView) view.findViewById(R.id.collectListView);
        toolbar = (Toolbar) view.findViewById(R.id.collect_toolbar);
        toolbar.setTitle("   植物收藏夹");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        filledData();
        updateindex();
    }

    private void filledData() {
        //收藏列表数据完成
        sourceDateList = new ArrayList<CollectPlantModel>();
        for (int i = 0; i < DataManager.getMyStar().response.size(); i++) {
            CollectPlantModel collectPlantModel = new CollectPlantModel();
            collectPlantModel.setName(DataManager.getMyStar().response.get(i).name);
            collectPlantModel.setUrl(Constants.PLANTICON_URL + DataManager.getMyStar().response.get(i).plant_id);
            collectPlantModel.setAddtime("收藏于 " + DataManager.getMyStar().response.get(i).date.substring(0, 4) + "."
                    + DataManager.getMyStar().response.get(i).date.substring(4, 6) + "." + DataManager.getMyStar().
                    response.get(i).date.substring(6, 8));
            collectPlantModel.setId(DataManager.getMyStar().response.get(i).plant_id);
            sourceDateList.add(collectPlantModel);
        }
    }

    public void updateindex() {
        // 填充数据
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CollectActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        collectlist.setLayoutManager(linearLayoutManager);
        UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
        adapter = new CollectAdapter(CollectActivity.this, sourceDateList, userInfo.getuserName());
        collectlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        return R.layout.activity_collect;
    }

    //按返回键，返回MainActivity界面,关闭当前页面
    @Override
    public void onBackPressed() {
        Intent in = getIntent();
        setResult(RESULT_CANCELED, in);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        HashMap param = new HashMap<>();
        if (getSimpleApplicationContext().isLogined()) {
            UserInfo ui = (UserInfo) getSimpleApplicationContext().getUserModel();
            param.put("userName", ui.getuserName());
        } else {
            param.put("userName", "blank");
        }
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), CollectActivity.this, GetMyStarResponse.class, param, Constants.GETMYSTAR_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetMyStarResponse response = (GetMyStarResponse) result.resultObject;
                DataManager.setMyStar(response);
                filledData();
                adapter.updatelist(sourceDateList);

            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
        super.onResume();
    }
}
