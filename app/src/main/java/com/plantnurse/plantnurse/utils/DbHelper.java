package com.plantnurse.plantnurse.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.plantnurse.plantnurse.model.Alarm;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class DbHelper extends SQLiteOpenHelper {

    //数据库版本号
    private static final int DATABASE_VERSION = 1;

    //数据库名称
    private static final String DATABASE_NAME = "plantNurse.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        String CREATE_TABLE_ALARM = "CREATE TABLE " + Alarm.TABLE + "("
                + Alarm.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Alarm.KEY_Content + " TEXT, "
                + Alarm.KEY_Time + " TEXT,"
                + Alarm.KEY_IsAlarm + " INTEGER,"
                + Alarm.KEY_Frequency + " INTEGER,"
                + Alarm.KEY_PlantName + " TEXT,"
                + Alarm.KEY_Weather + " TEXT,"
                + Alarm.KEY_Music + " INTEGER,"
                + Alarm.KEY_Available + " TEXT)";
        db.execSQL(CREATE_TABLE_ALARM);
    }



    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS " + Alarm.TABLE);

        //再次创建表
        onCreate(db);
    }

}
