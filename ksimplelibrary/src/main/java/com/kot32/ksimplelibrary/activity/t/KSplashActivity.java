package com.kot32.ksimplelibrary.activity.t;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.util.tools.DisplayUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by kot32 on 15/10/18.
 * 继承该类用于开发刚进入APP后的闪屏页面
 */
public abstract class KSplashActivity extends KSimpleBaseActivityImpl {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //播放动画
        ImageView imageView = new ImageView(this);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams picLayout = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(this, 200), DisplayUtil.dip2px(this, 100));
        picLayout.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imageView.setLayoutParams(picLayout);
        relativeLayout.addView(imageView);
        setContentView(relativeLayout);

        imageView.setImageResource(getLogoImageResource());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(imageView, "alpha", new FloatEvaluator(), 0f, 1f);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(KSplashActivity.this, getNextActivityClass());
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                SimpleTaskManager.startNewTask(new SimpleTask(getTaskTag()) {

                    @Override
                    protected Object doInBackground(Object[] params) {
                        init();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                    }
                });
            }
        });

        objectAnimator.start();
           /* create by Heloise
            在进入程序之前，先判断是否联网
            为联网则弹出AlertDialog提示尚未联网
            程序退出
           */
        if(getLocalIpAddress()==null){
            new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("糟糕，没有网了！")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            System.exit(0);
                        }
                    })
                    .show();
//            AlertDialog waringDialog=new AlertDialog.Builder(this).setTitle
//                    ("Warning").setMessage("糟糕，没有网了！").setNegativeButton
//                    (R.string.kdrawer_close, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            System.exit(0);
//                        }
//                    }).create();
//            waringDialog.show();
        }
    }
    //判断是否联网，ip地址为空则尚未联网
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("网络未连接",ex.toString());
        }
        return null;
    }

    abstract public void init();

    abstract public int getLogoImageResource();

    abstract public Class getNextActivityClass();
}
