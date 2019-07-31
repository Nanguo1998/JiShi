package com.icebreaker.timelapse.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by 小侯同学 on 2018/4/5.
 */


public class MyDBOpenHelper extends SQLiteOpenHelper {
    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE unlockCount(date VARCHAR(50) PRIMARY KEY ,count INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE appUsageStats(id INTEGER PRIMARY KEY AUTOINCREMENT,date VARCHAR(50) ,time BIGINT,package VARCHAR(100),count INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE appType(package VARCHAR(100) PRIMARY KEY ,type VARCHAR(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}