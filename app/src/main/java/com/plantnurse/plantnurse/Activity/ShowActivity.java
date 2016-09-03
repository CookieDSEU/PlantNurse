package com.plantnurse.plantnurse.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kot32.ksimplelibrary.activity.e.KSimpleBaseActivity;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.GetPlantInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.plantnurse.plantnurse.R.id.toolbar;
import static java.security.AccessController.getContext;


/**
 * Created by Heloise on 2016/9/1.
 */

public class ShowActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    //需要动态填充的数据
    private String mName;
    private Bitmap mIcon;
    private String mSpecies;
    private String mdifficulty;
    private int mSunIndex;
    private int mWaterIndex;
    private int mColdIndex;
    private String mIntrodution;
    private int mId;

    //需要初始化的控件
    private ImageView banner_planticon;//放图片的
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout_name;//放名字的
    private TextView text_species;
    private TextView text_difficulty;
    private RatingBar ratingBar_sun;
    private RatingBar ratingBar_water;
    private RatingBar ratingBar_cold;
    private TextView text_introdution;
    private FloatingActionButton button_collect;
    private FloatingActionButton button_adopt;
    private FloatingActionsMenu menu;
    private Context mContext;

    private boolean iscollected=false;
    private boolean isadopted=false;
    @Override
    public int initLocalData() {
        //设置名字
        mContext=this;
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView(ViewGroup view) {
        banner_planticon= (ImageView) view.findViewById(R.id.bannner);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout_name=(CollapsingToolbarLayout)view.findViewById(R.id.toolbar_layout2);
        text_species= (TextView) view.findViewById(R.id.text_species);
        text_difficulty=(TextView)view.findViewById(R.id.text_difficulty);
        ratingBar_sun= (RatingBar) view.findViewById(R.id.rating_sun);
        ratingBar_water= (RatingBar) view.findViewById(R.id.rating_water);
        ratingBar_cold= (RatingBar) view.findViewById(R.id.rating_cold);
        text_introdution= (TextView) view.findViewById(R.id.text_introdution);
        menu= (FloatingActionsMenu) view.findViewById(R.id.floatingmenu);

        button_collect=new FloatingActionButton(getBaseContext());
        button_collect.setSize(FloatingActionButton.SIZE_MINI);
        button_collect.setBackground(getResources().getDrawable(R.drawable.ic_collection1));
        button_collect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(!iscollected){
                    //收藏按钮，实现收藏界面更新(未实现)
                    button_collect.setBackground(getResources().getDrawable(R.drawable.ic_collection2));
                    Toast.makeText(mContext,"已添加到收藏夹",Toast.LENGTH_SHORT).show();
                    button_collect.setTitle("取消收藏");
                    iscollected=true;
                }else{
                    button_collect.setBackground(getResources().getDrawable(R.drawable.ic_collection1));
                    button_collect.setTitle("我要收藏");
                    Toast.makeText(mContext,"取消收藏",Toast.LENGTH_SHORT).show();
                    iscollected=false;
                }
            }
        });

        button_adopt=new FloatingActionButton(getBaseContext());
        button_adopt.setSize(FloatingActionButton.SIZE_MINI);
        button_adopt.setTitle("我要养他");
        button_adopt.setBackground(getResources().getDrawable(R.drawable.ic_adoption1));
        //收养按钮，跳转到增加植物界面
        button_adopt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(!isadopted){
                    button_adopt.setBackground(getResources().getDrawable(R.drawable.ic_adoption2));
                    button_adopt.setTitle("我已收养");
                    isadopted=true;
                    Intent intent=new Intent(ShowActivity.this,AddplantActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(ShowActivity.this,AddplantActivity.class);
                    startActivity(intent);
                }
            }
        });

        menu.addButton(button_collect);
        menu.addButton(button_adopt);
    }

    @Override
    public void initController() {

    }

    @Override
    public void onLoadingNetworkData() {
        Intent intent=getIntent();
        mName=intent.getStringExtra("name");
        mId=intent.getIntExtra("id",0);
        HashMap<String,String> param=new HashMap<String,String>();
        param.put("name",mName);
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(),this, GetPlantInfoResponse.class,param, Constants.PLANTINFO_URL,NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetPlantInfoResponse response=(GetPlantInfoResponse)result.resultObject;
                mSpecies=response.species;
                mSunIndex=response.sun;
                mWaterIndex=response.water;
                mColdIndex=response.cold;
                int difficult=response.difficulty;
                switch (difficult){
                    case 0:mdifficulty="非常简单";
                          break;
                    case 1:mdifficulty="简单";
                          break;
                    case 2:mdifficulty="一般";
                          break;
                    case 3:mdifficulty="有点难";
                          break;
                    case 4:mdifficulty="困难";
                          break;
                    case 5:mdifficulty="非常困难";
                          break;
                    default:mdifficulty=" ";
                          break;
                }

                mIntrodution=response.introduction;
                toolbarLayout_name.setTitle(mName);
                Picasso.with(mContext).load(Constants.PLANTPIC_URL+mId).into(banner_planticon);

        text_species.setText(mSpecies);
        text_difficulty.setText(mdifficulty);
        ratingBar_sun.setProgress(mSunIndex);
        ratingBar_water.setProgress(mWaterIndex);
        ratingBar_cold.setProgress(mColdIndex);
        text_introdution.setText(mIntrodution);
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }

    @Override
    public void onLoadedNetworkData(View view) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_showplant;
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

}
