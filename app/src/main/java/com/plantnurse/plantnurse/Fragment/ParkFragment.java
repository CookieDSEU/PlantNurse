package com.plantnurse.plantnurse.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;

import com.kot32.ksimplelibrary.activity.i.IBaseAction;
import com.kot32.ksimplelibrary.fragment.t.base.KSimpleBaseFragmentImpl;
//import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.Activity.AddplantActivity;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.utils.CircleImg;
import com.plantnurse.plantnurse.utils.DataManager;
import com.plantnurse.plantnurse.utils.ListDialog;
import com.plantnurse.plantnurse.utils.PlantListAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Eason_Tao on 2016/8/26.
 */

public class ParkFragment extends KSimpleBaseFragmentImpl implements IBaseAction{
    //view
    private TextView text_Tmp;
    private ImageView image_Weather;
    private ImageButton button_tips;
    private CircleImg image_Plant;
    private TextView text_City;
    private ListView weather_ListView;
    private PlantListAdapter mAdapter;//添加植物的图片适配器
    private RecyclerView mRecyclerView;
    //data
    private String now_Tmp;
    private String city;
    private int now_Cond;
    private  List<Map<String,Object>> mData;
    private List<Integer> plantList_Data;//添加植物的图片列表
    private Context mContext;
    private WeatherListAdapter adapter;

    @Override
    public int initLocalData() {
        mContext=getActivity();
        return 0;
    }

    @Override
    public void initView(ViewGroup view) {
        initDatas();
        text_Tmp = (TextView) view.findViewById(R.id.text_temp);
        text_City = (TextView) view.findViewById(R.id.text_city);
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
        mAdapter = new PlantListAdapter(getActivity(), plantList_Data);
        mAdapter.setOnItemClickLitener(new PlantListAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                image_Plant.setImageResource(plantList_Data.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }


    //初始化plantlist
    private void initDatas()
    {
        plantList_Data = new ArrayList<Integer>(Arrays.asList(R.drawable.flower1_s, R.drawable.flower2_s,
                R.drawable.flower3_s,R.drawable.flower4_s, R.drawable.flower5_s,R.drawable.flower1_s,
                R.drawable.flower2_s, R.drawable.flower3_s, R.drawable.flower4_s, R.drawable.flower5_s));

    }
    @Override
    public void initController() {
        now_Tmp = DataManager.getWeatherInfo().now.tmp;
        now_Cond = Integer.parseInt(DataManager.getWeatherInfo().now.cond.code);
        city = DataManager.getWeatherInfo().basic.city;
        image_Plant.setImageResource(R.drawable.flower2_s);
        image_Plant.setBorderWidth(6);
        //image_Plant.setBorderColor(R.color.flowerborder);
        image_Plant.setBorderColor(Color.WHITE);
        image_Plant.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), AddplantActivity.class);
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

        image_Weather.setOnClickListener(new View.OnClickListener() {
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

        text_Tmp.setText(now_Tmp + "℃");
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
            //Log.e("test", DataManager.getWeatherInfo().dailyForecast.get(i).tmp.min);
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
            //adapter.notifyDataSetChanged();
        }
        return list;
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

            //String str=mData.get(position).get("img");
            //int drawable =mContext .getResources().getIdentifier(str, "drawable", mContext.getPackageName());
            // holder.weather_Image.setImageResource(drawable);
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
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_park;
    }
}
