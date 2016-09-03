package com.plantnurse.plantnurse.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Network.ChangeInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.AddplantAdapter;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Eason_Tao on 2016/8/27.
 */
public class AddplantActivity extends KSimpleBaseActivityImpl implements IBaseAction,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private List<Integer> mData;//添加植物的图片列表
    private AddplantAdapter mAdapter;//添加植物的图片适配器
    private RecyclerView mRecyclerView;
    private CircleImg mImg;
    private Button overButton;

    private Calendar calendar;
    public static final String DATEPICKER_TAG = "datepicker";
    private DatePickerDialog datePickerDialog;
    private EditText _year;
    private EditText _month;
    private EditText _day;
    private EditText nameText;
    private EditText nicnameText;
    private EditText otherText;
    private RatingBar sunRatingBar;
    private RatingBar waterRatingBar;
    private RatingBar snowRatingBar;
    //data
    private String birth;
    private int birthday;
    private int birthmonth;
    private int birthyear;
    private String name;
    private String nicname;
    private String other;
    private int sun;
    private int water;
    private int snow;


    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        //初始化数据
        mImg = (CircleImg) findViewById(R.id.id_content);
        overButton = (Button) findViewById(R.id.addplant_addoverbutton);
        nameText = (EditText) findViewById(R.id.addplant_name);
        nicnameText = (EditText) findViewById(R.id.addplant_nicname);
        otherText = (EditText) findViewById(R.id.addplant_other);
        _year = (EditText) findViewById(R.id.text_birthyear);
        _month = (EditText) findViewById(R.id.text_birthmonth);
        _day = (EditText) findViewById(R.id.text_birthday);
        sunRatingBar = (RatingBar) findViewById(R.id.addplant_sun);
        waterRatingBar = (RatingBar) findViewById(R.id.addplant_water);
        snowRatingBar = (RatingBar) findViewById(R.id.addplant_snow);
        mImg.setImageResource(R.drawable.flower2);
        //关联图片，设置默认图片

        //完成按钮
        overButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.getText().equals("")||nicnameText.getText().equals("")||nameText.getText().equals("")||_year.getText().equals("")||_month.getText().equals("")||_day.getText().equals("")){
                    ToastUtil.showLong("请多留下一些它的信息吧...");
                }
                else {
                    //get birthday
                    birth = ""+_year.getText();
                    birthmonth = Integer.parseInt(_month.getText().toString());
                    birthday = Integer.parseInt(_day.getText().toString());
                    if(birthmonth<10)
                        birth = birth+"0"+birthmonth;
                    else
                        birth = birth+birthmonth;
                    if(birthday<10)
                        birth = birth+"0"+birthday;
                    else
                        birth = birth+birthday;
                    //get name ...
                    name = nameText.getText().toString();
                    nicname = nicnameText.getText().toString();
                    other = otherText.getText().toString();
                    //get setting
                    sun = sunRatingBar.getNumStars();
                    water = waterRatingBar.getNumStars();
                    snow = waterRatingBar.getNumStars();
                    HashMap<String,String> param=new HashMap<String, String>();
                    param.put("nickname",nicname);
                    param.put("name",name);
                    param.put("birthday",birth);
                    param.put("sun",sun+"");
                    param.put("water",water+"");
                    param.put("cold",snow+"");
                    param.put("remark",other);
                    UserInfo userInfo=(UserInfo)getSimpleApplicationContext().getUserModel();
                    param.put("owner",userInfo.getuserName());
                    param.put("pic","test");
                    SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getSimpleApplicationContext(), ChangeInfoResponse.class, param, Constants.ADDPLANT_URL, NetworkTask.GET) {
                        @Override
                        public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                            ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                            if (response.getresponseCode() == 1) {
                                new SweetAlertDialog(AddplantActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("添加成功！")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(AddplantActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("连接超时，添加失败！")
                                        .show();
                            }
                        }

                        @Override
                        public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                        }
                    });
                }
            }
        });

        findViewById(R.id.text_birthyear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        findViewById(R.id.text_birthmonth).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        findViewById(R.id.text_birthday).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        //选择图片recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new AddplantAdapter(this, mData);
        mAdapter.setOnItemClickLitener(new AddplantAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                mImg.setImageResource(mData.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas()
    {
        mData = new ArrayList<Integer>(Arrays.asList(R.drawable.flower1_s, R.drawable.flower2_s,
                R.drawable.flower3_s,R.drawable.flower4_s, R.drawable.flower5_s,R.drawable.flower1_s,
                R.drawable.flower2_s, R.drawable.flower3_s, R.drawable.flower4_s, R.drawable.flower5_s));
        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);

    }



    @Override
    public void initController () {
    }

    @Override
    public void onLoadingNetworkData () {

    }

    @Override
    public void onLoadedNetworkData (View contentView){

    }


    @Override
    public int getContentLayoutID () {
        return R.layout.activity_addplant;
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        month++;
        _year.setText(""+year);
        _month.setText(""+month);
        _day.setText(""+day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(AddplantActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }
}
