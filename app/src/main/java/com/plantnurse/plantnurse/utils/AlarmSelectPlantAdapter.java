package com.plantnurse.plantnurse.utils;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.plantnurse.plantnurse.Activity.AddAlarmActivity;
import com.plantnurse.plantnurse.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Yxuan on 2016/8/30.
 */
public class AlarmSelectPlantAdapter extends RecyclerView.Adapter<AlarmSelectPlantAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> datas;
    private List<String> selectedDatas;
    private AddAlarmActivity addAlarmActivity = new AddAlarmActivity();
    private Context mcontext;

    public AlarmSelectPlantAdapter(Context context, List<String> datas,
                                   List<String> selectedDatas) {
        this.layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        mcontext = context;
        this.selectedDatas = selectedDatas;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImg mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mImg = (CircleImg) itemView.findViewById(R.id.id_index_addplant_item_image);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //可直接引用花园中新建植物的布局
        View view = layoutInflater.inflate(R.layout.item_addplant, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int positon) {
        Picasso.with(mcontext).load(Constants.MYPLANTPIC_URL + datas.get(positon)).into(viewHolder.mImg);

        int i;//判断如何修改select的值
        if (selectedDatas.isEmpty()) {//若没有选择的植物，则全部初始化为0
            addAlarmActivity.setSelect(positon, 0);
        } else {
            for (i = 0; i < selectedDatas.size(); i++) {//遍历一遍被选择的植物，比对
                if (datas.get(positon).equals(selectedDatas.get(i))) {//若已被选择，则初始化为1
                    addAlarmActivity.setSelect(positon, 1);
                    viewHolder.mImg.setBorderWidth(5);
                    viewHolder.mImg.setBorderColor(R.color.greenborder);
                    break;
                }
            }
            if (i == datas.size()) {//遍历一遍没有则初始化为0
                addAlarmActivity.setSelect(positon, 0);
            }
        }

        viewHolder.mImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddAlarmActivity.getSelect().get(positon) == 0) {//未被选择，则选择显示边框
                    viewHolder.mImg.setBorderWidth(5);
                    viewHolder.mImg.setBorderColor(R.color.greenborder);
                    AddAlarmActivity.getSelect().set(positon, 1);//select相应位置设为1
                } else {
                    viewHolder.mImg.setBorderWidth(0);
                    viewHolder.mImg.setBorderColor(R.color.nocolor);
                    AddAlarmActivity.getSelect().set(positon, 0);
                }
            }
        });
    }
}
