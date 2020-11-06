package com.bac.bacplatform.old.module.insurance.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.repository.database.LocalDataDbHelper;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.db.dao
 * 创建人：Wjz
 * 创建时间：2016/12/27
 * 类描述：
 */

public class InsuranceDataBaseDao {

    /*insert*/
    public static long insertContent(SQLiteDatabase db, String key, String value, String phone, String md5) {
        ContentValues values = new ContentValues();
        values.put(Constants.ContentTable.KEY, key);
        values.put(Constants.ContentTable.VALUE, value);
        values.put(Constants.ContentTable.PHONE, phone);
        values.put(Constants.ContentTable._MD5, md5);
        long insert = db.insert(LocalDataDbHelper.T_CONTENT, null, values);
        return insert;
    }

    public static long insertIndex(SQLiteDatabase db, String key, String temp) {
        ContentValues values = new ContentValues();
        values.put(Constants.IndexTable.KEY, key);
        values.put(Constants.IndexTable.TEMP, temp);
        long insert = db.insert(LocalDataDbHelper.T_INDEX, null, values);
        return insert;
    }

    /*add*/
    public static void addContent(SQLiteDatabase db, String key, String value, String phone, String md5) {
        int i = updateContent(db, key, value, phone, md5);
        if (i < 1) {
            insertContent(db, key, value, phone, md5);
        }
    }

    public static void addIndex(SQLiteDatabase db, String key, String temp) {
        int i = updateIndex(db, key, temp);
        if (i < 1) {
            insertIndex(db, key, temp);
        }

    }

    /*replace*/
    public static long replaceContent(SQLiteDatabase db, String key, String value, String phone, String md5) {
        ContentValues values = new ContentValues();
        values.put(Constants.ContentTable.KEY, key);
        values.put(Constants.ContentTable.VALUE, value);
        values.put(Constants.ContentTable.PHONE, phone);
        values.put(Constants.ContentTable._MD5, md5);
        return db.replace(LocalDataDbHelper.T_CONTENT, null, values);
    }

    public static long replaceIndex(SQLiteDatabase db, int _id, String key, String temp) {
        ContentValues values = new ContentValues();
        values.put(Constants.IndexTable._ID, _id);
        values.put(Constants.IndexTable.TEMP, temp);
        values.put(Constants.IndexTable.KEY, key);
        return db.replace(LocalDataDbHelper.T_INDEX, null, values);
    }

    /*update*/
    public static int updateContent(SQLiteDatabase db, String key, String value, String phone, String md5) {
        ContentValues values = new ContentValues();
        values.put(Constants.ContentTable.VALUE, value);
        values.put(Constants.ContentTable.PHONE, phone);
        values.put(Constants.ContentTable._MD5, md5);
        return db.update(LocalDataDbHelper.T_CONTENT, values, Constants.ContentTable.KEY.concat(" = ?"), new String[]{key});

    }

    public static int updateIndex(SQLiteDatabase db, String key, String temp) {
        ContentValues values = new ContentValues();
        values.put(Constants.IndexTable.TEMP, temp);
        values.put(Constants.IndexTable.KEY, key);
        return db.update(LocalDataDbHelper.T_INDEX, values, null, null);
    }

    /*query*/
    public static Cursor queryContent(SQLiteDatabase db, String key, String phone) {
        Cursor query = db.query(LocalDataDbHelper.T_CONTENT,
                new String[]{Constants.ContentTable.VALUE, Constants.ContentTable._MD5},
                Constants.ContentTable.KEY.concat(" = ? and ").concat(Constants.ContentTable.PHONE).concat(" = ? "),
                new String[]{key, phone},
                null, null, null);
        return query;
    }

    public static Cursor queryIndex(SQLiteDatabase db) {
        Cursor query = db.query(LocalDataDbHelper.T_INDEX,
                new String[]{Constants.IndexTable.KEY, Constants.IndexTable.TEMP},
                null, null,
                null, null, null);
        return query;
    }
}
