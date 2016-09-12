package com.plantnurse.plantnurse.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.plantnurse.plantnurse.model.Alarm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmInfo {

    private DbHelper dbHelper;

    public AlarmInfo(Context context) {

        dbHelper = new DbHelper(context);
//        SQLiteDatabase db=dbHelper.getReadableDatabase();
//        dbHelper.onUpgrade(db,1,1);
    }

    public int insert(Alarm alarm) {
        //打开连接，写入数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Alarm.KEY_RoleColor, alarm.roleColor);
        values.put(Alarm.KEY_Content, alarm.content);
        values.put(Alarm.KEY_Time, alarm.time);
        values.put(Alarm.KEY_IsAlarm, alarm.isAlarm);
        values.put(Alarm.KEY_PlantName, alarm.plantName);
        values.put(Alarm.KEY_Frequency, alarm.frequency);
        values.put(Alarm.KEY_Water, alarm.water);
        values.put(Alarm.KEY_Sun, alarm.sun);
        values.put(Alarm.KEY_TakeBack, alarm.takeBack);
        values.put(Alarm.KEY_TakeCare, alarm.takeCare);
        values.put(Alarm.KEY_Fertilization, alarm.fertilization);
        values.put(Alarm.KEY_Weather, alarm.weather);
        values.put(Alarm.KEY_Music, alarm.music);
        values.put(Alarm.KEY_Available, alarm.available);
        //返回行数值
        long alarm_Id = db.insert(Alarm.TABLE, null, values);
        db.close();
        return (int) alarm_Id;
    }

    public void delete(int alarm_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Alarm.TABLE, Alarm.KEY_ID + "=?", new String[]{String.valueOf(alarm_Id)});
        db.close();

    }

    public void update(Alarm alarm) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Alarm.KEY_RoleColor, alarm.roleColor);
        values.put(Alarm.KEY_Content, alarm.content);
        values.put(Alarm.KEY_Time, alarm.time);
        values.put(Alarm.KEY_IsAlarm, alarm.isAlarm);
        values.put(Alarm.KEY_Frequency, alarm.frequency);
        values.put(Alarm.KEY_PlantName, alarm.plantName);
        values.put(Alarm.KEY_Water, alarm.water);
        values.put(Alarm.KEY_Sun, alarm.sun);
        values.put(Alarm.KEY_TakeBack, alarm.takeBack);
        values.put(Alarm.KEY_TakeCare, alarm.takeCare);
        values.put(Alarm.KEY_Fertilization, alarm.fertilization);
        values.put(Alarm.KEY_Weather, alarm.weather);
        values.put(Alarm.KEY_Music, alarm.music);
        values.put(Alarm.KEY_Available, alarm.available);
        db.update(Alarm.TABLE, values, Alarm.KEY_ID + "=?", new String[]{String.valueOf(alarm.alarm_id)});
        db.close();
    }

    public ArrayList<HashMap<String, String>> getAlarmList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Alarm.KEY_ID + "," +
                Alarm.KEY_RoleColor + "," +
                Alarm.KEY_Content + "," +
                Alarm.KEY_Time + "," +
                Alarm.KEY_IsAlarm + "," +
                Alarm.KEY_Frequency + "," +
                Alarm.KEY_PlantName + "," +
                Alarm.KEY_Water + "," +
                Alarm.KEY_Sun + "," +
                Alarm.KEY_TakeBack + "," +
                Alarm.KEY_TakeCare + "," +
                Alarm.KEY_Fertilization + "," +
                Alarm.KEY_Weather + "," +
                Alarm.KEY_Music + "," +
                Alarm.KEY_Available +
                " FROM " + Alarm.TABLE;
        ArrayList<HashMap<String, String>> alarmList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> alarm = new HashMap<String, String>();
                alarm.put("alarm_id", cursor.getString(cursor.getColumnIndex(Alarm.KEY_ID)));
                alarm.put("roleColor", cursor.getString(cursor.getColumnIndex(Alarm.KEY_RoleColor)));
                alarm.put("content", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Content)));
                alarm.put("time", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Time)));
                alarm.put("isAlarm", cursor.getString(cursor.getColumnIndex(Alarm.KEY_IsAlarm)));
                alarm.put("frequency", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Frequency)));
                alarm.put("plantNurse", cursor.getString(cursor.getColumnIndex(Alarm.KEY_PlantName)));
                alarm.put("water", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Water)));
                alarm.put("sun", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Sun)));
                alarm.put("takeBack", cursor.getString(cursor.getColumnIndex(Alarm.KEY_TakeBack)));
                alarm.put("takeCare", cursor.getString(cursor.getColumnIndex(Alarm.KEY_TakeCare)));
                alarm.put("fertilization", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Fertilization)));
                alarm.put("weather", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Weather)));
                alarm.put("music", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Music)));
                alarm.put("available", cursor.getString(cursor.getColumnIndex(Alarm.KEY_Available)));
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarmList;
    }

    public Alarm getAlarmById(int Id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                Alarm.KEY_ID + "," +
                Alarm.KEY_RoleColor + "," +
                Alarm.KEY_Content + "," +
                Alarm.KEY_Time + "," +
                Alarm.KEY_IsAlarm + "," +
                Alarm.KEY_Frequency + "," +
                Alarm.KEY_PlantName + "," +
                Alarm.KEY_Water + "," +
                Alarm.KEY_Sun + "," +
                Alarm.KEY_TakeBack + "," +
                Alarm.KEY_TakeCare + "," +
                Alarm.KEY_Fertilization + "," +
                Alarm.KEY_Weather + "," +
                Alarm.KEY_Music + "," +
                Alarm.KEY_Available +
                " FROM " + Alarm.TABLE
                + " WHERE " +
                Alarm.KEY_ID + "=?";

        Alarm alarm = new Alarm();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(Id)});
        if (cursor.moveToFirst()) {
            do {
                alarm.alarm_id = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_ID));
                alarm.roleColor = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_RoleColor));
                alarm.content = cursor.getString(cursor.getColumnIndex(Alarm.KEY_Content));
                alarm.time = cursor.getString(cursor.getColumnIndex(Alarm.KEY_Time));
                alarm.isAlarm = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_IsAlarm));
                alarm.frequency = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_Frequency));
                alarm.plantName = cursor.getString(cursor.getColumnIndex(Alarm.KEY_PlantName));
                alarm.water = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_Water));
                alarm.sun = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_Sun));
                alarm.takeBack = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_TakeBack));
                alarm.takeCare = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_TakeCare));
                alarm.fertilization = cursor.getInt(cursor.getColumnIndex(Alarm.KEY_Fertilization));
                alarm.weather = cursor.getString(cursor.getColumnIndex(Alarm.KEY_Weather));
                alarm.music = cursor.getString(cursor.getColumnIndex(Alarm.KEY_Music));
                alarm.available = cursor.getString(cursor.getColumnIndex(Alarm.KEY_Available));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarm;
    }
}

