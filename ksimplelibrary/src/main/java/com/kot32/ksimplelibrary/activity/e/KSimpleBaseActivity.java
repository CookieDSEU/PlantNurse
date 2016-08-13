package com.kot32.ksimplelibrary.activity.e;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kot32.ksimplelibrary.KSimpleApplication;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.util.tools.DisplayUtil;
import com.kot32.ksimplelibrary.widgets.view.KLoadingView;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by kot32 on 15/11/3.
 */
public abstract class KSimpleBaseActivity extends ActionBarActivity {


    private IBaseAction baseAction;
    private View contentView;
    private KLoadingView loadingView;

    private ACache cache;
    private AtomicBoolean isSetContentView = new AtomicBoolean(false);

    private SimpleTask initTask;

    private RelativeLayout loadingContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseAction = getIBaseActivityAction();
        loadingView = new KLoadingView(this);
        if (baseAction == null) {
            Log.e("警告", "未设置Activity行为");
            return;
        }
        init();
    }

    private void init() {

        initTask = new SimpleTask(getTaskTag()) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingContent = new RelativeLayout(KSimpleBaseActivity.this);
                loadingContent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(KSimpleBaseActivity.this, 60), DisplayUtil.dip2px(KSimpleBaseActivity.this, 60));
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                loadingView.setLayoutParams(layoutParams);
                loadingContent.addView(loadingView);
                setContentView(loadingContent);
                loadingView.start();

            }

            @Override
            protected Object doInBackground(Object[] params) {

                cache = ACache.get(getApplicationContext());

                int t = baseAction.initLocalData();
                switch (t) {
                    case IBaseAction.LOAD_NETWORK_DATA_AND_SHOW:
                        baseAction.onLoadingNetworkData();
                        break;
                    case IBaseAction.LOAD_NETWORK_DATA_AND_DISMISS:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSetContentView.compareAndSet(false, true)) {
                                    showContentView();
                                }

                            }
                        });
                        baseAction.onLoadingNetworkData();
                        break;
                    case IBaseAction.DONT_LOAD_NETWORK_DATA:
                        //do nothing
                        break;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (isSetContentView.compareAndSet(false, true)) {
                    showContentView();
                }

            }
        };
        SimpleTaskManager.startNewTask(initTask);

    }

    private void showContentView() {
        if (loadingView.isStart()) {
            loadingView.stop();
            loadingContent.removeView(loadingView);
        }
        baseAction.onLoadedNetworkData(contentView);
        View tmpContentView = getContentView();
        View tmpCustomView = getCustomContentView(tmpContentView);
        if (tmpCustomView != null) {
            contentView = tmpCustomView;
        } else {
            contentView = tmpContentView;
        }
        if (contentView == null) {
            Log.e("警告", "未设置显示视图");
        } else {
            setContentView(contentView);
            baseAction.initView((ViewGroup) contentView);
        }

        baseAction.initController();
    }


    protected View getContentView() {
        if (baseAction.getContentLayoutID() == 0) {
            return null;
        }
        return getLayoutInflater().inflate(baseAction.getContentLayoutID(), null);

    }

    public abstract IBaseAction getIBaseActivityAction();

    //处理需要自定义ContentView的情况
    public abstract View getCustomContentView(View oldContentView);

    // 缓存业务数据,时间（秒）
    protected boolean cachePageData(HashMap<String, Object> dataMap, int cacheSecondsTime) {
        try {
            cache.put(getLocalClassName(), dataMap, cacheSecondsTime);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //得到缓存数据
    protected HashMap<String, Object> getCachePageData() {
        HashMap<String, Object> data = (HashMap<String, Object>) cache.getAsObject(getLocalClassName());
        if (data == null) {
            Log.e("警告", "没有拿到缓存数据，返回空值");
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleTaskManager.removeTasksWithTag(getTaskTag());
    }

    public KSimpleApplication getSimpleApplicationContext() {
        if (getApplicationContext() instanceof KSimpleApplication) {
            return (KSimpleApplication) getApplicationContext();
        } else {
            Log.e("警告", "Application 类没有继承 KSimpleApplication");
            return null;
        }
    }

    public String getTaskTag() {
        return this.getClass().getSimpleName();
    }
}
