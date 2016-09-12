package com.plantnurse.plantnurse.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.i.ITabPageAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.Activity.MainActivity;
import com.plantnurse.plantnurse.Activity.MyPlantActivity;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.Network.GetMyPlantResponse;
import com.plantnurse.plantnurse.Network.WeatherAPI;
import com.plantnurse.plantnurse.Network.WeatherResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataAnalysis;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.ListDialog;
import com.plantnurse.plantnurse.utils.PlantListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Eason_Tao on 2016/8/26.
 */

public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction, ITabPageAction {
    //view
    private LinearLayout layout_Weather;
    private TextView text_Hum;
    private TextView text_Weather;
    private TextView text_Tmp;
    private ImageView image_Weather;
    private ImageButton button_tips;
    private CircleImg image_Plant;
    private TextView text_City;
    private PlantListAdapter mAdapter;//添加植物的图片适配器
    private RecyclerView mRecyclerView;
    private FloatingActionsMenu float_menu;
    private FloatingActionButton float_Addplant;
    private FloatingActionButton float_Plantlist;
    private FloatingActionButton float_Plantlist2;
    //data
    private DataAnalysis mdataAnaylsis;
    String tips;
    private int nowPosition = 0;
    private String now_Hum;
    private String now_Weather;
    private String now_Tmp;
    private String city;
    private int now_Cond;
    private MainApplication mApp;
    private List<Map<String, Object>> mData;
    private List<String> plantList_Data;//添加植物的图片列表
    private Context mContext;
    private WeatherListAdapter adapter;


    @Override
    public int initLocalData() {
        mApp = (MainApplication) getActivity().getApplication();
        mContext = getActivity();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        //data analysis


        layout_Weather = (LinearLayout) view.findViewById(R.id.layout_weather);
        image_Weather = (ImageView) view.findViewById(R.id.image_weather);
        image_Plant = (CircleImg) view.findViewById(R.id.image_flower);
        button_tips = (ImageButton) view.findViewById(R.id.tipButton);
        //选择图片recyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_plantlist);
        text_Tmp = (TextView) view.findViewById(R.id.text_temp);
        text_City = (TextView) view.findViewById(R.id.text_city);
        text_Weather = (TextView) view.findViewById(R.id.text_weather);
        text_Hum = (TextView) view.findViewById(R.id.text_hum);

        float_menu = (FloatingActionsMenu) view.findViewById(R.id.floatingmenu_park);
        float_Addplant = (FloatingActionButton) view.findViewById(R.id.minifloat_addplant);
        float_Plantlist = (FloatingActionButton) view.findViewById(R.id.minifloat_plantlist);
        float_Plantlist2 = (FloatingActionButton) view.findViewById(R.id.minifloat_addplant2);
        //添加floating

        if (DataManager.isFirstEnterParkFragment) {
            button_tips.setBackgroundResource(R.drawable.bubblebutton_red);
        } else {
            button_tips.setBackgroundResource(R.drawable.bubblebutton);
        }

        float_Addplant.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) getActivity();
                ma.getContainer().setCurrentItem(1);
            }
        });

        float_Plantlist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mApp.isLogined()) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("请先登录!")
                            .show();
                } else {
                    Intent intent = new Intent(getActivity(), AddplantActivity.class);
                    startActivity(intent);
                }
            }
        });

        //植物列表floating
        float_Plantlist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ListDialog ld = new ListDialog(getActivity(), R.style.CalenderStyle);
                Window calender_Window = ld.getWindow();
                WindowManager.LayoutParams lp = calender_Window.getAttributes();
                calender_Window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                lp.width = 1000;
                lp.height = 1000;
                lp.y = -100;
                mData = getData();
                adapter = new WeatherListAdapter(getActivity());
                ld.getListView().setAdapter(adapter);
                ld.show();
            }
        });


        button_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_tips.setBackgroundResource(R.drawable.bubblebutton);
                DataManager.isFirstEnterParkFragment = false;
                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("小提示")
                        .setContentText(tips)
                        .setConfirmText("我知道了")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelText("设置闹钟")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                Intent intent = new Intent(getActivity(), AddAlarmActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器

    }


    //初始化plantlist
    private void initDatas() {
        DataAnalysis da;
    }

    @Override
    public void initController() {
        now_Tmp = DataManager.getWeatherInfo().now.tmp;
        now_Cond = Integer.parseInt(DataManager.getWeatherInfo().now.cond.code);
        now_Hum = DataManager.getWeatherInfo().now.hum;
        now_Weather = DataManager.getWeatherInfo().now.cond.txt;

        city = DataManager.getWeatherInfo().basic.city;
        MainActivity mainActivity = (MainActivity) getActivity();
        if (DataManager.getMyPlant().response.size() == 0) {
            image_Plant.setImageResource(R.drawable.logo2);
        } else {
            if (mainActivity.getSimpleApplicationContext().isLogined())
                Picasso.with(getActivity()).load(Constants.MYPLANTPIC_URL + plantList_Data.get(0)).into(image_Plant);
            else
                image_Plant.setImageResource(R.drawable.logo2);
        }

        image_Plant.setBorderWidth(6);
        image_Plant.setBorderColor(Color.WHITE);
        image_Plant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DataManager.getMyPlant().response.size() > 0) {
                    Intent intent = new Intent(getActivity(), MyPlantActivity.class);
                    intent.putExtra("id", nowPosition);
                    startActivity(intent); // 启动Activity
                }

            }
        });

