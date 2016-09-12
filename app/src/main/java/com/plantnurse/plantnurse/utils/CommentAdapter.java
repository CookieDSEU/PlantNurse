package com.plantnurse.plantnurse.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plantnurse.plantnurse.Activity.ShowActivity;
import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.CommentModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Heloise on 2016/9/7.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<CommentModel> mlist = null;
    private ShowActivity mContext;

    public void updatelist(List<CommentModel> a) {
        mlist = a;
        notifyDataSetChanged();
    }

    public CommentAdapter(ShowActivity context, List<CommentModel> list) {
        mContext = context;
        mlist = list;
        mInflater = LayoutInflater.from(mContext);
    }

    //ViewHolder类
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        TextView cname;
        TextView ctime;
        TextView ccomment;
        CircleImg cicon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cname = (TextView) view.findViewById(R.id.text_name);
        viewHolder.ctime = (TextView) view.findViewById(R.id.text_time);
        viewHolder.ccomment = (TextView) view.findViewById(R.id.text_comment);
        viewHolder.cicon = (CircleImg) view.findViewById(R.id.image_icon);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        for (int i = 0; i < mlist.size(); i++) {
            holder.cname.setText(mlist.get(position).getName());
            holder.ctime.setText(mlist.get(position).getTime());
            holder.ccomment.setText(mlist.get(position).getComment());
            Picasso.with(mContext).load(mlist.get(position).getIconUrl()).into(holder.cicon);
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
