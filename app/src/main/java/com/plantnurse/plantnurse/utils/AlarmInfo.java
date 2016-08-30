package com.plantnurse.plantnurse.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kot32.ksimplelibrary.KSimpleApplication;
import com.plantnurse.plantnurse.Fragment.AlarmFragment;
import com.plantnurse.plantnurse.model.Alarm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yxuan on 2016/8/26.
 */
public class AlarmInfo  {

    private DbHelper dbHelper;

    public AlarmInfo(Context context){
        dbHelper=new DbHelper(context);
    }

    public int insert(Alarm alarm){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Alarm.KEY_Content,alarm.content);
        values.put(Alarm.KEY_Time,alarm.time);
        values.put(Alarm.KEY_IsAlarm,alarm.isAlarm);
        //返回行数值
        long alarm_Id=db.insert(Alarm.TABLE,null,values);
        db.close();
        return (int)alarm_Id;
    }

    public void delete(int alarm_Id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Alarm.TABLE, Alarm.KEY_ID + "=?", new String[]{String.valueOf(alarm_Id)});
        db.close();

    }

    public void update(Alarm alarm){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Alarm.KEY_Content,alarm.content);
        values.put(Alarm.KEY_Time,alarm.time);
        //values.put(Alarm.KEY_IsAlarm,alarm.isAlarm);

        db.update(Alarm.TABLE,values,Alarm.KEY_ID+"=?",new String[] { String.valueOf(alarm.alarm_id) });
        db.close();
    }

    public ArrayList<HashMap<String, String>> getAlarmList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Alarm.KEY_ID+","+
                Alarm.KEY_Content+","+
                Alarm.KEY_Time+","+
                Alarm.KEY_IsAlarm+","+
                Alarm.KEY_Frequency+","+
                Alarm.KEY_PlantName+","+
                Alarm.KEY_Weather+","+
                Alarm.KEY_Music+","+
                Alarm.KEY_Available+
                " FROM "+Alarm.TABLE;
        ArrayList<HashMap<String,String>> alarmList=new ArrayList<HashMap<String, String>>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String,String> alarm=new HashMap<String,String>();
                alarm.put("alarm_id",cursor.getString(cursor.getColumnIndex(Alarm.KEY_ID)));
                alarm.put("content",cursor.getString(cursor.getColumnIndex(Alarm.KEY_Content)));
                alarm.put("time",cursor.getString(cursor.getColumnIndex(Alarm.KEY_Time)));
                alarm.put("isAlarm",cursor.getString(cursor.getColumnIndex(Alarm.KEY_IsAlarm)));
                alarmList.add(alarm);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarmList;
    }

    public Alarm getAlarmById(int Id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Alarm.KEY_ID + "," +
                Alarm.KEY_Content + "," +
                Alarm.KEY_Time +","+
                Alarm.KEY_IsAlarm+","+
                Alarm.KEY_Frequency +","+
                Alarm.KEY_PlantName +","+
                Alarm.KEY_Weather +","+
                Alarm.KEY_Music +","+
                Alarm.KEY_Available+
                " FROM " + Alarm.TABLE
                + " WHERE " +
                Alarm.KEY_ID + "=?";

        Alarm alarm=new Alarm();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});
        if(cursor.moveToFirst()){
            do{
                alarm.alarm_id =cursor.getInt(cursor.getColumnIndex(Alarm.KEY_ID));
                alarm.content=cursor.getString(cursor.getColumnIndex(Alarm.KEY_Content));
                alarm.time  =cursor.getString(cursor.getColumnIndex(Alarm.KEY_Time));
                alarm.isAlarm=cursor.getInt(cursor.getColumnIndex(Alarm.KEY_IsAlarm));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarm;
    }
}

