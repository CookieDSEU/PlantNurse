package com.kot32.ksimplelibrary.widgets.drawer;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kot32.ksimplelibrary.R;
import com.kot32.ksimplelibrary.util.tools.DisplayUtil;
import com.kot32.ksimplelibrary.widgets.drawer.component.DrawerComponent;
import com.kot32.ksimplelibrary.widgets.view.UniversalTapLayout;

/**
 * Created by kot32 on 15/11/23.
 * <p/>
 * 该类提供两种构造自定义侧滑菜单的做法
 * 1.使用自带的addItem 方式
 * 2.使用自定义的侧滑菜单布局（1.继承KDrawerContentLayout 实现，2.直接传入一个LayoutID 实现，推荐继承实现方式）
 */
public class KDrawerBuilder {

    private DrawerLayout mDrawer;
    private ActionBarActivity mActivity;
    private DrawerAction drawerAction;

    private Toolbar mToolBar;
    //DrawerLayout 的菜单内容
    private ViewGroup mContentView;

    private int mDefaultWidth;

    private int mItemDefaultHeight;

    private boolean isExistedDrawer = false;


    /**
     * @param activity
     */
    public KDrawerBuilder(ActionBarActivity activity) {
        mDrawer = new DrawerLayout(activity);
        mDrawer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mActivity = activity;

        mDefaultWidth = DisplayUtil.dip2px(activity, 100);
        mItemDefaultHeight = DisplayUtil.dip2px(activity, 55);

        mContentView = new LinearLayout(activity);
        mContentView.setLayoutParams(new ViewGroup.LayoutParams(mDefaultWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setBackgroundColor(Color.WHITE);
        ((LinearLayout) mContentView).setOrientation(LinearLayout.VERTICAL);
    }

    /**
     * 与自定义的布局xml 文件一起构造
     *
     * @param layoutID
     * @return
     */
    public KDrawerBuilder withLayoutID(final int layoutID, final InitWithLayoutIDAction action) {

        mContentView = new KDrawerContentLayout(mActivity) {
            @Override
            public void initDrawerController() {
                action.initDrawerController();
            }

            @Override
            public int getLayoutID() {
                return layoutID;
            }

            @Override
            public void initViews(ViewGroup contentView) {
                action.initViews(contentView);
            }

            @Override
            public void updateViewOnDrawerOpened() {
                action.updateViewOnDrawerOpened();
            }

            @Override
            public void onDrawerClosed() {
                action.onDrawerClosed();
            }
        };
        return this;
    }

    public interface InitWithLayoutIDAction {
        /**
         * 控制逻辑
         */
        void initDrawerController();

        /**
         * 初始化侧滑菜单的子View
         */
        void initViews(ViewGroup contentView);

        /**
         * 菜单划出后的界面更新
         */
        void updateViewOnDrawerOpened();

        /**
         * 菜单关闭后的操作
         */
        void onDrawerClosed();
    }


    /**
     * 与自定义的ViewGroup 一起构造
     */

    public KDrawerBuilder withCustomContentView(ViewGroup content) {
        mContentView = content;
        if (mContentView instanceof KDrawerContentLayout) {
            ((KDrawerContentLayout) mContentView).attachToDrawer(mDrawer);
        }
        return this;
    }

    /**
     * 与ToolBar 一起构造，形成汉堡菜单和返回键转换的特效
     *
     * @param toolBar
     * @return
     */
    public KDrawerBuilder withToolBar(Toolbar toolBar) {

        if (toolBar == null) return this;
        mToolBar = toolBar;
        if (mActivity == null) {
            Log.e("警告", "KDrawerBuilder中没有设定Drawer所属的Activity，绑定 ToolBar失败");
            return this;
        }

        mActivity.setSupportActionBar(toolBar);
        mActivity.getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawer, toolBar, R.string.kdrawer_open, R.string.kdrawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerAction != null) {
                    drawerAction.onDrawerOpened(drawerView);
                }

                if (mContentView != null && (mContentView instanceof KDrawerContentLayout)) {
                    ((KDrawerContentLayout) mContentView).updateViewOnDrawerOpened();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerAction != null) {
                    drawerAction.onDrawerClosed(drawerView);
                }
                if (mContentView != null && (mContentView instanceof KDrawerContentLayout)) {
                    ((KDrawerContentLayout) mContentView).onDrawerClosed();
                }
            }
        };
        mDrawerToggle.syncState();
        mDrawer.setDrawerListener(mDrawerToggle);

