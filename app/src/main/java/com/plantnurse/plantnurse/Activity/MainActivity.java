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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.t.KTabActivity;
import com.kot32.ksimplelibrary.manager.preference.PreferenceManager;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.kot32.ksimplelibrary.widgets.drawer.KDrawerBuilder;
import com.kot32.ksimplelibrary.widgets.drawer.component.DrawerComponent;
import com.kot32.ksimplelibrary.widgets.view.KTabBar;
import com.plantnurse.plantnurse.Fragment.AlarmFragment;
import com.plantnurse.plantnurse.Fragment.MyFragment;
import com.plantnurse.plantnurse.Fragment.ParkFragment;
import com.plantnurse.plantnurse.Fragment.BookFragment;
import com.plantnurse.plantnurse.Network.GetIndexResponse;
import com.plantnurse.plantnurse.Network.GetMyPlantResponse;
import com.plantnurse.plantnurse.Network.GetMyStarResponse;
import com.plantnurse.plantnurse.Network.WeatherAPI;
import com.plantnurse.plantnurse.Network.WeatherResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.DoubleClickExit;
import com.plantnurse.plantnurse.utils.ToastUtil;
import com.plantnurse.plantnurse.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends KTabActivity implements IBaseAction {
    private List<Fragment> fragmentList = new ArrayList<>();
    private Toolbar toolbar;
    private DrawerLayout drawer;
    public static final int REQUEST_CODE = 0x04;
    private DrawerComponent.DrawerHeader header;
    ParkFragment pf;

    @Override
    public List<Fragment> getFragmentList() {
        pf=new ParkFragment();
        fragmentList.add(pf);
        fragmentList.add(new BookFragment());
        fragmentList.add(new AlarmFragment());
        fragmentList.add(new MyFragment());
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitleTextColor(0xffffffff);
        }
        setTitle("PlantNurse");
        header = new DrawerComponent.DrawerHeader(DrawerComponent.DrawerHeader.DrawerHeaderStyle.KENBURNS,
               R.drawable.header_back,
               this);
        header.addAvatar(R.drawable.avatar, Constants.AVATAR_URL, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否登录
                if (getSimpleApplicationContext().isLogined()) {
                    //关闭drawer
                    drawer.closeDrawer(GravityCompat.START);
                    //跳转到“我的”fragement
                    getContainer().setCurrentItem(3);

                } else {
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
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
                .withWidth(250)
                .addDrawerHeader(header, null)
                .addDrawerSectionTitle("菜单", Color.parseColor("#2F4F4F"))
                .addDrawerSubItem(R.drawable.singin, "登录", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getSimpleApplicationContext().isLogined()) {
                            ToastUtil.showShort("您已经登录");
                            return;
                        }


                        if (!getSimpleApplicationContext().isLogined()) {

                            /*刷新界面*/
                            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);

                        }
                    }
                })

                .addDrawerSubItem(R.drawable.register, "注册", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断当前是否登录
                        if (getSimpleApplicationContext().isLogined()) {

                            ToastUtil.showShort("你已经登录！");

                        } else {

                            RegisterPage registerPage = new RegisterPage();
                            registerPage.setRegisterCallback(new EventHandler() {
                                public void afterEvent(int event, int result, Object data) {
                                    // 解析注册结果
                                    if (result == SMSSDK.RESULT_COMPLETE) {
                                        @SuppressWarnings("unchecked")
                                        HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                                        String phone = (String) phoneMap.get("phone");
                                        // 提交用户信息
                                        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                                        intent.putExtra("phone",phone);
                                        startActivityForResult(intent, REQUEST_CODE);
                                    }
                                }
                            });
                            registerPage.show(MainActivity.this);
                            if (drawer != null) {
                                drawer.closeDrawers();
                            }
                        }
                    }
                })
                .addDrawerSubItem(R.drawable.logoff, "注销", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!getSimpleApplicationContext().isLogined()) {
                            ToastUtil.showShort("你还没有登录");
                            return;
                        }
                        getSimpleApplicationContext().logout();


                        if (!getSimpleApplicationContext().isLogined()) {
                            ToastUtil.showShort("注销成功");
                            /*刷新界面*/
                            finish();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                })

                .addDrawerDivider(Color.parseColor("#EEE9E9"))
                .addDrawerSubItem(R.drawable.about, "关于本软件", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    }
                })
                .addDrawerSubItem(R.drawable.update, "检查更新", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.checkVersion(MainActivity.this);
                    }
                })
                .withDrawerAction(new KDrawerBuilder.DrawerAction() {
                    @Override
                    public void onDrawerOpened(View kDrawerView) {
                        //打开了侧滑菜单
                        if (getSimpleApplicationContext().isLogined()) {
                            if(DataManager.isAvatarChanged_drawer){
                                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                                imagePipeline.clearCaches();
                                DataManager.isAvatarChanged_drawer =false;
                            }
                            UserInfo userInfo = (UserInfo) PreferenceManager.getLocalUserModel();
                            header.changeNickName(userInfo.getuserName());
                            String temp = Constants.AVATAR_URL + "?id=" + userInfo.getuserName();
                            header.changeAvatorURL(temp);
                            header.changeIntroduction("正式用户");
                        } else {
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
                .addDrawerDivider(Color.parseColor("#EEE9E9"))
                .build();


    }

    @Override
    public void initController() {
         addTab(R.drawable.park_grey, R.drawable.park_color, "花园", Color.GRAY, Color.parseColor("#04b00f"));
         addTab(R.drawable.book_grey, R.drawable.book_color, "百科", Color.GRAY, Color.parseColor("#04b00f"));
         addTab(R.drawable.clock_grey, R.drawable.clock_color, "闹钟", Color.GRAY, Color.parseColor("#04b00f"));
         addTab(R.drawable.info_grey, R.drawable.info_color, "我的", Color.GRAY, Color.parseColor("#04b00f"));
    }


    @Override
    public void onLoadingNetworkData() {
        getWeatherInfo();
        getPlantIndex();
        getMyPlant();
        getMyStar();
    }

    private void getMyStar() {
        HashMap param=new HashMap<>();
        if(getSimpleApplicationContext().isLogined()) {
            UserInfo ui = (UserInfo) getSimpleApplicationContext().getUserModel();
            param.put("userName", ui.getuserName());
        }
        else{
            param.put("userName","blank");
        }
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(),MainActivity.this, GetMyStarResponse.class,param,Constants.GETMYSTAR_URL,NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetMyStarResponse response = (GetMyStarResponse) result.resultObject;
                DataManager.setMyStar(response);
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }

    private void getMyPlant() {
        HashMap param=new HashMap<>();
        if(getSimpleApplicationContext().isLogined()) {
            UserInfo ui = (UserInfo) getSimpleApplicationContext().getUserModel();
            param.put("userName", ui.getuserName());
        }
        else {
            param.put("userName", "blank");
        }
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(),getSimpleApplicationContext(), GetMyPlantResponse.class,
                param,Constants.GETMYPLANT_URL,NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetMyPlantResponse response = (GetMyPlantResponse) result.resultObject;
                DataManager.setMyPlant(response);
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });
    }

    private void getPlantIndex() {
        HashMap temp=new HashMap<>();
        SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getSimpleApplicationContext(),GetIndexResponse.class,
                temp, Constants.GETINDEX_URL, NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                GetIndexResponse response = (GetIndexResponse) result.resultObject;
                DataManager.setPlantIndex(response);
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {
            }
        });
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

    @Override
    protected void onResume() {
        getMyPlant();
        getMyStar();
        if(DataManager.isCityChanged){
            getWeatherInfo();
            DataManager.isCityChanged=false;
        }
        super.onResume();
    }

    public void getWeatherInfo() {
        HashMap<String, String> params = new HashMap<String, String>();
        if (getSimpleApplicationContext().isLogined()) {
            UserInfo userInfo = (UserInfo) getSimpleApplicationContext().getUserModel();
            params.put("key", Constants.WEATHER_KEY);
            params.put("city", userInfo.getcity());
        } else {
            params.put("key", Constants.WEATHER_KEY);
            params.put("city", "北京");

        }

        SimpleTaskManager.startNewTask(new NetworkTask(
                getTaskTag(),
                getSimpleApplicationContext(),
                WeatherAPI.class,
                params,
                Constants.WEATHER_API_URL,
                NetworkTask.GET) {
            @Override
            public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                WeatherAPI weatherAPI = (WeatherAPI) result.resultObject;
                WeatherResponse weatherInfo = weatherAPI.response.get(0);
                DataManager.setWeatherInfo(weatherInfo);

            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                DataManager.isMyPlantChanged=true;
                DataManager.isCityChanged=true;
                pf.onPageSelected();
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
