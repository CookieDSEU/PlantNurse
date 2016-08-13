package com.kot32.ksimplelibrary.fragment.t;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.kot32.ksimplelibrary.widgets.view.KRefreshView;

/**
 * Created by kot32 on 15/11/7.
 * 继承该类用以开发可以全局下拉刷新（类似京东，美团）的 Fragment
 */
public abstract class KRefreshFragment extends KSimpleBaseFragmentImpl implements KRefreshView.IRefreshAction {


    private KRefreshView refreshView;

    /**
     * 复写该方法，返回自定义的头部 RefreshView
     */
    public View customHeaderView() {
        return null;
    }

    /**
     * 复写该方法，自定义RefreshView 的一些属性
     *
     * @return
     */
    public void customRefreshView(KRefreshView refreshView) {
    }


    public abstract View onRefresh();

    public abstract View onRefreshComplete();

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void refreshComplete() {
        onRefreshComplete();
    }

    public KRefreshView getRefreshView() {
        return refreshView;
    }

    @Override
    public View getCustomContentView(View oldView) {

        refreshView = new KRefreshView(getActivity());
        refreshView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.weight = 1;
        oldView.setLayoutParams(layoutParams);
        refreshView.addView(oldView, 1);

        refreshView.setiRefreshAction(this);
        refreshView.setHeaderHeight(100);//dp
        refreshView.setMAX_LIMIT_SLOT(50);

        if (customHeaderView() != null)
            refreshView.setHeaderView(customHeaderView(), null);
        customRefreshView(refreshView);

        return refreshView;
    }

    public void initLoadMoreFunc() {
        if (refreshView != null) {
            refreshView.initLoadMoreFunc();
            refreshView.setLoadMoreConfig(getLoadMoreConfig());
        }
    }

    public KRefreshView.LoadMoreConfig getLoadMoreConfig() {
        return new KRefreshView.LoadMoreConfig(false, null, null, null, 0);
    }
}
