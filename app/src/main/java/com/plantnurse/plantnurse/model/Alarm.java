package com.plantnurse.plantnurse.model;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class Alarm {

    //表名
    public static final String TABLE="Alarms";

    //表的各域名
    public static final String KEY_ID="alarm_id";
    public static final String KEY_Content="content";
    public static final String KEY_Time="time";
    public static final String KEY_IsAlarm="isAlarm";
    public static final String KEY_Frequency="frequency";
    public static final String KEY_PlantName="plantName";
    public static final String KEY_Weather="weather";
    public static final String KEY_Music="music";
    public static final String KEY_Available="available";

    //属性
    public int alarm_id;
    public String content;
    public String time;
    public int isAlarm;
    public int frequency;//哪个值对应哪个频率
    public String plantName;
    public String weather;
    public int music;
    public String available;


}
