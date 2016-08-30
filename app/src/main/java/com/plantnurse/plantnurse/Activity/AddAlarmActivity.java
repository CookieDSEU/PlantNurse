package com.plantnurse.plantnurse.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.utils.AddplantAdapter;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.SelectPlantAdapter;
import com.plantnurse.plantnurse.utils.ToastUtil;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
        implements IBaseAction, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    /**
     * Create by Heloise
     * Begin
     */
    private NumberPicker numberPicker_h;
    private NumberPicker numberPicker_m;
    private NumberPicker numberPicker_ampm;
    private boolean iseveryday_clicked = false;
    private boolean isdayoverday_clicked = false;
    private boolean isthreeday_clicked = false;
    private boolean iswater_clicked = false;
    private boolean issun_clicked = false;
    private boolean isback_clicked = false;
    private boolean isworm_clicked = false;
    private boolean ismedicine_clicked = false;
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
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    /**
     * End
     * Create by Heloise
     */

    private RecyclerView recyclerView;
    private SelectPlantAdapter adapter;//添加植物的图片适配器
    private List<Integer> plantData;//添加植物的图片列表
    private ImageView plantView;

    String currentTime;//起始时间
    long selectedTime;//选定的时间
    private AlarmManager am;
    Intent intent;
    PendingIntent pendingIntent;
    private int alarm_Id;//闹钟的ID号
    private int isAlarm = 0;//判断是否有添加闹钟,0为没有，1为有
    private String mtext, mtime;//所编辑的内容和时间
    //设置时间格式
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    EditText editText;


    @Override
    public int initLocalData() {
        //获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        currentTime = formatter.format(curDate);

        //获取传来的值，新建闹钟alarm_Id=0
        Intent intent = getIntent();
        alarm_Id = intent.getIntExtra("alarm_Id", 0);
        plantData = new ArrayList<Integer>(Arrays.asList(R.drawable.sunny, R.drawable.cloudy,
                R.drawable.cloudy_2, R.drawable.cloudy_3, R.drawable.rainy_2, R.drawable.rainy,
                R.drawable.rainy_3));

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

        editText = (EditText) view.findViewById(R.id.edit_other);

        //选择图片recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_selectPlant);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        adapter = new SelectPlantAdapter(this, plantData);
        adapter.setOnItemClickLitener(new SelectPlantAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                ToastUtil.showShort(position + "");
                plantView.setImageResource(plantData.get(position));
            }
        });
        recyclerView.setAdapter(adapter);


        //alarm_Id不为0则表示是打开以前的闹钟
        if(alarm_Id!=0){
            AlarmInfo info = new AlarmInfo(getSimpleApplicationContext());
            Alarm alarm = new Alarm();
            alarm = info.getAlarmById(alarm_Id);
            //载入数据
            editText.setText(alarm.content);
            //获得时间
            mtime = alarm.time;
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
            }
        });

        button_userdefine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        button_everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iseveryday_clicked) {
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    iseveryday_clicked = false;
                } else {
                    button_everyday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    iseveryday_clicked = true;
                }
            }
        });

        button_dayoverday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isdayoverday_clicked) {
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isdayoverday_clicked =false;
                } else {
                    button_dayoverday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isdayoverday_clicked = true;
                }
            }
        });

        button_threeday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isthreeday_clicked) {
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isthreeday_clicked = false;
                } else {
                    button_threeday.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isthreeday_clicked = true;
                }
            }
        });

        button_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iswater_clicked) {
                    button_water.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    iswater_clicked = false;
                } else {
                    button_water.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    iswater_clicked = true;
                }
            }
        });

        button_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (issun_clicked) {
                    button_sun.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    issun_clicked = false;
                } else {
                    button_sun.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    issun_clicked = true;
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isback_clicked) {
                    button_back.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isback_clicked = false;
                } else {
                    button_back.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isback_clicked = true;
                }
            }
        });

        button_worm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isworm_clicked) {
                    button_worm.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    isworm_clicked = false;
                } else {
                    button_worm.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    isworm_clicked = true;
                }
            }
        });

        button_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ismedicine_clicked) {
                    button_medicine.setBackgroundColor(getResources().getColor(R.color.color_Grey));
                    ismedicine_clicked = false;
                } else {
                    button_medicine.setBackgroundColor(getResources().getColor(R.color.ic_color));
                    ismedicine_clicked = true;
                }
            }
        });

        //      if (savedInstanceState != null) {
        DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
        if (dpd != null) {
            dpd.setOnDateSetListener(this);
        }

        TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
        if (tpd != null) {
            tpd.setOnTimeSetListener(this);
        }


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

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(AddAlarmActivity.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(AddAlarmActivity.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
        if(hourOfDay<12){
            numberPicker_ampm.setValue(0);
            numberPicker_h.setValue(hourOfDay);
        }else{
            numberPicker_ampm.setValue(1);
            numberPicker_h.setValue(hourOfDay-12);
        }
        numberPicker_m.setValue(minute);

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



    /*
    //设置闹钟
    public void setAlarm(){
        try {
            Date date = formatter.parse(mtime);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //发送闹钟请求
        intent = new Intent(getSimpleApplicationContext(), AlarmReceiver.class);
        intent.putExtra("alarm_Id", alarm_Id);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(getSimpleApplicationContext(), alarm_Id, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, selectedTime, pendingIntent);
    }
*/

    /**
     * 返回该界面触动
     * 接受selectActivity的传值：time？plant？
     */
    @Override
    public void onResume() {
        super.onResume();
        //接受来自SelectActivity.class的传值：isAlarm(0为无添加闹钟，1为添加闹钟）
        //
    }


    /**
     * 返回触动事件
     * 1、若为新建闹钟（alarm_Id=0）：
     * （1）无内容：不能添加闹钟，返回时无需操作
     * （2）有内容：①无闹钟（time=null）:只需将content，plant载入数据库
     *              ②有闹钟：content、time、plant，setAlarm（）
     * 2、以前的闹钟（alarm_Id=alarm_id）：
     * （1）内容删为空：则将数据库对应内容删除
     * （2）内容不为空：①无闹钟（time=null）:只需将content，plant载入数据库
     *              ②有闹钟：content、time、plant，重新setAlarm（）
     */

    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlarmInfo info=new AlarmInfo(getSimpleApplicationContext());
        Alarm alarm=new Alarm();
        alarm.content = editText.getText().toString();
        if (alarm.content==null&&alarm.time==null) {

            //若内容为空或时间为空则取消新建备忘录，返回闹钟界面

        } else {
            //内容不为空则将数据写入数据库

            //获得的是最新的闹钟内容
            alarm.content=editText.getText().toString();
            alarm.time=mtime;
            alarm.isAlarm=isAlarm;

            if(alarm_Id==0){//一个新的闹钟
                info.insert(alarm);
                //alarm_Id=info.insert(alarm);
                ToastUtil.showShort("New Alarm Insert");
                if(isAlarm==0){

                    //没有设闹钟

                }else {
                    //setAlarm();
                }
            }else{//更新闹钟
                info.update(alarm);
                ToastUtil.showShort("Alarm Record updated");
                if(isAlarm==0){
                    //未新加闹钟
                }else {
                    //setAlarm();
                }
            }
        }
    }*/


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

