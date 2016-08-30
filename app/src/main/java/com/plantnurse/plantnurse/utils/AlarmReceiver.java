package com.plantnurse.plantnurse.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.plantnurse.plantnurse.utils.AlarmTip;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        int alarm_Id=intent.getIntExtra("alarm_Id",0);

        Intent i = new Intent(context, AlarmTip.class);
        i.putExtra("alarm_Id",alarm_Id);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }
}

