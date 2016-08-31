package com.plantnurse.plantnurse.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import fr.ganfra.materialspinner.MaterialSpinner;

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
    private Calendar calendar;
    public static final String DATEPICKER_TAG = "datepicker";
    private DatePickerDialog datePickerDialog;
    private EditText _year;
    private EditText _month;
    private EditText _day;

    @Override
    public int initLocalData() {
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        //初始化数据
        mImg = (ImageView) findViewById(R.id.id_content);

        _year = (EditText) findViewById(R.id.text_birthyear);
        _month = (EditText) findViewById(R.id.text_birthmonth);
        _day = (EditText) findViewById(R.id.text_birthday);
        mImg.setImageResource(R.drawable.flower2);
        //关联图片，设置默认图片

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
        //Toast.makeText(AddplantActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        _year.setText(""+year);
        _month.setText(""+month);
        _day.setText(""+day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(AddplantActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }
}