        return this;
    }

    public KDrawerBuilder withDrawerAction(DrawerAction drawerAction) {
        this.drawerAction = drawerAction;
        return this;
    }

    public KDrawerBuilder withWidth(int widthDp) {
        this.mDefaultWidth = DisplayUtil.dip2px(mActivity, widthDp);
        return this;
    }


    public interface DrawerAction {

        void onDrawerOpened(View kDrawerView);

        void onDrawerClosed(View kDrawerView);
    }


    public KDrawerBuilder withExistedDrawer(DrawerLayout drawer) {
        mDrawer = drawer;
        isExistedDrawer = true;
        return this;
    }


    public DrawerLayout build() {

        if (isExistedDrawer) {
            return mDrawer;
        }
        ViewGroup baseFrameLayout = (ViewGroup) mActivity.findViewById(Window.ID_ANDROID_CONTENT);
        View activityContentView = baseFrameLayout.getChildAt(0);
        baseFrameLayout.removeViewAt(0);

        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(mDefaultWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT;
        mContentView.setLayoutParams(params);
        mContentView.setClickable(true);

        mDrawer.addView(activityContentView, 0);//内容
        mDrawer.addView(mContentView, 1);//菜单内容

        baseFrameLayout.addView(mDrawer, 0);

        return mDrawer;
    }

    /**
     * 添加一个头部组件
     *
     * @param header
     * @param layoutParams
     * @return
     */
    public KDrawerBuilder addDrawerHeader(@NonNull DrawerComponent.DrawerHeader header, @Nullable LinearLayout.LayoutParams layoutParams) {
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mActivity, 170));
        }
        mContentView.addView(header, 0, layoutParams);
        return this;
    }

    /**
     * 添加一个子项目标题
     *
     * @param titleText
     * @param textColor
     * @return
     */
    public KDrawerBuilder addDrawerSectionTitle(String titleText, int textColor) {

        TextView title = new TextView(mActivity);
        title.setText(titleText);
        title.setTextColor(textColor);
        title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        title.getPaint().setFakeBoldText(true);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemDefaultHeight / 3.5f);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemDefaultHeight);
        titleParams.setMargins(50, 0, 0, 0);

        mContentView.addView(title, titleParams);

        return this;
    }

    /**
     * 添加一个子项目
     * 每一个子项目由四个部分组成
     * 1.图标
     * 2.文字
     * 3.自定义 Wight（可以是信标，或是开关等）
     * 4.点击事件监听器
     */
    public KDrawerBuilder addDrawerSubItem(int iconID, String title, @Nullable View customView, View.OnClickListener onClickListener) {
        addDrawerSubItem("res://" + mActivity.getPackageName() + "/" + iconID, title, customView, onClickListener);
        return this;
    }

    public KDrawerBuilder addDrawerSubItem(String iconPath, String title, @Nullable View customView, View.OnClickListener onClickListener) {
        addDrawerSubItem(iconPath, title, Color.DKGRAY, customView, onClickListener);
        return this;
    }

    public KDrawerBuilder addDrawerSubItem(String iconPath, String title, int titleColor, @Nullable View customView, View.OnClickListener onClickListener) {

        FrameLayout.LayoutParams customViewParams = new FrameLayout.LayoutParams(mItemDefaultHeight / 2, mItemDefaultHeight / 2);
        customViewParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        customViewParams.setMargins(0, 0, 100, 0);
        addDrawerSubItem(iconPath, title, titleColor, customView, customViewParams, onClickListener);
        return this;
    }

    public KDrawerBuilder addDrawerSubItem(@Nullable String iconPath, String title, int titleColor, @Nullable View customView, FrameLayout.LayoutParams customViewLayoutParams, final View.OnClickListener onClickListener) {
        FrameLayout container = new UniversalTapLayout(mActivity);
        container.setBackgroundColor(Color.WHITE);

        if (!TextUtils.isEmpty(iconPath)) {
            SimpleDraweeView icon = new SimpleDraweeView(mActivity);
            icon.setImageURI(Uri.parse(iconPath));
            FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(mItemDefaultHeight / 2, mItemDefaultHeight / 2);
            iconParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            iconParams.setMargins(50, 0, 0, 0);
            container.addView(icon, iconParams);
        }

        TextView titleTextView = new TextView(mActivity);
        titleTextView.setText(title);
        titleTextView.setTextColor(titleColor);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemDefaultHeight / 3.8f);
        titleTextView.getPaint().setFakeBoldText(true);
        FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        if (!TextUtils.isEmpty(iconPath)) {
            titleParams.setMargins(200, 0, 0, 0);
        } else {
            titleParams.setMargins(50, 0, 0, 0);
        }
        container.addView(titleTextView, titleParams);

        if (customView != null) {
            container.addView(customView, customViewLayoutParams);
        }
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawers();
                if (onClickListener != null)
                    onClickListener.onClick(v);
            }
        });

        mContentView.addView(container, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemDefaultHeight));
        return this;
    }

    public KDrawerBuilder addDrawerDivider(int color) {
        View divider = new View(mActivity);
        divider.setBackgroundColor(color);
        mContentView.addView(divider, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mActivity, 1)));
        return this;
    }


}
