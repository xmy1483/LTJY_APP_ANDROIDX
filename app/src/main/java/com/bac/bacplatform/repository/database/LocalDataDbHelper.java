package com.bac.bacplatform.repository.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bac.bacplatform.conf.Constants;


/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.repository.database
 * 创建人：Wjz
 * 创建时间：2017/4/11
 * 类描述： 创建helper
 */

public class LocalDataDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BacLocal.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String LONG_TYPE = " LONG";
    private static final String COMMA_SEP = " ,";

    // 保险
    public static final String T_CONTENT = "insurance_content";
    public static final String T_INDEX = "insurance_index";


    // 创建表
    private static final String SQL_CREATE_CACHE_TABLE =
            "CREATE TABLE " + Constants.LocalDb.LOCAL_TABLE_NAME + " (" +
                    Constants.LocalDb._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    Constants.LocalDb.LOCAL_JSON + TEXT_TYPE + COMMA_SEP +
                    Constants.LocalDb.LOCAL_MD5 + TEXT_TYPE + COMMA_SEP +
                    Constants.LocalDb.LOCAL_KEY + TEXT_TYPE + COMMA_SEP +
                    Constants.LocalDb.LOCAL_TIME + LONG_TYPE +
                    " )";

    // 创建保险缓存表
    private static final String sql =
            "CREATE TABLE " + T_CONTENT + " ( " + Constants.ContentTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    Constants.ContentTable.KEY + " TEXT UNIQUE ," +
                    Constants.ContentTable.VALUE + " TEXT ," +
                    Constants.ContentTable.PHONE + " TEXT ," +
                    Constants.ContentTable._MD5 + " TEXT " +
                    ");";//--> 表结构

    // 创建保险缓存表
    private static final String sql2 =
            "CREATE TABLE " + T_INDEX + " ( " + Constants.IndexTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.IndexTable.KEY + " TEXT ," +
                    Constants.IndexTable.TEMP + " TEXT " +
                    ");";

    public LocalDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        //db.execSQL(SQL_CREATE_CACHE_TABLE);// 本地http缓存

        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