//        button_tips.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                if()
//            }
//        });

        layout_Weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialog ld = new ListDialog(getActivity(), R.style.CalenderStyle);

                Window calender_Window = ld.getWindow();
                WindowManager.LayoutParams lp = calender_Window.getAttributes();
                calender_Window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                lp.width = 1000;
                lp.height = 1000;
                lp.y = -100;

                //---1---
//                ListAdapter adapter = new SimpleAdapter( getActivity(),getData(),
//                        R.layout.listview_weather,
//                        new String[] { "img","date","tmp"},
//                        new int[] {R.id.list_image, R.id.list_date,R.id.list_tmp});
//                weather_ListView.setAdapter(adapter);
                //---2---
                mData = getData();
                adapter = new WeatherListAdapter(getActivity());
                ld.getListView().setAdapter(adapter);
                //---3---
//                WeatherAdapter adapter = new WeatherAdapter(getActivity(),s);
//                weather_ListView.setAdapter(adapter);
                ld.show();
            }
        });

        text_Hum.setText("湿度:" + now_Hum + "%");
        text_Weather.setText(now_Weather);
        text_Tmp.setText(now_Tmp);
        text_City.setText(city);
        image_Weather.setImageResource(getWeatherImage(now_Cond));

    }

    private int getWeatherImage(int n) {
        int mimg = R.drawable.overcast;
        switch (n) {
            case 100:
                mimg = R.drawable.sunny;
                break;
            case 101:
                mimg = R.drawable.cloudy;
                break;
            case 102:
                mimg = R.drawable.cloudy;
                break;
            case 103:
                mimg = R.drawable.partlycloudy;
                break;
            case 104:
                mimg = R.drawable.overcast;
                break;
            case 300:
                mimg = R.drawable.showerrain;
                break;
            case 301:
                mimg = R.drawable.heavryrain;
                break;
            case 302:
                mimg = R.drawable.thundershower;
                break;
            case 305:
                mimg = R.drawable.lightrain;
                break;
            case 306:
                mimg = R.drawable.moderaterain;
                break;
            case 307:
                mimg = R.drawable.heavryrain;
                break;
            case 308:
                mimg = R.drawable.severestorm;
                break;
            case 310:
                mimg = R.drawable.severestorm;
                break;
            case 311:
                mimg = R.drawable.severestorm;
                break;
            case 312:
                mimg = R.drawable.severestorm;
                break;
            case 313:
                mimg = R.drawable.freezingrain;
                break;
            case 400:
                mimg = R.drawable.lightsnow;
                break;
            case 401:
                mimg = R.drawable.moderatesnow;
                break;
            case 402:
                mimg = R.drawable.heavysnow;
                break;
            case 403:
                mimg = R.drawable.heavysnow;
                break;
            case 404:
                mimg = R.drawable.sleet;
                break;
            case 406:
                mimg = R.drawable.sleet;
                break;
        }
        return mimg;
    }

    private int getDialogbackground(int cond, int tmp) {
        int mimg = R.drawable.dialogblue;
        if (cond == 100) {
            if (tmp > 30)
                mimg = R.drawable.dialogyellow;
            else if (tmp > 25)
                mimg = R.drawable.dialogorange;
            else
                mimg = R.drawable.dialogblue;
        } else if (cond == 104)
            mimg = R.drawable.dialogpurple;
        else if (cond >= 300 && cond <= 312)
            mimg = R.drawable.dialoggrey;
        else if (cond >= 400 && cond <= 407)
            mimg = R.drawable.dialogwhite;
        return mimg;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        int image_picid;
        int image_code;
        int tmax;
        int bc;
        Map<String, Object> map;
        for (int i = 0; i < 7; i++) {
            map = new HashMap<String, Object>();
            map.put("date", DataManager.getWeatherInfo().dailyForecast.get(i).date);
            map.put("tmp_min", DataManager.getWeatherInfo().dailyForecast.get(i).tmp.min);
            map.put("tmp_max", DataManager.getWeatherInfo().dailyForecast.get(i).tmp.max);
            map.put("cond", DataManager.getWeatherInfo().dailyForecast.get(i).cond.txtD);
            image_code = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(i).cond.codeD);
            image_picid = getWeatherImage(image_code);
            map.put("img", image_picid);
            tmax = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(i).tmp.max);
            bc = getDialogbackground(image_code, tmax);
            map.put("background", bc);
            list.add(map);
        }
        return list;
    }

    public String StringData(String data) {
        String a[] = data.split("-");
        String mMonth = a[1];
        String mDay = a[2];
        String mWay = "Sun";


        int month = Integer.parseInt(mMonth);
        int way = 1;
        switch (way) {
            case 1:
                mWay = "Sun";
                break;
            case 2:
                mWay = "Mon";
                break;
            case 3:
                mWay = "Tue";
                break;
            case 4:
                mWay = "Wed";
                break;
            case 5:
                mWay = "Thu";
                break;
            case 6:
                mWay = "Fri";
                break;
            case 7:
                mWay = "Sat";
                break;
        }
        switch (month) {
            case 1:
                mMonth = "Jan";
                break;
            case 2:
                mMonth = "Feb";
                break;
            case 3:
                mMonth = "Mar";
                break;
            case 4:
                mMonth = "Apr";
                break;
            case 5:
                mMonth = "May";
                break;
            case 6:
                mMonth = "Jun";
                break;
            case 7:
                mMonth = "Jul";
                break;
            case 8:
                mMonth = "Aug";
                break;
            case 9:
                mMonth = "Sep";
                break;
            case 10:
                mMonth = "Oct";
                break;
            case 11:
                mMonth = "Nov";
                break;
            case 12:
                mMonth = "Dec";
                break;
        }

        return mMonth + "." + mDay;
    }

    @Override
    public void onPageSelected() {
        if (DataManager.isMyPlantChanged) {

            HashMap param = new HashMap<>();
            if (mApp.isLogined()) {
                UserInfo ui = (UserInfo) mApp.getUserModel();
                param.put("userName", ui.getuserName());
            } else {
                param.put("userName", "blank");
            }
            SimpleTaskManager.startNewTask(new NetworkTask(getTaskTag(), getActivity(), GetMyPlantResponse.class,
                    param, Constants.GETMYPLANT_URL, NetworkTask.GET) {
                @Override
                public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                    GetMyPlantResponse response = (GetMyPlantResponse) result.resultObject;
                    DataManager.setMyPlant(response);
                    plantList_Data = new ArrayList<String>();
                    if (DataManager.getMyPlant().response.size() != 0) {
                        for (int i = 0; i < DataManager.getMyPlant().response.size(); i++) {
                            plantList_Data.add(DataManager.getMyPlant().response.get(i).pic);
                        }
                    } else {
//            plantList_Data.add("default1");
                    }
                    mAdapter.updatelist(plantList_Data);
                    if (DataManager.getMyPlant().response.size() == 0) {
                        image_Plant.setImageResource(R.drawable.logo2);
                    } else {
                        Picasso.with(getActivity()).load(Constants.MYPLANTPIC_URL + plantList_Data.get(0)).into(image_Plant);
                    }
                }

                @Override
                public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                }
            });
            DataManager.isMyPlantChanged = false;
        }
        if (DataManager.isCityChanged) {
            HashMap<String, String> params = new HashMap<String, String>();
            if (mApp.isLogined()) {
                UserInfo userInfo = (UserInfo) mApp.getUserModel();
                params.put("key", Constants.WEATHER_KEY);
                params.put("city", userInfo.getcity());
            } else {
                params.put("key", Constants.WEATHER_KEY);
                params.put("city", "北京");

            }

            SimpleTaskManager.startNewTask(new NetworkTask(
                    getTaskTag(),
                    getActivity(),
                    WeatherAPI.class,
                    params,
                    Constants.WEATHER_API_URL,
                    NetworkTask.GET) {
                @Override
                public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                    WeatherAPI weatherAPI = (WeatherAPI) result.resultObject;
                    WeatherResponse weatherInfo = weatherAPI.response.get(0);
                    DataManager.setWeatherInfo(weatherInfo);
                    initController();

                }

                @Override
                public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                }
            });


            DataManager.isCityChanged = false;
        }
        if (DataManager.isMyPlantPicChanged) {
            for (int i = 0; i < DataManager.getMyPlant().response.size(); i++) {
                String temp = Constants.MYPLANTPIC_URL + DataManager.getMyPlant().response.get(i).pic;
                Picasso.with(getActivity()).invalidate(temp);
                onLoadedNetworkData(getContentView());
                Picasso.with(getActivity()).load(Constants.MYPLANTPIC_URL + plantList_Data.get(0)).into(image_Plant);
            }
            DataManager.isMyPlantPicChanged = false;
        }
    }

    public final class ViewHolder {
        public LinearLayout weather_layout;
        public ImageView weather_image;
        public TextView weather_date;
        public TextView weather_cond;
        public TextView weather_tmp;
    }

    public class WeatherListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public WeatherListAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData != null ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.listview_weather, null);
                holder.weather_layout = (LinearLayout) convertView.findViewById(R.id.list_dialog);
                holder.weather_image = (ImageView) convertView.findViewById(R.id.list_image);
                holder.weather_cond = (TextView) convertView.findViewById(R.id.list_weatger);
                holder.weather_date = (TextView) convertView.findViewById(R.id.list_date);
                holder.weather_tmp = (TextView) convertView.findViewById(R.id.list_tmp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.weather_image.setBackgroundResource((Integer) mData.get(position).get("img"));
            holder.weather_date.setText(StringData((String) mData.get(position).get("date")));
            holder.weather_tmp.setText(mData.get(position).get("tmp_min") + "~" + mData.get(position).get("tmp_max") + "℃");
            holder.weather_cond.setText((String) mData.get(position).get("cond"));
            holder.weather_layout.setBackgroundResource((Integer) mData.get(position).get("background"));
            return convertView;
        }
    }


    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {
        plantList_Data = new ArrayList<String>();
        if (DataManager.getMyPlant().response.size() != 0) {
            for (int i = 0; i < DataManager.getMyPlant().response.size(); i++) {
                plantList_Data.add(DataManager.getMyPlant().response.get(i).pic);
            }
        } else {
//            plantList_Data.add("default1");
        }

        mAdapter = new PlantListAdapter(getActivity(), plantList_Data);
        mAdapter.setOnItemClickLitener(new PlantListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                nowPosition = position;
                Picasso.with(getActivity()).load(Constants.MYPLANTPIC_URL + plantList_Data.get(position)).into(image_Plant);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mdataAnaylsis = new DataAnalysis();
        tips = mdataAnaylsis.getResult();
    }

    @Override
    public void onResume() {
        onPageSelected();
        super.onResume();
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_park;
    }
}
