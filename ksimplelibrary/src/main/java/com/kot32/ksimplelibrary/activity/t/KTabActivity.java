package com.kot32.ksimplelibrary.activity.t;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.i.ITabPageAction;
import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
import com.kot32.ksimplelibrary.util.tools.ViewUtil;
import com.kot32.ksimplelibrary.widgets.view.KNoScrollViewPager;
import com.kot32.ksimplelibrary.widgets.view.KTabBar;

import java.util.List;

/**
 * Created by kot32 on 15/11/15.
 */
public abstract class KTabActivity extends KSimpleBaseActivityImpl implements IBaseAction {

    private LinearLayout       content;

    private KNoScrollViewPager container;

    private KTabBar            tabBar;

    private TabAdapter         tabAdapter;

    private List<Fragment>     fragmentList;

    private TabConfig          config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public KNoScrollViewPager getContainer(){
        return container;
    }
    private void init() {

        fragmentList = getFragmentList();
        if (fragmentList == null || fragmentList.size() == 0) {
            Log.e("警告", "未得到要显示的Fragment 列表");
            return;
        }
        initdata();
        initview();
        initcontroller();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    private void initdata() {
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        if (getTabConfig() == null) {
            config = new TabConfig(KTabBar.TabStyle.STYLE_GRADUAL, 9.5f, 1f, 0.45f, 0.45f, 0.12f, 12);
        } else {
            config = getTabConfig();
        }
    }

    private void initview() {
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                           ViewGroup.LayoutParams.MATCH_PARENT));

        container = new KNoScrollViewPager(this);
        container.setId(ViewUtil.generateViewId());
        LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        containerParam.weight = config.contentWeight;
        content.addView(container, containerParam);

        View divider = new View(this);
        divider.setBackgroundColor(Color.LTGRAY);
        divider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        content.addView(divider);

        tabBar = new KTabBar(this, config.style);
        LinearLayout.LayoutParams tabParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        tabParam.weight = config.tabBarWeight;
        content.addView(tabBar, tabParam);
    }

    private void initcontroller() {
        container.setAdapter(tabAdapter);
        container.setOffscreenPageLimit(0);
        container.setOnPageChangeListener(tabAdapter);

        tabBar.setOnTabClickListener(new KTabBar.OnTabClickListener() {

            @Override
            public void onClick(int index) {
                if (config.style == KTabBar.TabStyle.STYLE_NORMAL) {
                    container.setCurrentItem(index, false);
                } else if (config.style == KTabBar.TabStyle.STYLE_GRADUAL) {
                    container.setCurrentItem(index, false);
                }
            }
        });

        // 如果不是渐变模式，不允许KNoScrollViewPager滑动
        if (config.style == KTabBar.TabStyle.STYLE_NORMAL) {
            container.setNoScroll(true);
        }

    }

    public void addTab(int iconImgId, int highlightImgId, String text, int fontColor, int highlightFontColor) {
        Bitmap iconImgBitmap = BitmapFactory.decodeResource(getResources(), iconImgId);
        Bitmap highlightImgBitmap = BitmapFactory.decodeResource(getResources(), highlightImgId);

        addTab(iconImgBitmap, highlightImgBitmap, text, fontColor, highlightFontColor);
    }

    public void addTab(Bitmap iconImgBitmap, Bitmap highlightImgBitmap, String text, int fontColor,
                       int highlightFontColor) {

        if (fragmentList == null || fragmentList.size() == 0) {
            Log.e("警告", "未得到要显示的Fragment 列表,无法添加 Tab");
            return;
        }
        tabBar.addTab(iconImgBitmap, highlightImgBitmap, text, fontColor, highlightFontColor, config.eachTabWidthRatio,
                      config.eachTabHeightRatio, config.eachTabMrginTopRatio, config.textSize);
        if (tabAdapter != null) {
            tabAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View getCustomContentView(View v) {
        return content;
    }

    public abstract List<Fragment> getFragmentList();

    public abstract KTabBar.TabStyle getTabStyle();

    class TabAdapter extends FragmentPagerAdapter implements KNoScrollViewPager.OnPageChangeListener {

        public TabAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public void onPageScrolled(int p, float positionOffset, int positionOffsetPixels) {

            if (positionOffset > 0) {
                if (positionOffset <= 0.02) positionOffset = 0;
                if (positionOffset >= 0.98) positionOffset = 1;

                if (tabBar.getTabView(p) instanceof KTabBar.GradualTabView) {
                    KTabBar.GradualTabView iconLeft = (KTabBar.GradualTabView) tabBar.getTabView(p);
                    KTabBar.GradualTabView iconRight = (KTabBar.GradualTabView) tabBar.getTabView(p + 1);

                    iconLeft.beDarker(positionOffset);
                    iconRight.beLighter(positionOffset);
                }

            }

        }

        @Override
        public void onPageSelected(int position) {
            if (fragmentList.get(position) instanceof ITabPageAction) {
                ((ITabPageAction) fragmentList.get(position)).onPageSelected();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public KTabBar getTabBar() {
        return tabBar;
    }

    @Override
    public int getContentLayoutID() {
        return 0;
    }

    public abstract TabConfig getTabConfig();

    public class TabConfig {

        KTabBar.TabStyle style;
        // 内容在LinearLayout 中所占据的重量
        float            contentWeight;
        // TabBar 在LinearLayout 中所占据的重量
        float            tabBarWeight;
        // 每一个Tab图标 的宽度占它的容器的比例
        float            eachTabWidthRatio;
        // 每一个Tab图标 的高度占它的容器的比例
        float            eachTabHeightRatio;
        // 每一个Tab图标 离TabBar顶部的百分比
        float            eachTabMrginTopRatio;
        // 文字描述的大小(sp)
        float            textSize;

        public TabConfig(KTabBar.TabStyle style, float contentWeight, float tabBarWeight, float eachTabWidthRatio,
                         float eachTabHeightRatio, float eachTabMrginTopRatio, float textSize){
            this.style = style;
            this.contentWeight = contentWeight;
            this.tabBarWeight = tabBarWeight;
            this.eachTabWidthRatio = eachTabWidthRatio;
            this.eachTabHeightRatio = eachTabHeightRatio;
            this.eachTabMrginTopRatio = eachTabMrginTopRatio;
            this.textSize = textSize;
        }
    }

}
