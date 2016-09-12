package com.plantnurse.plantnurse.utils;

/**
 * Created by Eason_Tao on 2016/8/29.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.plantnurse.plantnurse.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.ViewHolder> {

    private Context mcontext;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<String> mDatas;

    public PlantListAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mcontext = context;
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        CircleImg mImg;

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void updatelist(List<String> a) {
        mDatas = a;
        notifyDataSetChanged();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_addplant2, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (CircleImg) view.findViewById(R.id.id_index_addplant_item_image);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
//        viewHolder.mImg.setImageResource(mDatas.get(i));
        Picasso.with(mcontext).load(Constants.MYPLANTPIC_URL + mDatas.get(i)).into(viewHolder.mImg);
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }
}

