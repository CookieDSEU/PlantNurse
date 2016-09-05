package com.plantnurse.plantnurse.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.kot32.ksimplelibrary.widgets.view.KLoadingView;
import com.plantnurse.plantnurse.Network.LoginResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CollectModel;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CollectAdapter;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.PlantIndexManager;
import com.plantnurse.plantnurse.utils.PlantListAdapter;
import com.plantnurse.plantnurse.utils.SortAdapter;
import com.plantnurse.plantnurse.model.SortModel;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.plantnurse.plantnurse.R.id.toolbar;

/**
 * Created by Heloise on 2016/9/3.
 */

public class CollectActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    private RecyclerView collectlist;
    private static CollectAdapter adapter; // 排序的适配器
    private List<CollectModel> sourceDateList = new ArrayList<CollectModel>();
    private Toolbar toolbar;

    @Override
    public int initLocalData() {
      return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        Log.e("collet","initView");
        collectlist= (RecyclerView) view.findViewById(R.id.collectListView);
        toolbar =(Toolbar)view.findViewById(R.id.collect_toolbar);
        toolbar.setTitle("   植物收藏夹");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        filledData();
        updateindex();
    }

    private void filledData() {
        Log.e("collect","filledData");
        //收藏列表植物个数
        //i< PlantIndexManager.getPlantIndex().response.size()
        //把网络获取的参数setName什么的就好
        //id 都是网络给的
        //这里懒得搞了，随便搞一个看看样子
        for (int i = 0; i < 1; i++) {
            CollectModel collectModel = new CollectModel();
            collectModel.setName("宝石花");
            //怎么设自己图片的url啊
            collectModel.setUrl(Constants.PLANTPIC_URL+11);
            collectModel.setAddtime("收藏于 2016.9.3");
            collectModel.setId(11);
            sourceDateList.add(collectModel);
        }
    }

    public void updateindex() {
        // 填充数据
        Log.e("collect","updateindex");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CollectActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        collectlist.setLayoutManager(linearLayoutManager);
        adapter = new CollectAdapter(CollectActivity.this, sourceDateList);
        adapter.notifyDataSetChanged();
        collectlist.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
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
        Intent in=getIntent();
        setResult(RESULT_CANCELED,in);
        finish();
        super.onBackPressed();
    }
}
