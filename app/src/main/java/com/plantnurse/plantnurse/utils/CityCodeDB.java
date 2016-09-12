package com.plantnurse.plantnurse.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Cookie_D on 2016/8/13.
 */
public class CityCodeDB {
    public static final String TABLE_PROVINCE = "province";
    public static final String TABLE_CITY = "city";
    private Context context;
    public CityCodeDB(Context context) {
        this.context = context;
    }
    public SQLiteDatabase getDatabase(String dbname) {
        AssetsDatabaseManager.initManager(context);
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase(dbname);
        return db;
    }

    // 查询province表,返回省份信息cursor
    public Cursor getAllProvince(SQLiteDatabase db) {
        if (db != null) {
            Cursor cur = db
                    .query(TABLE_PROVINCE, new String[]{"id", "name"}, null,
                            null, null, null, null);
            return cur;
        } else {
            return null;
        }
    }

    // 查询city表,返回指定省份的所有城市信息cursor
    public Cursor getCity(SQLiteDatabase db, String provinceid) {
        if (db != null) {
            Cursor cur = db.query(TABLE_CITY, new String[]{"id", "p_id",
                            "name"}, "p_id = ?", new String[]{provinceid}, null,
                    null, null);
            return cur;
        } else {
            return null;
        }
    }
}
