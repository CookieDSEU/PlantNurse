package com.plantnurse.plantnurse.utils;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kot32.ksimplelibrary.manager.task.base.NetworkTask;
import com.kot32.ksimplelibrary.manager.task.base.SimpleTaskManager;
import com.kot32.ksimplelibrary.network.NetworkExecutor;
import com.plantnurse.plantnurse.Activity.CollectActivity;
import com.plantnurse.plantnurse.Activity.ShowActivity;
import com.plantnurse.plantnurse.Network.ChangeInfoResponse;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CollectPlantModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Heloise on 2016/9/3.
 */

//第二版 RecyclerView版本的

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CollectPlantModel> mlist = null;
    private CollectActivity mContext;
    private String mUserName;

    //构造函数
    public CollectAdapter(CollectActivity context, List<CollectPlantModel> list, String username) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mlist = list;
        mUserName = username;
    }

    //ViewHolder类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView pname;
        CircleImg pimage;
        TextView ptime;
    }

    //创建ViewHolder时
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_collect, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.pname = (TextView) view.findViewById(R.id.plantname);
        viewHolder.pimage = (CircleImg) view.findViewById(R.id.planticon);
        viewHolder.ptime = (TextView) view.findViewById(R.id.collectdata);
        return viewHolder;
    }

    //viewHolder填充数据
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        for (int i = 0; i < mlist.size(); i++) {
            holder.pname.setText(mlist.get(position).getName());
            Picasso.with(mContext).load(mlist.get(position).getUrl()).into(holder.pimage);
            holder.ptime.setText(mlist.get(position).getAddtime());
        }
        //设置点击事件
        holder.itemView.findViewById(R.id.collectLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowActivity.class);
                String na = mlist.get(position).getName();
                intent.putExtra("name", na);
                intent.putExtra("id", mlist.get(position).getId());
                mContext.startActivity(intent);
            }
        });

        holder.itemView.findViewById(R.id.collectLinearLayout).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("确认删除这条收藏？")
                        .setConfirmText("确认")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                //取消收藏
                                HashMap<String, String> param = new HashMap<String, String>();
                                param.put("userName", mUserName);
                                param.put("name", holder.pname.getText().toString());
                                SimpleTaskManager.startNewTask(new NetworkTask("deletestar", mContext, ChangeInfoResponse.class, param, Constants.DELETESTAR_URL, NetworkTask.GET) {
                                    @Override
                                    public void onExecutedMission(NetworkExecutor.NetworkResult result) {
                                        ChangeInfoResponse response = (ChangeInfoResponse) result.resultObject;
                                        if (response.getresponseCode() == 1) {
                                            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("取消成功!")
                                                    .show();
                                            mlist.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onExecutedFailed(NetworkExecutor.NetworkResult result) {

                                    }
                                });
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    //获取循环的次数
    @Override
    public int getItemCount() {
        String t = " " + mlist.size();
        return mlist.size();
    }

    public void updatelist(List<CollectPlantModel> a) {
        mlist = a;
        notifyDataSetChanged();
    }

}

