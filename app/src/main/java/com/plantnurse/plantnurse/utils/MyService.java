package com.plantnurse.plantnurse.utils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import com.plantnurse.plantnurse.R;

import java.io.IOException;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class MyService extends Service {
    private MediaPlayer mp;
    private String path;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        // 初始化音乐资源
        try {
            mp = new MediaPlayer();// 创建MediaPlayer对象
            path = intent.getStringExtra("music");
            if (path.equals("陈奕迅-稳稳的幸福（默认）")) {//没有设置铃声则播放系统自定义铃声
                mp = MediaPlayer.create(this, R.raw.music);
            } else {
                mp.setDataSource(this, Uri.parse(path));
                mp.prepare();//缓冲
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 开始播放音乐
        mp.start();
        // 音乐播放完毕的事件处理
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                // 循环播放
                try {
                    mp.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        // 播放音乐时发生错误的事件处理
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                // 释放资源
                try {
                    mp.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 服务停止时停止播放音乐并释放资源
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}

