package com.kot32.ksimplelibrary.fragment.e;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTask;
import com.kot32.ksimplelibrary.util.tools.DisplayUtil;
import com.kot32.ksimplelibrary.widgets.view.KLoadingView;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by kot32 on 15/11/4.
 */
public abstract class KSimpleBaseFragment extends android.support.v4.app.Fragment {

    private IBaseAction baseAction;

    public abstract IBaseAction getIBaseAction();

    private View               contentView;

    public static final String TAG      = "KSimpleBaseFragment";

    private AtomicBoolean      isLoaded = new AtomicBoolean(false);

    protected ACache           cache;

    private SimpleTask         initTask;

    private KLoadingView       loadingView;

    private Handler            mHandler;

    {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        baseAction.onLoadedNetworkData(contentView);
                        if (contentView != null) {
                            baseAction.initView((ViewGroup) contentView);
                        }
                        baseAction.initController();
                        break;
                }
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseAction = getIBaseAction();
        if (baseAction == null) {
            Log.e("警告", "尚未实现 IBaseAction");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tmpContentView = getContentView();
        View tmpCustomView = getCustomContentView(tmpContentView);
        if (tmpCustomView != null) {
            contentView = tmpCustomView;
        } else {
            contentView = tmpContentView;
        }
        if (contentView == null) {
            Log.e("警告", "未设置显示视图");
        }

        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // if (isLoaded.compareAndSet(false, true)) {
        if (contentView != null) {
            baseAction.initView((ViewGroup) contentView);
        }
        startInit();
        // }

    }

    protected View getContentView() {
        if (baseAction.getContentLayoutID() == 0) {
            return null;
        }
        return getActivity().getLayoutInflater().inflate(baseAction.getContentLayoutID(), null);

    }


    // 处理需要自定义ContentView的情况
    public View getCustomContentView(View oldContentView) {
        return null;
    }

    private void startInit() {

        initTask = new SimpleTask(getTaskTag()) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                ViewGroup rootView = (ViewGroup) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
                loadingView = new KLoadingView(getActivity());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(DisplayUtil.dip2px(getActivity(),
                                                                                                        60),
                                                                                     DisplayUtil.dip2px(getActivity(),
                                                                                                        60));
                layoutParams.gravity = Gravity.CENTER;
                rootView.addView(loadingView, layoutParams);
                // 关闭硬件加速避免bug
                loadingView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                loadingView.start();

            }

            @Override
            protected Object doInBackground(Object[] params) {

                cache = ACache.get(getActivity());

                int t = baseAction.initLocalData();
                switch (t) {
                    case IBaseAction.LOAD_NETWORK_DATA_AND_SHOW:
                        baseAction.onLoadingNetworkData();
                        break;
                    case IBaseAction.LOAD_NETWORK_DATA_AND_DISMISS:
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                        baseAction.onLoadingNetworkData();
                        break;
                    case IBaseAction.DONT_LOAD_NETWORK_DATA:
                        // do nothing
                        break;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                baseAction.onLoadedNetworkData(contentView);

                baseAction.initController();
                if (loadingView != null && loadingView.isStart()) {
                    loadingView.stop();
                    ViewGroup rootView = (ViewGroup) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
                    rootView.removeView(loadingView);
                }

            }
        };
        SimpleTaskManager.startNewTask(initTask);
    }

    // 缓存业务数据,时间（秒）
    protected boolean cachePageData(HashMap<String, Object> dataMap, int day) {
        try {
            cache.put(getClass().getSimpleName(), dataMap, day * ACache.TIME_DAY);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 得到缓存数据
    protected HashMap<String, Object> getCachePageData() {
        HashMap<String, Object> data = (HashMap<String, Object>) cache.getAsObject(getClass().getSimpleName());

        if (data == null) {
            Log.e("警告", "没有拿到缓存数据，返回空值");
            return new HashMap<>();
        }
        return data;
    }

    public String getTaskTag() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SimpleTaskManager.removeTasksWithTag(getTaskTag());
        if (loadingView != null && loadingView.isStart()) {
            loadingView.stop();
            ViewGroup rootView = (ViewGroup) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(loadingView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SimpleTaskManager.removeTasksWithTag(getTaskTag());
        if (loadingView != null && loadingView.isStart()) {
            loadingView.stop();
            ViewGroup rootView = (ViewGroup) getActivity().findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(loadingView);
        }
    }
}
