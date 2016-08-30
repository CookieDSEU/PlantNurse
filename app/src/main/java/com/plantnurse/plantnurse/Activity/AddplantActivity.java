package com.plantnurse.plantnurse.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.AddplantAdapter;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;
/**
 * Created by Eason_Tao on 2016/8/27.
 */
public class AddplantActivity extends KSimpleBaseActivityImpl implements IBaseAction,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private List<Integer> mData;//添加植物的图片列表
    private AddplantAdapter mAdapter;//添加植物的图片适配器
    private RecyclerView mRecyclerView;
    private ImageView mImg;
//    private Button dateButton;
//    private Button timeButton;
    //data picker
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;


    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        //初始化数据
        mImg = (ImageView) findViewById(R.id.id_content);
//        dateButton = (Button) findViewById(R.id.dateButton);
//        timeButton = (Button) findViewById(R.id.timeButton);
        mImg.setImageResource(R.drawable.flower2);
        //关联图片，设置默认图片


        findViewById(R.id.dateButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.timeButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(true);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
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
                ToastUtil.showShort(position+"");
                mImg.setImageResource(mData.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas()
    {
        mData = new ArrayList<Integer>(Arrays.asList(R.drawable.flower1, R.drawable.flower2,
                R.drawable.flower3, R.drawable.flower4, R.mipmap.flower5,R.drawable.flower1, R.drawable.flower2,
                R.drawable.flower3, R.drawable.flower4, R.mipmap.flower5));
        calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
    }

//    private boolean isVibrate() {
//        return ((CheckBox) findViewById(R.id.checkBoxVibrate)).isChecked();
//    }
//
//    private boolean isCloseOnSingleTapDay() {
//        return ((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapDay)).isChecked();
//    }
//
//    private boolean isCloseOnSingleTapMinute() {
//        return ((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapMinute)).isChecked();
//    }

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
        Toast.makeText(AddplantActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(AddplantActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }
}
