package com.plantnurse.plantnurse.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.DbHelper;
import com.plantnurse.plantnurse.utils.MyService;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmActivity extends Activity {

    private String mtext;//内容
    private int alarm_Id;//id
    private int frequency;
    private Alarm alarm=new Alarm();
    private AlarmInfo info;
    private String alarm_music;
    //private String alarm_plantName;
    //private int weather;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //下面是为了在锁屏时也能启动闹铃
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Intent intent=getIntent();
        alarm_Id=intent.getIntExtra("alarm_Id", 0);
        frequency = intent.getIntExtra("frequency",0);
        //alarm_music=intent.getIntExtra("soundOrVibrator",0);
        info=new AlarmInfo(this);

        alarm = info.getAlarmById(alarm_Id);
        mtext=alarm.content;
        Log.e("text",mtext);
        alarm_music=alarm.music;

        // 播放闹铃
        final Intent intentSV = new Intent(this, MyService.class);
        if(alarm_music!=null){
            intentSV.putExtra("music",alarm_music);
        }
        startService(intentSV);

        if(frequency==0||frequency==4){//一次性闹钟
            alarm.isAlarm=0;//响过后设置为0，即不再是个闹钟
        }

        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        SweetAlertDialog dialog=new SweetAlertDialog(AlarmActivity.this,SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("闹钟");
        dialog.setContentText(mtext);
        dialog.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                stopService(intentSV);
                info.update(alarm);
                sweetAlertDialog.dismissWithAnimation();
                AlarmActivity.this.finish();
            }
        });
        dialog.show();
    }
}

