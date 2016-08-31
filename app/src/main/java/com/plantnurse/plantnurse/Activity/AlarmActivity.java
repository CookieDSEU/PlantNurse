package com.plantnurse.plantnurse.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;

import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.DbHelper;
import com.plantnurse.plantnurse.utils.MyService;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmActivity extends Activity {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private Intent getIntent;

    private String mtext;//内容
    private int alarm_Id;//id
    private Alarm alarm=new Alarm();
    private AlarmInfo info;
    private int isAlarm;
    //private int alarm_music;
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
        // 播放闹铃
        final Intent intentSV = new Intent(AlarmActivity.this, MyService.class);
        startService(intentSV);


        Intent intent=getIntent();
        alarm_Id=intent.getIntExtra("alarm_Id", 0);
        //alarm_music=intent.getIntExtra("soundOrVibrator",0);
        info=new AlarmInfo(this);

        alarm = info.getAlarmById(alarm_Id);
        mtext=alarm.content;

        //alarm.time=null;
        isAlarm=0;//响过后设置为0，即不再是个闹钟


        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        new AlertDialog.Builder(AlarmActivity.this).setTitle("闹钟").setMessage(mtext)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService(intentSV);
                        AlarmActivity.this.finish();
                        info.update(alarm);
                    }
                }).show();

    }

}

