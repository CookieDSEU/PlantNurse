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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.activity.i.ITabPageAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
//import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.MainApplication;
import com.plantnurse.plantnurse.Network.GetMyPlantResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.UserInfo;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.Constants;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.ListDialog;
import com.plantnurse.plantnurse.utils.PlantListAdapter;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Eason_Tao on 2016/8/26.
 */

public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction,ITabPageAction{
    //view
    private LinearLayout layout_Weather;
    private TextView text_Hum;
    private TextView text_Weather;
    private TextView text_Tmp;
    private ImageView image_Weather;
    private ImageButton button_tips;
    private CircleImg image_Plant;
    private TextView text_City;
    private ListView weather_ListView;
    private PlantListAdapter mAdapter;//添加植物的图片适配器
    private RecyclerView mRecyclerView;
    private FloatingActionsMenu float_menu;
    private FloatingActionButton float_Addplant;
    private FloatingActionButton float_Plantlist;
    //data
    private String now_Hum;
    private String now_Weather;
    private String now_Tmp;
    private String city;
    private int now_Cond;
    private MainApplication mApp;
    private  List<Map<String,Object>> mData;
    private List<String> plantList_Data;//添加植物的图片列表
    private Context mContext;
    private WeatherListAdapter adapter;

    @Override
    public int initLocalData() {
        mApp=(MainApplication)getActivity().getApplication();
        mContext=getActivity();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        text_Tmp = (TextView) view.findViewById(R.id.text_temp);
        text_City = (TextView) view.findViewById(R.id.text_city);
        text_Weather = (TextView) view.findViewById(R.id.text_weather);
        text_Hum = (TextView) view.findViewById(R.id.text_hum);

        float_menu= (FloatingActionsMenu) view.findViewById(R.id.floatingmenu_park);
        float_Addplant = (FloatingActionButton) view.findViewById(R.id.minifloat_addplant);
        float_Plantlist = (FloatingActionButton) view.findViewById(R.id.minifloat_plantlist);

//        float_Addplant.setTitle("添加植物");
        float_Addplant.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddplantActivity.class);
                intent.putExtra("addplant",0);
                startActivity(intent); // 启动Activity
            }
        });
//        float_Plantlist.setTitle("植物列表");
        float_Plantlist.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
            }
        });

        layout_Weather = (LinearLayout) view.findViewById(R.id.layout_weather);
        image_Weather = (ImageView) view.findViewById(R.id.image_weather);
        image_Plant = (CircleImg) view.findViewById(R.id.image_flower);
        button_tips = (ImageButton) view.findViewById(R.id.tipButton);
        //选择图片recyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_plantlist);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器

    }


    //初始化plantlist
    private void initDatas()
    {

    }
    @Override
    public void initController() {
        now_Tmp = DataManager.getWeatherInfo().now.tmp;
        now_Cond = Integer.parseInt(DataManager.getWeatherInfo().now.cond.code);
        now_Hum = DataManager.getWeatherInfo().now.hum;
        now_Weather = DataManager.getWeatherInfo().now.cond.txt;

        city = DataManager.getWeatherInfo().basic.city;
        image_Plant.setImageResource(R.drawable.flower2_s);
        image_Plant.setBorderWidth(6);
        //image_Plant.setBorderColor(R.color.flowerborder);
        image_Plant.setBorderColor(Color.WHITE);
        image_Plant.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), AddplantActivity.class);
                intent.putExtra("addplant",0);
                startActivity(intent); // 启动Activity
                return false;
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
 //               Dialog calendar_Dialog = new Dialog(getActivity(), R.style.CalenderStyle);
//                calendar_Dialog.setContentView(R.layout.dialog_calender);

                ListDialog ld=new ListDialog(getActivity(),R.style.CalenderStyle);
