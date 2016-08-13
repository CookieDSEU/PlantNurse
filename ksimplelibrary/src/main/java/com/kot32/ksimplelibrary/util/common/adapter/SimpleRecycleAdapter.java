package com.kot32.ksimplelibrary.util.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by kot32 on 15/10/15.
 * T: 绑定对象数据的类型
 */
public abstract class SimpleRecycleAdapter<T> extends RecyclerView.Adapter {

    private List<T> data;
    private Context context;

    public SimpleRecycleAdapter(List<T> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public InnerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        //返回一个ViewHolder
        InnerViewHolder innerViewHolder
                = new InnerViewHolder(LayoutInflater.from(context).inflate(getItemLayoutID(), viewGroup, false));
        return innerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBind((InnerViewHolder) holder, position);
    }

    public abstract void onBind(InnerViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 子类复写该方法传入item的id
     *
     * @return
     */
    public abstract int getItemLayoutID();

    public class InnerViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views = new SparseArray<View>();
        private View convertView;

        public InnerViewHolder(View view) {
            super(view);
            convertView = view;
        }

        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }

    }

    public Object getItem(int position) {
        if (position >= data.size())
            return null;
        return data.get(position);
    }
}
