package com.plantnurse.plantnurse.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.Fragment.AlarmFragment;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.AlarmReceiver;
import com.plantnurse.plantnurse.utils.SelectPlantAdapter;
import com.plantnurse.plantnurse.utils.ToastUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;


import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yxuan on 2016/8/26.
 * 接受AlarmFragment的传值：alarm_Id，确定是新建还是之前有的闹钟
 * （新建则insert时数据库alarm_id自动+1）
 * 接受传值后读取数据库，content、time、plant
 * （1）方案：只需将content传入数据库
 * 首选（2）方案：需要获取SelectActivity.class传入的值：time、plant，写入数据库，再设置闹钟
 */
public class AddAlarmActivity extends KSimpleBaseActivityImpl
        implements IBaseAction, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /**
     * Create by Heloise
     * Begin
     */
    private NumberPicker numberPicker_h;
    private NumberPicker numberPicker_m;
    private NumberPicker numberPicker_ampm;
    private int iseveryday_clicked = 0;
    private int isdayoverday_clicked = 0;
    private int isthreeday_clicked = 0;
    private int isuserdefine_clicked = 0;
    private int iswater_clicked = 0;
    private int issun_clicked = 0;
    private int isback_clicked = 0;
    private int isworm_clicked = 0;
    private int ismedicine_clicked = 0;
    private Button button_everyday;
    private Button button_dayoverday;
    private Button button_threeday;
    private Button button_water;
    private Button button_sun;
    private Button button_back;
    private Button button_worm;
    private Button button_medicine;
    private Button button_time;
    private Button button_userdefine;
    private Button button_ok;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    /**
     * End
     * Create by Heloise
     */

    private RecyclerView recyclerView;//选择植物列表
    private SelectPlantAdapter adapter;//添加植物的图片适配器
    private List<Integer> plantData;//添加植物的图片列表
    EditText editText;//备注
    private ImageView plantView;

    private long currentTime;//当前时间
    long selectedTime;//选定的时间
    //private AlarmManager am;
    //Intent intent;
    //PendingIntent pendingIntent;
    private int alarm_Id;//闹钟的ID号
    private int isAlarm = 0;//判断是否有添加闹钟,0为没有，1为有
    private String text;//所编辑的内容
    private String date;//所选择的日期
    private String time;//所选择的时间
    private int frequency;//设置重复次数对应的值：每天1、隔一天2、隔两天3、自定义4、无选择默认为当天时间0
    private AlarmInfo info;//闹钟的增删查改等工作
    private Alarm alarm;//获取闹钟的相关属性

    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置时间格式
    int ampm;//NumberPicker的上下午
    int hour;//NumberPicker的小时
    int min;//NumberPicker的分钟





    @Override
    public int initLocalData() {

        //获取AlarmFragment传来的值，新建闹钟的alarm_Id=0
        Intent intent = getIntent();
        alarm_Id = intent.getIntExtra("alarm_Id", 0);

        //需要从数据库中读取用户所有的植物，
        plantData = new ArrayList<Integer>(Arrays.asList(R.drawable.sunny, R.drawable.cloudy,
                R.drawable.cloudy_2, R.drawable.cloudy_3, R.drawable.rainy_2, R.drawable.rainy,
                R.drawable.rainy_3));

        //初始化对应闹钟
        info = new AlarmInfo(getSimpleApplicationContext());
        alarm = new Alarm();

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        /**
         * Create by Heloise
         * Begin
         */
        numberPicker_h = (NumberPicker) findViewById(R.id.numberPicker_hour);
        numberPicker_m = (NumberPicker) findViewById(R.id.numberPicker_minute);
        numberPicker_ampm = (NumberPicker) findViewById(R.id.numberPicker_AMPM);
        String[] ampm = {"上午", "下午"};
        numberPicker_ampm.setDisplayedValues(ampm);
        numberPicker_ampm.setMinimumHeight(0);
        numberPicker_ampm.setMaxValue(1);
        numberPicker_h.setMinValue(0);
        numberPicker_h.setMaxValue(11);
        numberPicker_m.setMinValue(0);
        numberPicker_m.setMaxValue(59);

        setNumberPickerDividerColor(numberPicker_ampm);
        setNumberPickerDividerColor(numberPicker_h);
        setNumberPickerDividerColor(numberPicker_m);

        button_everyday = (Button) findViewById(R.id.button_everyday);
        button_dayoverday = (Button) findViewById(R.id.button_dayoverday);
        button_threeday = (Button) findViewById(R.id.button_threeday);
        button_water = (Button) findViewById(R.id.button_water);
        button_sun = (Button) findViewById(R.id.button_sun);
        button_back = (Button) findViewById(R.id.button_back);
        button_worm = (Button) findViewById(R.id.button_worm);
        button_medicine = (Button) findViewById(R.id.button_medicine);
        button_time = (Button) findViewById(R.id.button_time);
        button_userdefine = (Button) findViewById(R.id.button_userdefine);
        button_ok = (Button) findViewById(R.id.button_ok);

        calendar = Calendar.getInstance();

        datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), true);
        timePickerDialog = TimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false, false);
        /**
         * End
         * Create by Heloise
         */

        //备注
        editText = (EditText) view.findViewById(R.id.edit_other);
        //选择图片recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_selectPlant);
        //设置recyclerView的布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置recyclerView的适配器
        adapter = new SelectPlantAdapter(this, plantData);
        adapter.setOnItemClickLitener(new SelectPlantAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showShort(position + "");
                plantView.setImageResource(plantData.get(position));
            }
        });
        recyclerView.setAdapter(adapter);


        //初始化数据
        //alarm_Id不为0则表示是打开以前的闹钟
        if (alarm_Id != 0) {
            alarm = info.getAlarmById(alarm_Id);
            editText.setText(alarm.content);
            time = alarm.time;
            isAlarm = alarm.isAlarm;
            frequency = alarm.frequency;
            iswater_clicked = alarm.water;
            issun_clicked = alarm.sun;
            isback_clicked = alarm.takeBack;
            isworm_clicked = alarm.takeCare;
            ismedicine_clicked = alarm.fertilization;

            //初始化已设置的闹钟按钮
            repeatClickEvent(frequency);
            actionClickEvent(iswater_clicked, button_water);
            actionClickEvent(issun_clicked, button_sun);
            actionClickEvent(isback_clicked, button_back);
            actionClickEvent(isworm_clicked, button_worm);
            actionClickEvent(ismedicine_clicked, button_medicine);

        }

    }

    //重复闹钟按钮的初始化颜色
    public void repeatClickEvent(int i) {
        switch (i) {
            case 1:
                iseveryday_clicked=1;
                button_everyday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                break;
            case 2:
                isdayoverday_clicked=1;
                button_dayoverday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                break;
            case 3:
                isthreeday_clicked=1;
                button_threeday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                break;
            case 4:
                isuserdefine_clicked=1;
                button_userdefine.setBackgroundColor(getResources().getColor(R.color.ic_color));
                break;
        }

    }

    //选择行为按钮的初始化颜色
    public void actionClickEvent(int i, Button b) {
        switch (i) {
            case 0:
                b.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                break;
            case 1:
                b.setBackgroundColor(getResources().getColor(R.color.ic_color));
                break;
        }

    }

    @Override
    public void initController() {

        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timePickerDialog.setVibrate(isVibrate());
                //timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
                isAlarm=1;
            }
        });


        numberPicker_ampm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (oldVal != newVal) {
                    isAlarm = 1;
                    ampm = newVal;
                }
            }
        });

        numberPicker_h.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal){
                    isAlarm=1;
                    hour=newVal;
                }
            }
        });

        numberPicker_m.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal){
                    isAlarm=1;
                    min=newVal;
                }
            }
        });

        /**
         * ButtonGroup:选择重复的按钮只能选择一个
         */
        button_userdefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isuserdefine_clicked == 1) {//用户取消自定义，则日期要变
                    //获取当前时间
                    currentTime=System.currentTimeMillis();
                    //时间处理
                    ampm=numberPicker_ampm.getValue();
                    hour=numberPicker_h.getValue();
                    min=numberPicker_m.getValue();
                    if(ampm==1){
                        hour+=12;
                    }
                    //获取设置的时间(取消之前设置的日期）
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                            (Calendar.DAY_OF_MONTH),hour, min, 10);
                    selectedTime=calendar.getTimeInMillis();
                    button_userdefine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isuserdefine_clicked = 0;
                    frequency=0;
                    date=null;
                } else {
                    //若选择自定义，则其他的按钮全为false，颜色设为灰色
                    iseveryday_clicked = 0;
                    isdayoverday_clicked = 0;
                    isthreeday_clicked = 0;
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    //自定义按钮亮起，为true
                    button_userdefine.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isuserdefine_clicked = 1;
                    frequency = 4;

                    //datePickerDialog.setVibrate(isVibrate());
                    datePickerDialog.setYearRange(1985, 2028);
                    //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

                }

            }
        });

        button_everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iseveryday_clicked == 1) {
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    iseveryday_clicked = 0;
                } else {
                    //若用户选择每天，其他按钮false
                    isuserdefine_clicked = 0;
                    isdayoverday_clicked = 0;
                    isthreeday_clicked = 0;
                    button_userdefine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    //选择每天按钮
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    iseveryday_clicked = 1;
                    frequency = 1;
                }
            }
        });

        button_dayoverday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isdayoverday_clicked == 1) {
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isdayoverday_clicked = 0;
                } else {
                    //其他按钮false
                    iseveryday_clicked = 0;
                    isuserdefine_clicked = 0;
                    isthreeday_clicked = 0;
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_userdefine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    //选择隔一天按钮
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isdayoverday_clicked = 1;
                    frequency = 2;
                }
            }
        });

        button_threeday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isthreeday_clicked == 1) {
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isthreeday_clicked = 0;
                } else {
                    //其他false
                    iseveryday_clicked = 0;
                    isdayoverday_clicked = 0;
                    isthreeday_clicked = 0;
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    //选择隔两天
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isthreeday_clicked = 1;
                    frequency = 3;
                }
            }
        });
        /**
         * ButtonGroup End
         */


        /**
         * ButtonGroup：可同时选择多个
         */
        button_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iswater_clicked == 1) {
                    button_water.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    iswater_clicked = 0;
                } else {
                    button_water.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    iswater_clicked = 1;
                }
            }
        });

        button_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (issun_clicked == 1) {
                    button_sun.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    issun_clicked = 0;
                } else {
                    button_sun.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    issun_clicked = 1;
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isback_clicked == 1) {
                    button_back.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isback_clicked = 0;
                } else {
                    button_back.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isback_clicked = 1;
                }
            }
        });

        button_worm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isworm_clicked == 1) {
                    button_worm.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isworm_clicked = 0;
                } else {
                    button_worm.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isworm_clicked = 1;
                }
            }
        });

        button_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ismedicine_clicked == 1) {
                    button_medicine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    ismedicine_clicked = 0;
                } else {
                    button_medicine.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    ismedicine_clicked = 1;
                }
            }
        });
        /**
         * ButtonGroup End
         */



        //      if (savedInstanceState != null) {
        DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
        if (dpd != null) {
            dpd.setOnDateSetListener(this);
        }

        TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
        if (tpd != null) {
            tpd.setOnTimeSetListener(this);
        }



        //确认键，将所有东西存入数据库
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalSelectedTime();

                //获得的是最新的闹钟内容
                alarm.content=editText.getText().toString();
                alarm.isAlarm=isAlarm;
                alarm.time = formatter.format(selectedTime);
                alarm.frequency=frequency;
                //alarm.plantName=
                alarm.water=iswater_clicked;
                alarm.sun=issun_clicked;
                alarm.takeBack=isback_clicked;
                alarm.takeCare=isworm_clicked;
                alarm.fertilization=ismedicine_clicked;

                if(alarm_Id==0){//新闹钟
                    info.insert(alarm);
                    setAlarm(AddAlarmActivity.this,alarm.frequency,alarm.time,alarm.alarm_id,0);
                    ToastUtil.showShort("New Alarm!");
                }else{//以前设的闹钟
                    info.update(alarm);
                    setAlarm(AddAlarmActivity.this,alarm.frequency,alarm.time,alarm.alarm_id,0);
                    ToastUtil.showShort("Alarm Update!");
                }
                ToastUtil.showShort(alarm.time);
                finish();
            }
        });


    }

    //最终的选择时间
    public void finalSelectedTime(){
        //获取当前时间
        currentTime=System.currentTimeMillis();
        String mtime;
        //时间处理
        ampm=numberPicker_ampm.getValue();
        hour=numberPicker_h.getValue();
        min=numberPicker_m.getValue();
        if(ampm==1){
            hour+=12;
        }
        String time= hour+":"+min;
        if(frequency==4){//自定义闹钟
            mtime=date+" "+time;
            try {
                Date date = formatter.parse(mtime);
                selectedTime = date.getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{//非自定义
            //获取设置的时间
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                    (Calendar.DAY_OF_MONTH), hour, min, 10);
            selectedTime=calendar.getTimeInMillis();
            //若设置的时间比当前的小，则顺延到明天
            if(selectedTime<=currentTime) {
                selectedTime+=24*3600*1000;
            }
        }

    }


    /**
     * @param frequency       周期性时间间隔的标志
     * @param time            时间
     * @param id              闹钟的id
     * @param soundOrVibrator 2表示声音和震动都执行，1表示只有铃声提醒，0表示只有震动提醒
     */
    public static void setAlarm(Context context, int frequency, String time, int id, int soundOrVibrator) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long intervalMillis = 0;
        long selectedTime = 0;
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = formatter.parse(time);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (frequency == 4||frequency==0) {//自定义
            intervalMillis = 0;
        } else if (frequency == 1) {//每天
            intervalMillis = 24 * 3600 * 1000;
        } else if (frequency == 2) {//隔一天
            intervalMillis = 24 * 3600 * 1000 * 2;
        }else if(frequency==3){//隔两天
            intervalMillis=24 * 3600 * 1000 * 3;
        }
        Intent intent = new Intent();
        intent.setClass(context,AlarmReceiver.class);
//        intent.putExtra("intervalMillis", intervalMillis);
//        intent.putExtra("msg", tips);
        intent.putExtra("id", id);
//        intent.putExtra("soundOrVibrator", soundOrVibrator);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, 0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            am.setWindow(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
//        } else {
            if (frequency == 4||frequency==0) {//自定义闹钟
                am.set(AlarmManager.RTC_WAKEUP, selectedTime, sender);
            } else {
                am.setRepeating(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
            }
//        }
    }



    //自定义闹钟选择
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        int ampm;//上午下午的值
        int hour;//NumberPicker的小时数
        int min;//NumberPicker的分钟数
        String mtime;//分钟数<10
        String htime;//小时数<10
        String ftime;//最终时间
        month++;
        date = year + "-" + month + "-" + day;

        //获取当前时间
        currentTime=System.currentTimeMillis();
        //时间处理
        ampm=numberPicker_ampm.getValue();
        hour=numberPicker_h.getValue();
        min=numberPicker_m.getValue();
        htime=""+hour;
        mtime=""+min;
        if(ampm==1){
            hour+=12;
        }
        if(hour<10){
            htime="0"+hour;
        }
        if(min<10){
            mtime="0"+min;
        }
        time= htime+":"+mtime;
        ftime=date+" "+time;
        try {
            Date date = formatter.parse(ftime);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(selectedTime<=currentTime){
            button_userdefine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
            isuserdefine_clicked = 0;
            frequency=0;
            date=null;
            ToastUtil.showShort("请设置大于当前时间的闹钟！");

        }else{
            ToastUtil.showShort(ftime);
        }

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        //获取当前时间
        currentTime=System.currentTimeMillis();
        //获取设置的时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hourOfDay, minute, 10);
        selectedTime=calendar.getTimeInMillis();

        if (hourOfDay < 12) {
            numberPicker_ampm.setValue(0);
            numberPicker_h.setValue(hourOfDay);
        } else {
            numberPicker_ampm.setValue(1);
            numberPicker_h.setValue(hourOfDay - 12);
        }
        numberPicker_m.setValue(minute);
        //若设置的时间比当前的小，则顺延到明天
        if(selectedTime<=currentTime) {
            selectedTime+=24*3600*1000;
        }

        time = hourOfDay + ":" + minute;
        ToastUtil.showShort(time);
    }






    /**
     * Create by Heloise
     * Begin
     */
    //就是他们三个找不着
    //莫名其妙啊
    private boolean isVibrate() {
        return true;
    }

    private boolean isCloseOnSingleTapDay() {
        return true;
    }

    private boolean isCloseOnSingleTapMinute() {
        return true;
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(getResources().getColor(android.R.color.black)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    /**
     * End
     * Create by Heloise
     */



    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_addalarm;
    }


}

