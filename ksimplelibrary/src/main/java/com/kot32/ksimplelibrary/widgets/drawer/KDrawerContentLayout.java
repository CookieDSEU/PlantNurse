package com.kot32.ksimplelibrary.widgets.drawer;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kot32.ksimplelibrary.widgets.base.KBaseWidgets;

/**
 * Created by kot32 on 15/11/23.
 */
public abstract class KDrawerContentLayout extends KBaseWidgets {

    private View contentView;

    private DrawerLayout mDrawerLayout;

    public KDrawerContentLayout(Context context) {
        super(context);
    }

    public KDrawerContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        if (getLayoutID() == 0) {
            contentView = new LinearLayout(mContext);
        } else {
            contentView = LayoutInflater.from(mContext).inflate(getLayoutID(), this, true);
        }

        initViews((ViewGroup) contentView);
    }

    @Override
    public void initController() {
        initDrawerController();
    }

    /**
     * 控制逻辑
     */

    public abstract void initDrawerController();

    /**
     * 侧滑菜单的LayoutID
     *
     * @return
     */
    public abstract int getLayoutID();

    /**
     * 初始化侧滑菜单的子View
     */
    public abstract void initViews(ViewGroup contentView);

    /**
     * 菜单划出后的界面更新
     */
    public abstract void updateViewOnDrawerOpened();

    /**
     * 菜单关闭后的操作
     */
    public abstract void onDrawerClosed();

    public void attachToDrawer(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }


}
