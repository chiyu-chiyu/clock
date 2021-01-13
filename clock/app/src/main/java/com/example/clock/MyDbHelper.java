package com.example.clock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class MyDbHelper extends SQLiteOpenHelper {
    private static final String mTablename = "mymemo";//数据表的名称
    public MyDbHelper(@Nullable Context context) {
        super(context, "mymemo.db", null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + mTablename + "(_id integer primary key autoincrement, " +
                "now_time varchar(50) unique,information varchar(100) )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}