//                Window calender_Window = calendar_Dialog.getWindow();
                Window calender_Window = ld.getWindow();
                WindowManager.LayoutParams lp = calender_Window.getAttributes();
                calender_Window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                lp.width = 1000;
                lp.height = 500;
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

        switch (now_Cond){
            case 100:
                image_Weather.setImageResource(R.drawable.p1);
                break;
            case 101:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 102:
                image_Weather.setImageResource(R.drawable.cloudy);
                break;
            case 103:
                image_Weather.setImageResource(R.drawable.cloudy_2);
                break;
            case 104:
                image_Weather.setImageResource(R.drawable.cloudy_3);
                break;
            case 300:
                image_Weather.setImageResource(R.drawable.rainy_2);
                break;
            case 302:
                image_Weather.setImageResource(R.drawable.thunder);
                break;
            case 305:
                image_Weather.setImageResource(R.drawable.rainy_3);
                break;
            case 306:
                image_Weather.setImageResource(R.drawable.rainy_3);
                break;
            case 307:
                image_Weather.setImageResource(R.drawable.rainy);
                break;
            case 400:
                image_Weather.setImageResource(R.drawable.snow);
                break;
            case 406:
                image_Weather.setImageResource(R.drawable.snow_2);
                break;
            }


    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        int image_code;
        Map<String, Object> map;
        for (int i = 0; i < 7; i++) {
            map = new HashMap<String, Object>();
            map.put("date", DataManager.getWeatherInfo().dailyForecast.get(i).date);
            map.put("tmp_min", DataManager.getWeatherInfo().dailyForecast.get(i).tmp.min);
            map.put("tmp_max", DataManager.getWeatherInfo().dailyForecast.get(i).tmp.max);
            image_code = Integer.parseInt(DataManager.getWeatherInfo().dailyForecast.get(i).cond.codeD);
            switch (image_code) {
                case 100:
                    map.put("img",R.drawable.sunny);
                    break;
                case 101:
                    map.put("img", R.drawable.cloudy);
                    break;
                case 102:
                    map.put("img", R.drawable.cloudy);
                    break;
                case 103:
                    map.put("img", R.drawable.cloudy_2);
                    break;
                case 104:
                    map.put("img", R.drawable.cloudy_3);
                    break;
                case 300:
                    map.put("img", R.drawable.rainy_2);
                    break;
                case 302:
                    map.put("img", R.drawable.thunder);
                    break;
                case 305:
                    map.put("img", R.drawable.rainy_3);
                    break;
                case 306:
                    map.put("img", R.drawable.rainy_3);
                    break;
                case 307:
                    map.put("img", R.drawable.rainy);
                    break;
                case 400:
                    map.put("img", R.drawable.snow);
                    break;
                case 406:
                    map.put("img", R.drawable.snow_2);
                    break;
           }
            list.add(map);
        }
        return list;
    }

    @Override
    public void onPageSelected() {
        HashMap param=new HashMap<>();
        if(mApp.isLogined()) {
            UserInfo ui = (UserInfo) mApp.getUserModel();
            param.put("userName", ui.getuserName());
        }
        else {
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
            }

            @Override
            public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

            }
        });

    }

    public final class ViewHolder{
        public ImageView weather_image;
        public TextView weather_date;
        public TextView weather_tmp;
    }

    public class WeatherListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public  WeatherListAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData!=null?mData.size():0;
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
            if (convertView == null){
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.listview_weather,null);
                holder.weather_image = (ImageView) convertView.findViewById(R.id.list_image);
                holder.weather_date = (TextView) convertView.findViewById(R.id.list_date);
                holder.weather_tmp = (TextView) convertView.findViewById(R.id.list_tmp);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.weather_image.setBackgroundResource((Integer) mData.get(position).get("img"));
            holder.weather_date.setText((String) mData.get(position).get("date"));
            holder.weather_tmp.setText((String)(mData.get(position).get("tmp_min")+"~"+mData.get(position).get("tmp_max")+"℃"));
            return convertView;
        }
    }


    @Override
    public void onLoadingNetworkData() {

    }

    @Override
    public void onLoadedNetworkData(View contentView) {
        plantList_Data = new ArrayList<String>();
        if(DataManager.getMyPlant().response.size()!= 0){
            for (int i =0;i<DataManager.getMyPlant().response.size();i++) {
                plantList_Data.add(DataManager.getMyPlant().response.get(i).pic);
            }
        }
        else {
//            plantList_Data.add("default1");
        }

        mAdapter = new PlantListAdapter(getActivity(), plantList_Data);
        mAdapter.setOnItemClickLitener(new PlantListAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
//                image_Plant.setImageResource(plantList_Data.get(position));
                Picasso.with(getActivity()).load(Constants.MYPLANTPIC_URL+plantList_Data.get(position)).into(image_Plant);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_park;
    }
}
