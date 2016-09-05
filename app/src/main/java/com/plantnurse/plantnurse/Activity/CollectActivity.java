package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CollectModel;
import com.plantnurse.plantnurse.utils.CollectAdapter;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Heloise on 2016/9/3.
 */

public class CollectActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    private RecyclerView collectlist;
    private static CollectAdapter adapter; // 收藏的适配器
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
        //收藏列表数据完成
        for (int i = 0; i < DataManager.getMyStar().response.size(); i++) {
            CollectModel collectModel = new CollectModel();
            collectModel.setName(DataManager.getMyStar().response.get(i).name);
            collectModel.setUrl(Constants.PLANTICON_URL+DataManager.getMyStar().response.get(i).plant_id);
            collectModel.setAddtime("收藏于 "+DataManager.getMyStar().response.get(i).date.substring(0,4)+"."
            +DataManager.getMyStar().response.get(i).date.substring(5,6)+"."+DataManager.getMyStar().
                    response.get(i).date.substring(7,8));
            collectModel.setId(DataManager.getMyStar().response.get(i).plant_id);
            Log.e("collect",DataManager.getMyStar().response.get(i).name);
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
        collectlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
