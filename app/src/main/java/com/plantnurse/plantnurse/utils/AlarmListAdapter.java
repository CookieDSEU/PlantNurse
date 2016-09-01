package com.plantnurse.plantnurse.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.Fragment.AlarmFragment;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.Alarm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yxuan on 2016/8/29.
 */
public class AlarmListAdapter
        extends RecyclerView.Adapter<AlarmListAdapter.AlarmItemHolder> {

    private AlarmInfo info;
    private ArrayList<HashMap<String,String>> alarmList;
    private HashMap<String,String> alarm_hashMap;
    private int alarmId;
    private AlarmFragment context;
    private LayoutInflater layoutInflater;
    //private SlidingViewClickListener mIDeleteBtnClickListener;


    public AlarmListAdapter(AlarmFragment mContext, ArrayList<HashMap<String,String>> mAlarmList) {
        this.context = mContext;
        this.alarmList=mAlarmList;
        layoutInflater = LayoutInflater.from(mContext.getActivity());
    }

    @Override
    public AlarmItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        View view= layoutInflater.inflate(R.layout.view_alarm_entry, parent,
                false);
        AlarmItemHolder holder = new AlarmItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmItemHolder holder, int position) {
        //info = new AlarmInfo(context.getActivity());
        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        alarm_hashMap=alarmList.get(position);
        alarmId=Integer.parseInt(alarm_hashMap.get(Alarm.KEY_ID));
        Alarm alarm=new Alarm();
        alarm = info.getAlarmById(alarmId);
        holder.showTime.setText(alarm.time);
        holder.showContent.setText(alarm.content);
        //holder.alarmIcon.findViewById(R.drawable.cloudy);


    }

    @Override
    public int getItemCount() {

        info = new AlarmInfo(context.getActivity());
        alarmList =  info.getAlarmList();
        return alarmList.size();
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
        builder.setTitle("删除闹钟");
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



    public class AlarmItemHolder extends ViewHolder {
        TextView showTime;
        TextView showContent;
        ImageView alarmIcon;

        public AlarmItemHolder(View itemView) {
            super(itemView);
            showTime = (TextView) itemView.findViewById(R.id.time);
            showContent = (TextView) itemView.findViewById(R.id.content);
            alarmIcon=(ImageView)itemView.findViewById(R.id.alarm_icon);

            itemView.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlarmDetail(getPosition());
                }
            });

            itemView.findViewById(R.id.container).setOnLongClickListener(new View.OnLongClickListener() {
                      @Override
                      public boolean onLongClick(View v) {
                          deleteAlarm(getPosition());
                          return true;
                      }
            });
        }
    }
}