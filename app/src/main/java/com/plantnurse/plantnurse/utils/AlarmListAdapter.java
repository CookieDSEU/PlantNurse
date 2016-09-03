package com.plantnurse.plantnurse.utils;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.DialogPreference;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.Fragment.AlarmFragment;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yxuan on 2016/8/29.
 */
public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmItemHolder> {

    private AlarmInfo info;
    private ArrayList<HashMap<String,String>> alarmList;
    private HashMap<String,String> alarm_hashMap;
    private int alarmId;
    private AlarmFragment context;
    private LayoutInflater layoutInflater;
     int isCircleImg_Click;


    public AlarmListAdapter(AlarmFragment mContext, ArrayList<HashMap<String,String>> mAlarmList){
        this.context = mContext;
        this.alarmList=mAlarmList;
        layoutInflater = LayoutInflater.from(mContext.getActivity());
    }

    @Override
    public AlarmItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        View view= layoutInflater.inflate(R.layout.view_alarm_entry, parent, false);
        AlarmItemHolder holder = new AlarmItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AlarmItemHolder holder, final int position) {

        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        alarm_hashMap=alarmList.get(position);
        alarmId=Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
        Alarm alarm=new Alarm();
        alarm = info.getAlarmById(alarmId);

        handleInfo(holder, alarm);


        //单击出现详细信息
        holder.itemView.findViewById(R.id.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmDetail(position);
            }
        });

        //长按删除
        holder.itemView.findViewById(R.id.cardView).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteAlarm(position);
                return true;
            }
        });

        //单击CirecleImg选择开或者关闭闹钟（isAlarm）
        holder.itemView.findViewById(R.id.alarm_role).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarm=new Alarm();
                alarm = info.getAlarmById(alarmId);
                if(isCircleImg_Click==1){//原先为闹钟，点击取消
                    holder.showBackground.getBackground().setAlpha(100);
                   // holder.showRole.getBackground().setAlpha(100);
                    cancelAlarm(context.getActivity(), alarm.alarm_id);
                    alarm.isAlarm=0;
                    ToastUtil.showShort("闹钟已取消");
                    isCircleImg_Click=0;
                }else {//重新启用闹钟
                    holder.showBackground.getBackground().setAlpha(255);
//                    holder.showRole.getBackground().setAlpha(255);
                    setAlarm(context.getActivity(),alarm,0);

                }
            }
        });


    }

    //设置闹钟
    public  void setAlarm(Context context, Alarm alarm, int soundOrVibrator) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long intervalMillis = 0;
        long selectedTime = 0;
        long currentTime=System.currentTimeMillis();
        try {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = formatter.parse(alarm.time);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (alarm.frequency == 4||alarm.frequency==0) {//自定义
            intervalMillis = 0;
        } else if (alarm.frequency == 1) {//每天
            intervalMillis = 24 * 3600 * 1000;
        } else if (alarm.frequency == 2) {//隔一天
            intervalMillis = 24 * 3600 * 1000 * 2;
        }else if(alarm.frequency==3){//隔两天
            intervalMillis=24 * 3600 * 1000 * 3;
        }
        //发送闹钟请求
        Intent intent = new Intent(context,AlarmReceiver.class);
        intent.putExtra("alarm_Id", alarm.alarm_id);
        intent.putExtra("frequency",alarm.frequency);
//        intent.putExtra("soundOrVibrator", soundOrVibrator);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.alarm_id, intent, 0);

        if (alarm.frequency == 4||alarm.frequency==0) {//自定义闹钟
            if(selectedTime<=currentTime){
                ToastUtil.showShort("请选择大于当前的时间");
                alarm.isAlarm=0;
                isCircleImg_Click=0;
            }else{
                am.set(AlarmManager.RTC_WAKEUP, selectedTime, sender);
                alarm.isAlarm=1;
                isCircleImg_Click=1;
                ToastUtil.showShort("闹钟已重新启用");
            }

        } else {
            if(selectedTime<=currentTime){
                selectedTime+=24*3600*1000;
                am.setRepeating(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
            }else{
                am.setRepeating(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
            }
            alarm.isAlarm=1;
            isCircleImg_Click=1;
            ToastUtil.showShort("闹钟已重新启用");
        }

    }

    //取消闹钟
    public static void cancelAlarm(Context context,int id) {
        Intent intent = new Intent();
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    //初始化数据处理
    public void handleInfo(AlarmItemHolder holder,Alarm alarm){
        String selectedTime=alarm.time;
        int frequency=alarm.frequency;
        String plantName=alarm.plantName;
        int water=alarm.water;
        int sun=alarm.sun;
        int back=alarm.takeBack;
        int care=alarm.takeCare;
        int fetilization=alarm.fertilization;
        String tips=alarm.content;
        int roleColor=alarm.roleColor;
        isCircleImg_Click=alarm.isAlarm;



        //初始化行为
        if(water==0){
            holder.showWater.setImageResource(R.drawable.alarmaction_water_grey);
        }else{
            holder.showWater.setImageResource(R.drawable.alarmaction_water);
        }

        if(sun==0){
            holder.showSun.setBackgroundResource(R.drawable.alarmaction_sun_grey);
        }else{
            holder.showSun.setBackgroundResource(R.drawable.alarmaction_sun);
        }

        if(back==0){
            holder.showBack.setBackgroundResource(R.drawable.alarmaction_hand_grey);
        }else{
            holder.showBack.setBackgroundResource(R.drawable.alarmaction_hand);
        }

        if(care==0){
            holder.showCare.setBackgroundResource(R.drawable.alarmaction_bug_grey);
        }else{
            holder.showCare.setBackgroundResource(R.drawable.alarmaction_bug);
        }

        if(fetilization==0){
            holder.showFertilization.setBackgroundResource(R.drawable.alarmaction_fat_grey);
        }else{
            holder.showFertilization.setBackgroundResource(R.drawable.alarmaction_fat);
        }

        //分隔日期和时间
        String strDorT[]=selectedTime.split(" ");
        String date=strDorT[0];
        String time=strDorT[1];

        //初始化时间
        holder.showTime.setText(time);

        //初始化重复的选择或日期
        switch (frequency){
            case 0://用户没设置
                holder.showDate.setText(date);
                break;
            case 1://每天
                holder.showDate.setText("每天");
                break;
            case 2://隔一天
                holder.showDate.setText("隔一天");
                break;
            case 3://隔两天
                holder.showDate.setText("隔两天");
                break;
            case 4://自定义
                holder.showDate.setText(date);
                break;
        }

        //初始化植物
        String strP[]=selectedTime.split(" ");


        //初始化tips
        holder.showTips.setText("Tips:" + tips);

        //初始化角色

        switch (roleColor){
            case 1://选择绿色
                holder.showRole.setImageResource(R.drawable.alarmrole_green);
                holder.showRole.setBorderWidth(10);
                holder.showRole.setBorderColor(R.color.greenborder);
                holder.showBackground.setBackgroundResource(R.drawable.alarmcardbc_green);
                break;
            case 2://选择粉色
                holder.showRole.setImageResource(R.drawable.alarmrole_pink);
                holder.showRole.setBorderWidth(10);
                holder.showRole.setBorderColor(R.color.pinkborder);
                holder.showBackground.setBackgroundResource(R.drawable.alarmcardbc_pink);
                break;
            case 3://选择蓝色
                holder.showRole.setImageResource(R.drawable.alarmrole_blue);
                holder.showRole.setBorderWidth(10);
                holder.showRole.setBorderColor(R.color.blueborder);
                holder.showBackground.setBackgroundResource(R.drawable.alarmcardbc_blue);
                break;
//            case 4://选择黄色
//                holder.showRole.setImageResource(R.drawable.alarmrole_yellow);
//                holder.showRole.setBorderWidth(10);
//                holder.showRole.setBorderColor(R.color.yellowborder);
//                //holder.showBackground.setBackgroundResource(R.drawable.alarmcardbc_yellow);
//                break;
        }

        //初始化亮度，是否为闹钟
        if(isCircleImg_Click==1){//是闹钟

        }else{
            holder.showBackground.getBackground().setAlpha(100);
            //holder.showRole.getBackground().setAlpha(100);
        }

    }


    //单击事件
    public void showAlarmDetail(int pos){
        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        alarm_hashMap=alarmList.get(pos);
        alarmId=Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
        Alarm alarm=new Alarm();
        alarm = info.getAlarmById(alarmId);
        Intent intent = new Intent(context.getActivity(), AddAlarmActivity.class);
        intent.putExtra("alarm_Id", alarmId);
        context.getActivity().startActivity(intent);
    }

    //长按事件删除
    public void deleteAlarm(int pos){
        final int position=pos;
        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        builder.setTitle("提示");
        builder.setMessage("确定要删除闹钟么？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                info = new AlarmInfo(context.getActivity());
                alarmList = info.getAlarmList();
                alarm_hashMap = alarmList.get(position);
                alarmId = Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
                info.delete(alarmId);
                context.onResume();
            }
        });
        builder.show();
    }

    //单击选择角色
//    public void selectedRole( final AlarmItemHolder holder,final int pos){
//
//
//
//        LayoutInflater inflater = LayoutInflater.from(context.getActivity());
//        final View view = inflater.inflate(R.layout.alarm_dialog_selectrole, null);
//
//        final CircleImg role_green;
//        final CircleImg role_pink;
//        final CircleImg role_blue;
//        final CircleImg role_yellow;
//
//        role_green=(CircleImg)view.findViewById(R.id.role_green);
//        role_pink=(CircleImg)view.findViewById(R.id.role_pink);
//        role_blue=(CircleImg)view.findViewById(R.id.role_blue);
//        role_yellow=(CircleImg)view.findViewById(R.id.role_yellow);
//
//
//        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
//        builder.setTitle("选择你喜欢的角色");
//        builder.setView(view);
//        role_green.setImageResource(R.drawable.alarmrole_green);
//        role_pink.setImageResource(R.drawable.alarmrole_green);
//        role_blue.setImageResource(R.drawable.alarmrole_green);
//        role_yellow.setImageResource(R.drawable.alarmrole_green);
//        //监听点击事件
//        view.findViewById(R.id.role_green).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isClickGreen==0){//之前未选中
//                    role = 1;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_green.setBorderWidth(3);
//                    role_pink.setBorderColor(R.color.white);
//                    role_blue.setBorderColor(R.color.white);
//                    role_yellow.setBorderColor(R.color.white);
//                    role_green.setBorderColor(R.color.greenborder);
//                    isClickGreen=1;
//                    isClickPink=0;
//                    isClickBlue=0;
//                    isClickYellow=0;
//                }else{//取消选择
//                    role = 0;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_green.setBorderWidth(3);
//                    role_green.setBorderColor(R.color.white);
//                    isClickGreen=0;
//                }
//
//            }
//        });
//        view.findViewById(R.id.role_pink).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isClickPink==0){//之前未选中
//                    role = 2;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_pink.setBorderWidth(3);
//                    role_green.setBorderColor(R.color.white);
//                    role_blue.setBorderColor(R.color.white);
//                    role_yellow.setBorderColor(R.color.white);
//                    role_pink.setBorderColor(R.color.greenborder);
//                    isClickPink=1;
//                    isClickGreen=0;
//                    isClickBlue=0;
//                    isClickYellow=0;
//                }else{//取消选择
//                    role = 0;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_pink.setBorderWidth(3);
//                    role_pink.setBorderColor(R.color.white);
//                    isClickPink=0;
//                }
//            }
//        });
//        view.findViewById(R.id.role_blue).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isClickBlue==0){//之前未选中
//                    role = 3;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_blue.setBorderWidth(3);
//                    role_pink.setBorderColor(R.color.white);
//                    role_green.setBorderColor(R.color.white);
//                    role_yellow.setBorderColor(R.color.white);
//                    role_blue.setBorderColor(R.color.greenborder);
//                    isClickBlue=1;
//                    isClickPink=0;
//                    isClickGreen=0;
//                    isClickYellow=0;
//                }else{//取消选择
//                    role = 0;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_blue.setBorderWidth(3);
//                    role_blue.setBorderColor(R.color.white);
//                    isClickBlue=0;
//                }
//            }
//        });
//        view.findViewById(R.id.role_yellow).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isClickYellow==0){//之前未选中
//                    role = 4;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_yellow.setBorderWidth(3);
//                    role_pink.setBorderColor(R.color.white);
//                    role_blue.setBorderColor(R.color.white);
//                    role_green.setBorderColor(R.color.white);
//                    role_yellow.setBorderColor(R.color.greenborder);
//                    isClickYellow=1;
//                    isClickPink=0;
//                    isClickGreen=0;
//                    isClickBlue=0;
//                }else{//取消选择
//                    role = 0;
//                    // holder.showRole.setImageResource(R.drawable.alarmrole_green);
//                    role_yellow.setBorderWidth(3);
//                    role_yellow.setBorderColor(R.color.white);
//                    isClickYellow=0;
//                }
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                info = new AlarmInfo(context.getActivity());
//                alarmList = info.getAlarmList();
//                alarm_hashMap = alarmList.get(pos);
//                alarmId = Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
//                Alarm alarm = new Alarm();
//                alarm = info.getAlarmById(alarmId);
//                alarm.available = "" + role;
//                info.delete(alarmId);
//                context.onResume();
//            }
//        });
//        builder.show();
//
//    }




    @Override
    public int getItemCount() {

        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        return alarmList.size();
    }



    public class AlarmItemHolder extends ViewHolder {

        LinearLayout showBackground;
//        ImageView showBackground;
        CircleImg showRole;
        ImageView showWater;
        ImageView showSun;
        ImageView showBack;
        ImageView showCare;
        ImageView showFertilization;
        TextView showDate;
        TextView showTime;
        CircleImg showPlant1;
        CircleImg showPlant2;
        CircleImg showPlant3;
        CircleImg showPlant4;
        TextView showTips;


        public AlarmItemHolder(View itemView) {
            super(itemView);

            showBackground= (LinearLayout) itemView.findViewById(R.id.container);
            showRole = (CircleImg) itemView.findViewById(R.id.alarm_role);
            showWater= (ImageView) itemView.findViewById(R.id.action_water);
            showSun= (ImageView) itemView.findViewById(R.id.action_sun);
            showBack= (ImageView) itemView.findViewById(R.id.action_back);
            showCare= (ImageView) itemView.findViewById(R.id.action_care);
            showFertilization= (ImageView) itemView.findViewById(R.id.action_fetilization);
            showDate = (TextView) itemView.findViewById(R.id.date);
            showTime = (TextView) itemView.findViewById(R.id.time);
            showPlant1 = (CircleImg) itemView.findViewById(R.id.alarm_plant1);
            showPlant2 = (CircleImg) itemView.findViewById(R.id.alarm_plant2);
            showPlant3 = (CircleImg) itemView.findViewById(R.id.alarm_plant3);
            showPlant4 = (CircleImg) itemView.findViewById(R.id.alarm_plant4);
            showTips = (TextView) itemView.findViewById(R.id.alarm_tips);

            showRole.setBorderWidth(10);
            showRole.setBorderColor(R.color.greenborder);
            showRole.setImageResource(R.drawable.alarmrole_green);//角色默认为绿色

            //植物默认为透明
            showPlant1.setColorFilter(R.color.nocolor);
            showPlant2.setColorFilter(R.color.nocolor);
            showPlant3.setColorFilter(R.color.nocolor);
            showPlant4.setColorFilter(R.color.nocolor);

        }
    }
}