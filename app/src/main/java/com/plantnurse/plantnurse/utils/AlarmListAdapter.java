package com.plantnurse.plantnurse.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.Fragment.AlarmFragment;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Yxuan on 2016/8/29.
 */
public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmItemHolder> {

    private AlarmInfo info;
    private ArrayList<HashMap<String, String>> alarmList;
    private HashMap<String, String> alarm_hashMap;
    private int alarmId;
    private AlarmFragment context;
    private LayoutInflater layoutInflater;
    private Alarm alarm = new Alarm();

    /**
     * 构造函数
     *
     * @param mContext   传进AlarmFragment的上下文
     * @param mAlarmList 传进所有闹钟
     */
    public AlarmListAdapter(AlarmFragment mContext, ArrayList<HashMap<String, String>> mAlarmList) {
        this.context = mContext;
        this.alarmList = mAlarmList;
        layoutInflater = LayoutInflater.from(mContext.getActivity());
    }

    @Override
    public AlarmItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.view_alarm_entry, parent, false);
        AlarmItemHolder holder = new AlarmItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AlarmItemHolder holder, final int position) {
        //取出每一个的闹钟
        alarm_hashMap = alarmList.get(position);
        alarmId = Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
        alarm = info.getAlarmById(alarmId);
        //初始化所有的信息
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

                //每次都要重新取对应的闹钟信息
                alarm_hashMap = alarmList.get(position);
                alarmId = Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
                alarm = info.getAlarmById(alarmId);

                if (alarm.isAlarm == 1) {//原先为闹钟，点击取消
                    holder.showBackground.setAlpha(0.25f);
                    cancelAlarm(context.getActivity(), alarm.alarm_id);
                    alarm.isAlarm = 0;
                    info.update(alarm);
                    ToastUtil.showShort("闹钟已取消");
                } else {//重新启用闹钟
                    setAlarm(context.getActivity(), holder, alarm, 0);
                }
            }
        });
    }

    /**
     * 初始化数据处理
     *
     * @param holder AlarmItemHolder，引用控件资源
     * @param alarm  对哪个闹钟进行初始化
     */
    public void handleInfo(AlarmItemHolder holder, Alarm alarm) {
        //声明初始化所有闹钟信息
        String selectedTime = alarm.time;
        int frequency = alarm.frequency;
        String plantName = alarm.plantName;
        int water = alarm.water;
        int sun = alarm.sun;
        int back = alarm.takeBack;
        int care = alarm.takeCare;
        int fertilization = alarm.fertilization;
        String tips = alarm.content;
        int roleColor = alarm.roleColor;

        //初始化行为
        initAction(water, holder.showWater, R.drawable.alarmaction_water_grey, R.drawable.alarmaction_water);
        initAction(sun, holder.showSun, R.drawable.alarmaction_sun_grey, R.drawable.alarmaction_sun);
        initAction(back, holder.showBack, R.drawable.alarmaction_hand_grey, R.drawable.alarmaction_hand);
        initAction(care, holder.showCare, R.drawable.alarmaction_bug_grey, R.drawable.alarmaction_bug);
        initAction(fertilization, holder.showFertilization, R.drawable.alarmaction_fat_grey, R.drawable.alarmaction_fat);

        //分隔日期和时间
        String strDorT[] = selectedTime.split(" ");
        String date = strDorT[0];
        String time = strDorT[1];
        //初始化时间
        holder.showTime.setText(time);

        //初始化重复的选择或日期
        switch (frequency) {
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
        if (plantName != null) {
            String strP[] = alarm.plantName.split(",");
            List<CircleImg> cirImg = new ArrayList<CircleImg>(Arrays.asList
                    (holder.showPlant1, holder.showPlant2, holder.showPlant3, holder.showPlant4));
            int l = 0;
            if (strP.length > 4) {//最多显示4盆植物
                l = 4;
            } else {
                l = strP.length;
            }
            for (int i = 0; i < l; i++) {
                Picasso.with(context.getActivity()).load(Constants.MYPLANTPIC_URL
                        + strP[i]).into(cirImg.get(i));
            }
        }

        //初始化tips
        holder.showTips.setText("Tips:" + tips);

        //初始化角色
        switch (roleColor) {
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
        }

        //初始化背景亮度，是否为闹钟
        if (alarm.isAlarm == 1) {//是闹钟
            holder.showBackground.setAlpha(1);
        } else {
            holder.showBackground.setAlpha(0.25f);
        }
    }

    /**
     * 单击事件
     *
     * @param pos 第几条item
     */
    public void showAlarmDetail(int pos) {
        info = new AlarmInfo(context.getActivity());
        alarmList = info.getAlarmList();
        alarm_hashMap = alarmList.get(pos);
        alarmId = Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
        Intent intent = new Intent(context.getActivity(), AddAlarmActivity.class);
        intent.putExtra("alarm_Id", alarmId);
        context.getActivity().startActivity(intent);
    }

    /**
     * 长按事件删除
     *
     * @param pos 第几条item
     */
    public void deleteAlarm(int pos) {
        final int position = pos;
        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        SweetAlertDialog builder = new SweetAlertDialog(context.getActivity(), SweetAlertDialog.NORMAL_TYPE);
        builder.setTitleText("提示");
        builder.setContentText("确定要删除闹钟吗？");
        builder.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                info = new AlarmInfo(context.getActivity());
                alarmList = info.getAlarmList();
                alarm_hashMap = alarmList.get(position);
                alarmId = Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
                if (alarm.isAlarm == 1) {//如果是个闹钟，则取消掉
                    cancelAlarm(context.getActivity(), alarmId);
                }
                info.delete(alarmId);
                context.onResume();
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
    }

    /**
     * 设置闹钟
     *
     * @param context         上下文
     * @param holder          AlarmItemHolder，引用控件资源
     * @param alarm           对哪个闹钟设置
     * @param soundOrVibrator 震动或响铃（未启用）
     */
    public void setAlarm(Context context, AlarmItemHolder holder, Alarm alarm, int soundOrVibrator) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long intervalMillis = 0;
        long selectedTime = 0;
        long currentTime = System.currentTimeMillis();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentT = formatter.format(currentTime);
        String[] strCDorT = currentT.split(" ");//分隔当前时间日期
        String[] strSDorT = alarm.time.split(" ");//分隔已选择的时间日期
        try {
            Date date = formatter.parse(alarm.time);
            selectedTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (alarm.frequency == 4 || alarm.frequency == 0) {//自定义
            intervalMillis = 0;
        } else if (alarm.frequency == 1) {//每天
            intervalMillis = 24 * 3600 * 1000;
        } else if (alarm.frequency == 2) {//隔一天
            intervalMillis = 24 * 3600 * 1000 * 2;
        } else if (alarm.frequency == 3) {//隔两天
            intervalMillis = 24 * 3600 * 1000 * 3;
        }
        //发送闹钟请求
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarm_Id", alarm.alarm_id);
        intent.putExtra("frequency", alarm.frequency);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.alarm_id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);

        if (alarm.frequency == 4 || alarm.frequency == 0) {//自定义闹钟

            if (selectedTime <= currentTime) {
                alarm.isAlarm = 0;
                ToastUtil.showShort("请选择大于当前的时间");
            } else {
                am.set(AlarmManager.RTC_WAKEUP, selectedTime, sender);
                alarm.isAlarm = 1;
                holder.showBackground.setAlpha(1);
                ToastUtil.showShort("闹钟已重新启用");
            }
        } else {
            try {
                String time = strCDorT[0] + " " + strSDorT[1];//将当前日期和选择的时间结合
                Date date = formatter.parse(time);
                selectedTime = date.getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (selectedTime <= currentTime) {
                selectedTime += intervalMillis;//直接跳到下一次的时间
                alarm.time = formatter.format(selectedTime);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am.setWindow(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
                } else {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am.setWindow(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
                } else {
                    am.setRepeating(AlarmManager.RTC_WAKEUP, selectedTime, intervalMillis, sender);
                }
            }
            alarm.isAlarm = 1;
            holder.showBackground.setAlpha(1);
            ToastUtil.showShort("闹钟已重新启用");
        }
        info.update(alarm);
    }

    /**
     * 取消闹钟
     *
     * @param context 上下文
     * @param id      取消哪个闹钟
     */
    public static void cancelAlarm(Context context, int id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * 初始化行为图标
     *
     * @param i      行为对应的数
     * @param img    哪个ImageView
     * @param grey   灰色图标
     * @param nogrey 亮色图标
     */
    public void initAction(int i, ImageView img, int grey, int nogrey) {
        if (i == 0) {
            img.setImageResource(grey);
        } else {
            img.setImageResource(nogrey);
        }
    }

    @Override
    public int getItemCount() {
        info = new AlarmInfo(context.getActivity());
        alarmList = info.getAlarmList();
        return alarmList.size();
    }

    public class AlarmItemHolder extends ViewHolder {
        LinearLayout showBackground;
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
            showBackground = (LinearLayout) itemView.findViewById(R.id.container);
            showRole = (CircleImg) itemView.findViewById(R.id.alarm_role);
            showWater = (ImageView) itemView.findViewById(R.id.action_water);
            showSun = (ImageView) itemView.findViewById(R.id.action_sun);
            showBack = (ImageView) itemView.findViewById(R.id.action_back);
            showCare = (ImageView) itemView.findViewById(R.id.action_care);
            showFertilization = (ImageView) itemView.findViewById(R.id.action_fetilization);
            showDate = (TextView) itemView.findViewById(R.id.date);
            showTime = (TextView) itemView.findViewById(R.id.time);
            showPlant1 = (CircleImg) itemView.findViewById(R.id.alarm_plant1);
            showPlant2 = (CircleImg) itemView.findViewById(R.id.alarm_plant2);
            showPlant3 = (CircleImg) itemView.findViewById(R.id.alarm_plant3);
            showPlant4 = (CircleImg) itemView.findViewById(R.id.alarm_plant4);
            showTips = (TextView) itemView.findViewById(R.id.alarm_tips);

            //植物默认为透明
            showPlant1.setColorFilter(R.color.nocolor);
            showPlant2.setColorFilter(R.color.nocolor);
            showPlant3.setColorFilter(R.color.nocolor);
            showPlant4.setColorFilter(R.color.nocolor);
        }
    }
}