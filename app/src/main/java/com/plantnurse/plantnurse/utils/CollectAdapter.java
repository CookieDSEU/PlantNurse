package com.plantnurse.plantnurse.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.Activity.CollectActivity;
import com.plantnurse.plantnurse.Activity.ShowActivity;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CollectModel;
import com.plantnurse.plantnurse.model.SortModel;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Heloise on 2016/9/3.
 */

//第二版 RecyclerView版本的

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<CollectModel> mlist=null;
    private CollectActivity mContext;

    //构造函数
    public CollectAdapter(CollectActivity context,List<CollectModel> list){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mlist=list;
    }

    //ViewHolder类
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View arg0)
        {
            super(arg0);
        }
        TextView pname;
        CircleImg pimage;
        TextView ptime;
    }

    //创建ViewHolder时
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_collect,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.pname=(TextView)view.findViewById(R.id.plantname);
        viewHolder.pimage=(CircleImg)view.findViewById(R.id.planticon);
        viewHolder.ptime=(TextView)view.findViewById(R.id.collectdata);
        return viewHolder;
    }

    //viewHolder填充数据
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.pname.setText(mlist.get(position).getName());
        Picasso.with(mContext).load(mlist.get(position).getUrl()).into(holder.pimage);
        holder.ptime.setText(mlist.get(position).getAddtime());
        //设置点击事件
        holder.itemView.findViewById(R.id.collectLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowActivity.class);
                String na =  mlist.get(position).getName();
                intent.putExtra("name", na);
                intent.putExtra("id", mlist.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }

    //获取循环的次数
    @Override
    public int getItemCount() {
        return mlist.size();
    }

}

