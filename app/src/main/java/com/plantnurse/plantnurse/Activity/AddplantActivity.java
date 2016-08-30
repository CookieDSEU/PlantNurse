//package com.plantnurse.plantnurse.Activity;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kot32.ksimplelibrary.util.common.adapter.SimpleBaseAdapter;
import com.kot32.ksimplelibrary.util.common.adapter.SimpleRecycleAdapter;
import com.plantnurse.plantnurse.R;
//import com.plantnurse.plantnurse.utils.WeatherManager;
//import com.plantnurse.plantnurse.utils.carousel.CarouselLayoutManager;
//import com.kot32.ksimplelibrary.activity.i.IBaseAction;
//import com.kot32.ksimplelibrary.activity.t.base.KSimpleBaseActivityImpl;
//import com.plantnurse.plantnurse.utils.carousel.CarouselZoomPostLayoutListener;
//import com.plantnurse.plantnurse.utils.carousel.CenterScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Eason_Tao on 2016/8/27.
 */
//public class AddplantActivity extends KSimpleBaseActivityImpl implements IBaseAction {
//
//    private List<Integer> test;
//    private List<Map<String, Object>> mData;
//    private PlantAdapter adapter;
//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//    private GoogleApiClient client;
//
//    @Override
//    public int initLocalData() {
//        return 0;
//    }
//
//    @Override
//    public void initView(ViewGroup view) {
//
//        mData = getData();
//        test = getData2();
//        adapter = new PlantAdapter(this);
//        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
//        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
//
//        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_plant);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(new Testadapter(test, this));
//        //recyclerView.setAdapter(adapter);
//        recyclerView.addOnScrollListener(new CenterScrollListener());
//
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//    }
//
//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Addplant Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }
//
//    private class Testadapter extends SimpleRecycleAdapter<Integer> {
//
//
//        public Testadapter(List<Integer> data, Context context) {
//            super(data, context);
//        }
//
//        @Override
//        public void onBind(InnerViewHolder holder, int position) {
//            ImageView plant = holder.getView(R.id.addplant_recycleimg);
//            plant.setBackgroundResource(test.get(0));
//        }
//
//        @Override
//        public int getItemLayoutID() {
//            return R.layout.activity_addplant;
//        }
//    }
//
//
//    private List<Map<String, Object>> getData() {
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map;
//
//        map = new HashMap<String, Object>();
//        map.put("img", R.drawable.sunny);
//        list.add(map);
//        map.put("img", R.drawable.cloudy);
//        list.add(map);
//        map.put("img", R.drawable.cloudy_2);
//        list.add(map);
//        map.put("img", R.drawable.cloudy_3);
//        list.add(map);
//        map.put("img", R.drawable.thunder);
//        list.add(map);
//        return list;
//    }
//
//    private List<Integer> getData2() {
//        List<Integer> list = new ArrayList<Integer>();
//        list.add(R.drawable.sunny);
//        list.add(R.drawable.cloudy_2);
//        list.add(R.drawable.cloudy);
//        list.add(R.drawable.thunder);
//        list.add(R.drawable.rainy);
//        return list;
//    }
//
//    public final class ViewHolder {
//        public ImageView plant_image;
//    }
//
//    public class PlantAdapter extends BaseAdapter {
//
//        private LayoutInflater mInflater;
//
//        public PlantAdapter(Context context) {
//            this.mInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return mData != null ? mData.size() : 0;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mData.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                holder = new ViewHolder();
//
//                convertView = mInflater.inflate(R.layout.activity_addplant, null);
//                holder.plant_image = (ImageView) convertView.findViewById(R.id.recycle_plant);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            holder.plant_image.setBackgroundResource((Integer) mData.get(position).get("img"));
//            return convertView;
//        }
//    }
//
//
//    @Override
//    public void initController() {
//
//    }
//
//    @Override
//    public void onLoadingNetworkData() {
//
//    }
//
//    @Override
//    public void onLoadedNetworkData(View contentView) {
//
//    }
//
//
//    @Override
//    public int getContentLayoutID() {
//        return R.layout.activity_addplant;
////    }
//}
