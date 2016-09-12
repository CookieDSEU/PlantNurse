package com.plantnurse.plantnurse.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.plantnurse.plantnurse.model.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yxuan on 2016/9/6.
 */
public class MusicLoader {

    public List<MusicInfo> getMusicInfo(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
        //最开始的默认音乐
        MusicInfo musicInfo0 = new MusicInfo();
        musicInfo0.setTitle("稳稳的幸福（默认音乐）");
        musicInfo0.setArtist("陈奕迅");
        musicInfo0.setUrl("陈奕迅-稳稳的幸福（默认）");
        musicInfos.add(musicInfo0);
        //本地歌曲
        for (int i = 0; i < cursor.getCount(); i++) {
            //新建一个歌曲对象,将从cursor里读出的信息存放进去,直到取完cursor里面的内容为止.
            MusicInfo musicInfo = new MusicInfo();
            cursor.moveToNext();

            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));    //音乐id

            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题

            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家

            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长

            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));    //文件路径

            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐

            if (isMusic != 0 && duration / (1000 * 60) >= 1) {        //只把1分钟以上的音乐添加到集合当中
                musicInfo.setId(id);
                musicInfo.setTitle(title);
                musicInfo.setArtist(artist);
                musicInfo.setUrl(url);
                musicInfos.add(musicInfo);
            }
        }
        return musicInfos;
    }
}

