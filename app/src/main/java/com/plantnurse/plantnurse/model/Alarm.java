package com.plantnurse.plantnurse.model;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class Alarm {

    //表名
    public static final String TABLE = "Alarms";

    //表的各域名
    public static final String KEY_ID = "alarm_id";
    public static final String KEY_RoleColor = "roleColor";
    public static final String KEY_Content = "content";
    public static final String KEY_Time = "time";
    public static final String KEY_IsAlarm = "isAlarm";
    public static final String KEY_Frequency = "frequency";
    public static final String KEY_PlantName = "plantName";
    public static final String KEY_Water = "water";
    public static final String KEY_Sun = "sun";
    public static final String KEY_TakeBack = "takeBack";
    public static final String KEY_TakeCare = "takeCare";
    public static final String KEY_Fertilization = "fertilization";
    public static final String KEY_Weather = "weather";
    public static final String KEY_Music = "music";
    public static final String KEY_Available = "available";

    //属性
    public int alarm_id;
    public int roleColor;
    public String content;
    public String time;
    public int isAlarm;
    public int frequency;//哪个值对应哪个频率
    public String plantName;
    public int water;
    public int sun;
    public int takeBack;
    public int takeCare;
    public int fertilization;
    public String weather;
    public String music;
    public String available;


}
