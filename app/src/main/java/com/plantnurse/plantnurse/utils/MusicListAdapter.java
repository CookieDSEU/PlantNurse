package com.plantnurse.plantnurse.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.plantnurse.plantnurse.R;
import com.plantnurse.plantnurse.model.MusicInfo;

import java.util.List;

/**
 * Created by Yxuan on 2016/9/6.
 */
final class ViewHolder {
    public TextView music_title;
    public TextView music_artist;
}

public class MusicListAdapter extends BaseAdapter {
    private Context context;        //上下文对象引用
    private LayoutInflater mInflater;
    private List<MusicInfo> musicInfos;   //存放MusicInfo引用的集合
    private MusicInfo musicInfo;        //MusicInfo对象引用
    private ViewHolder holder;

    public MusicListAdapter(Context context, List<MusicInfo> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return musicInfos.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.music_list_item, null);
            holder.music_title = (TextView) convertView.findViewById(R.id.music_title);
            holder.music_artist = (TextView) convertView.findViewById(R.id.music_artist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        musicInfo = musicInfos.get(position);
        holder.music_title.setText(musicInfo.getTitle());         //显示标题
        holder.music_artist.setText(musicInfo.getArtist());       //显示艺术家

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
}
