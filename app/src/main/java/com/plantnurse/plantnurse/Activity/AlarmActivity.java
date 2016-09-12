package com.plantnurse.plantnurse.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.plantnurse.plantnurse.model.Alarm;
import com.plantnurse.plantnurse.utils.AlarmInfo;
import com.plantnurse.plantnurse.utils.MyService;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmActivity extends Activity {

    private String mtext = "";//内容
    private String name = "";//植物名字
    private String action = "";//行为
    private int alarm_Id;//id
    private int frequency;
    private Alarm alarm = new Alarm();
    private AlarmInfo info;
    private String alarm_music;

    //提取行为
    public void getAction(int i, String a) {
        if (i == 1) {
            action += a + "、";
        }
    }

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

        //获取音量
        final AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        //得到手机音乐音量的最大值
        final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //得到当前手机的音量
        final int mCurVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mCurVolume == 0) {
            //设置音量大小
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
        }

        Intent intent = getIntent();
        alarm_Id = intent.getIntExtra("alarm_Id", 0);
        frequency = intent.getIntExtra("frequency", 0);
        info = new AlarmInfo(this);

        alarm = info.getAlarmById(alarm_Id);
        mtext = alarm.content;
        name = alarm.available;
        alarm_music = alarm.music;
        getAction(alarm.water, "浇水");
        getAction(alarm.sun, "晒太阳");
        getAction(alarm.takeBack, "收回");
        getAction(alarm.takeCare, "打理");
        getAction(alarm.fertilization, "施肥");

        // 播放闹铃
        final Intent intentSV = new Intent(this, MyService.class);
        if (alarm_music != null) {
            intentSV.putExtra("music", alarm_music);
        }
        startService(intentSV);

        if (frequency == 0 || frequency == 4) {//一次性闹钟
            alarm.isAlarm = 0;//响过后设置为0，即不再是个闹钟
        }

        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        SweetAlertDialog dialog = new SweetAlertDialog(AlarmActivity.this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("闹钟");
        if (action == "") {
            dialog.setContentText("您的" + name + "需要照顾。" + "\n\r" + "Tips:" + mtext);
        } else {
            dialog.setContentText("您的" + name + "需要" + action + "\n\r" + "Tips:" + mtext);
        }
        dialog.setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                stopService(intentSV);
                info.update(alarm);
                //设置音量大小,使其回到原来静音状态
                if (mCurVolume == 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                }
                sweetAlertDialog.dismissWithAnimation();
                AlarmActivity.this.finish();
            }
        });
        dialog.show();
    }
}

