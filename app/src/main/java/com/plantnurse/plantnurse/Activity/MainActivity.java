package com.plantnurse.plantnurse.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.KTabActivity;
import com.kot32.ksimplelibrary.cache.ACache;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.widgets.drawer.KDrawerBuilder;
import com.kot32.ksimplelibrary.widgets.drawer.component.DrawerComponent;
import com.kot32.ksimplelibrary.widgets.view.KTabBar;
import com.plantnurse.plantnurse.Fragment.ParkFragment;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DoubleClickExit;
import com.plantnurse.plantnurse.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends KTabActivity implements IBaseAction{
    private List<Fragment> fragmentList=new ArrayList<>();
    private android.support.v7.widget.Toolbar toolbar;
    private DrawerLayout drawer;
    private ACache mCache;
    private String checkLogin;
    @Override
    public List<Fragment> getFragmentList() {
        fragmentList.add(new ParkFragment());
        fragmentList.add(new  ParkFragment());
        fragmentList.add(new  ParkFragment());
        return fragmentList;
    }

    @Override
    public KTabBar.TabStyle getTabStyle() {
        return KTabBar.TabStyle.STYLE_GRADUAL;
    }

    @Override
    public TabConfig getTabConfig() {
        return null;
    }

    @Override
    public int initLocalData() {

        return 0;
    }

    @Override
    public void initView(ViewGroup view) {

        mCache = ACache.get(MainActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitleTextColor(0xffffffff);
        }
        setTitle("PlantNurse");
        final DrawerComponent.DrawerHeader header = new DrawerComponent.DrawerHeader(DrawerComponent.DrawerHeader.DrawerHeaderStyle.KENBURNS,
                R.drawable.header_back,
                this);
        header.addAvatar(R.drawable.avatar, Constants.AVATAR_URL, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否登录
                mCache = ACache.get(MainActivity.this);
                checkLogin=mCache.getAsString("userName");
                if (checkLogin!=null) {

                    Toast.makeText(MainActivity.this, "已登录,查看并修改用户信息", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    startActivity(intent);
                    if (drawer != null) {
                        drawer.closeDrawers();
                    }
                }
            }
        });
        header.addNickName("未登录");
        header.addIntroduction("请点击默认头像登录");
        drawer = new KDrawerBuilder(this)
                .withToolBar(toolbar)
                .withWidth(300)
                .addDrawerHeader(header, null)
                .addDrawerSectionTitle("菜单", Color.DKGRAY)
                .addDrawerSubItem(null, "注销", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCache = ACache.get(MainActivity.this);
                        checkLogin = mCache.getAsString("userName");
                        if (checkLogin == null) {
                            Toast.makeText(MainActivity.this, "你还没有登录", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        getSimpleApplicationContext().logout();

                        mCache.remove("userName");
                        mCache.remove("token");
                        checkLogin = mCache.getAsString("userName");

                        if (checkLogin == null) {
                            Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                            /*刷新界面*/
                            finish();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivityForResult(intent, 1);
                        }
                    }
                })
                .addDrawerSubItem(null, "注册", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断当前是否登录
                        mCache = ACache.get(MainActivity.this);
                        checkLogin=mCache.getAsString("userName");
                        if (checkLogin!=null) {

                            Toast.makeText(MainActivity.this, "你已经注册过了！", Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                            startActivityForResult(intent, 1);
                            if (drawer != null) {
                                drawer.closeDrawers();
                            }
                        }
                    }
                })
                .addDrawerDivider(Color.parseColor("#f1f2f1"))
                .addDrawerSubItem("", "关于本软件", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    }
                })
                .addDrawerSubItem("", "检查更新", null, null)
                .withDrawerAction(new KDrawerBuilder.DrawerAction() {
                    @Override
                    public void onDrawerOpened(View kDrawerView) {
                        //打开了侧滑菜单
                        mCache = ACache.get(MainActivity.this);
                        checkLogin=mCache.getAsString("userName");
                        if (checkLogin!=null) {
                            header.changeNickName(checkLogin);
                            header.changeAvatorURL(Constants.AVATAR_URL);
                            header.changeIntroduction("正式用户");
                        }else{
                            header.changeNickName("未登录");
                            header.changeAvatorURL(Constants.AVATAR_URL);
                            header.changeIntroduction("点击头像登录");
                        }
                    }

                    @Override
                    public void onDrawerClosed(View kDrawerView) {
                        //关闭了侧滑菜单
                    }
                })
                .build();


    }

    @Override
    public void initController() {

        addTab(R.drawable.park_grey, R.drawable.park_color, "花园", Color.GRAY, Color.parseColor("#04b00f"));
        addTab(R.drawable.clock_grey, R.drawable.clock_color, "闹钟", Color.GRAY, Color.parseColor("#04b00f"));
        addTab(R.drawable.info_grey, R.drawable.info_color, "我的", Color.GRAY, Color.parseColor("#04b00f"));
    }



    @Override
    public void onLoadingNetworkData() {
        SimpleTaskManager.startNewTask(getSimpleApplicationContext().getLoginTask());

    }

    @Override
    public void onLoadedNetworkData(View contentView) {

    }
    @Override
    public View getCustomContentView(View v) {
        ViewGroup vg = (ViewGroup) super.getCustomContentView(v);
        toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.default_toolbar, null);
        vg.addView(toolbar, 0);
        return vg;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickExit.check()) {
                ToastUtil.showShort("再按一次退出");
            } else {
                finish();
            }
        }
    }
}