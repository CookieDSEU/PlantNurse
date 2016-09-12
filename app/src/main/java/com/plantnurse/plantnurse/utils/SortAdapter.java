package com.plantnurse.plantnurse.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.SortModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * create by Heloise
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {

    //用的是SortModel 的list
    private List<SortModel> list = null;
    private Context mContext;

    public List<SortModel> getlist() {
        return list;
    }

    public SortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    //Adapter的核心，通过数据实例化view
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_plant, null);
            viewHolder.tvTitle = (TextView) view
                    .findViewById(R.id.tv_user_item_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.icon = (CircleImg) view
                    .findViewById(R.id.iv_user_item_icon);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        SortModel model = list.get(position);
        //设置名字和图片
        viewHolder.tvTitle.setText(model.getName());
        Picasso.with(mContext).load(model.getUrl()).into(viewHolder.icon);
        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        CircleImg icon;
    }

    /**
     * 得到首字母的ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    public String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}