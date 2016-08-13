package com.kot32.ksimplelibrary.activity.t;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.util.tools.DisplayUtil;

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

    }


    abstract public void init();

    abstract public int getLogoImageResource();

    abstract public Class getNextActivityClass();
}
