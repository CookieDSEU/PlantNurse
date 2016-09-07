package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.ChangeInfoResponse;
import com.plantnurse.plantnurse.Network.GetMyPlantResponse;
import com.plantnurse.plantnurse.Network.GetPlantInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyPlantActivity extends KSimpleBaseActivityImpl implements IBaseAction {
    //view
    private TextView birthText;
    private TextView tagText;
    private TextView nameText;
    private RatingBar ratingBar_sun;
    private RatingBar ratingBar_water;
    private RatingBar ratingBar_cold;
    private ImageView banner_planticon;//放图片的
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout_name;//放名字的
    //data
    private int selectedId;
    private String mName;
    private String mTag;
    private String mBirth;
    private int acompanyDay;
    private int mSunIndex;
    private int mWaterIndex;
    private int mColdIndex;
    private String birthday;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 插入选择铃声的toolbar菜单
        getMenuInflater().inflate(R.menu.deleteplant_toolbar_menu, menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SweetAlertDialog builder =  new SweetAlertDialog(MyPlantActivity.this, SweetAlertDialog.NORMAL_TYPE);
                builder.setTitleText("提示");
                builder.setContentText("确定要删除此植物吗？");
                builder.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        HashMap<String,String> param=new HashMap<String, String>();
                        UserInfo userInfo=(UserInfo)getSimpleApplicationContext().getUserModel();
                        param.put("userName",userInfo.getuserName());
                        param.put("nickname",DataManager.getMyPlant().response.get(selectedId).nickname);
                        DataManager.isMyPlantChanged = true;
                        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(),MyPlantActivity.this, ChangeInfoResponse.class,param,Constants.DELETEPLANT_URL,NetworkTask.GET) {
                            @Override
                            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                                ChangeInfoResponse response=(ChangeInfoResponse)result.resultObject;
                                if(response.getresponseCode()==1){
                                    SweetAlertDialog builder =  new SweetAlertDialog(MyPlantActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                    builder.setTitleText("提示");
                                    builder.setContentText("删除成功");
                                    builder.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }
                            }

                            @Override
                            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                            }
                        });
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                builder.setCancelText("取消").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                builder.show();

                return true;
            }
        });
        return true;
    }



    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        ratingBar_sun= (RatingBar) view.findViewById(R.id.myplant_rating_sun);
        ratingBar_water= (RatingBar) view.findViewById(R.id.myplant_rating_water);
        ratingBar_cold= (RatingBar) view.findViewById(R.id.myplant_rating_cold);
        birthText = (TextView) view.findViewById(R.id.myplant_birthtext);
        tagText = (TextView) view.findViewById(R.id.myplant_tagtext);
        nameText = (TextView) view.findViewById(R.id.myplant_nametext);

        banner_planticon= (ImageView) view.findViewById(R.id.myplant_bannner);
        toolbar=(Toolbar)view.findViewById(R.id.myplant_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarLayout_name=(CollapsingToolbarLayout)view.findViewById(R.id.myplant_toolbarlayout);
    }

    @Override
    public void initController() {

    }

    @Override
    public void onLoadingNetworkData() {
        Intent intent=getIntent();
        selectedId = intent.getIntExtra("id",0);
        HashMap<String,String> param=new HashMap<String,String>();
        param.put("name",mName);
        //判断是否已经收藏
        //是否登录
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), this, GetPlantInfoResponse.class, param, Constants.PLANTINFO_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetPlantInfoResponse bookResponse = (GetPlantInfoResponse) result.resultObject;
                mName = "学名: "+DataManager.getMyPlant().response.get(selectedId).name;
                mSunIndex = DataManager.getMyPlant().response.get(selectedId).sun;
                mWaterIndex = DataManager.getMyPlant().response.get(selectedId).water;
                mColdIndex = DataManager.getMyPlant().response.get(selectedId).cold;


                birthday = DataManager.getMyPlant().response.get(selectedId).birthday+"";
                Log.e("test3",birthday);
                Calendar c = Calendar.getInstance();
                int nowyear =c.get(Calendar.YEAR);
                int nowmonth = c.get(Calendar.MONTH);
                int nowday =  c.get(Calendar.DAY_OF_MONTH);
                String now = ""+nowyear;
                if(nowmonth<10)
                    now+="0"+nowmonth;
                else
                    now+=nowmonth;

                if(nowday<10)
                    now+="0"+nowday;
                else
                    now += nowday;

                Log.e("test4",now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
                Date date1 = null;
                try {
                    date1 = simpleDateFormat.parse(birthday);
                    Log.e("test1",date1+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = simpleDateFormat.parse(now);
                    Log.e("test2",date2+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar cal1 = new GregorianCalendar();
                GregorianCalendar cal2 = new GregorianCalendar();
                cal1.setTime(date1);
                cal2.setTime(date2);
                double dayCount = (cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*3600*24);//从间隔毫秒变成间隔天数
                int f = (int)dayCount+1;
                mBirth = DataManager.getMyPlant().response.get(selectedId).nickname+"已经陪伴你"+f+"天啦";

                mTag = DataManager.getMyPlant().response.get(selectedId).remark;

                toolbarLayout_name.setTitle(DataManager.getMyPlant().response.get(selectedId).nickname);

                Picasso.with(MyPlantActivity.this).load(Constants.MYPLANTPIC_URL + DataManager.getMyPlant().response.get(selectedId).pic).into(banner_planticon);

                ratingBar_sun.setProgress(mSunIndex);
                ratingBar_water.setProgress(mWaterIndex);
                ratingBar_cold.setProgress(mColdIndex);

                birthText.setText(mBirth);
                nameText.setText(mName);
                tagText.setText(mTag);
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }




    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_myplant;
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