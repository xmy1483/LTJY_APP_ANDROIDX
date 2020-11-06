package com.bac.bacplatform.old.module.insurance;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.insurance.dao.InsuranceDataBaseDao;
import com.bac.bacplatform.repository.database.LocalDataDbHelper;

import java.util.Date;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.base
 * 创建人：Wjz
 * 创建时间：2016/8/31
 * 类描述：
 */
public class InsuranceActivity extends SuperActivity {

    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;
    protected LocalDataDbHelper localDataDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localDataDbHelper = ((BacApplication) getApplicationContext()).getLocalDataDbHelper();
        initAbleDatabase();
        //refreshIndex(); // 刷新最后退出的activity
    }

    protected void initAbleDatabase() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseAbleDatabase();
    }

    private void releaseAbleDatabase() {
        if (readableDatabase!=null) {
            readableDatabase.close();
        }
        if (writableDatabase!=null) {
            writableDatabase.close();
        }
    }

    protected void refreshIndex() {
        //mDbDao.addIndex(this.getClass().getName(), String.valueOf(new Date().getTime()));
        InsuranceDataBaseDao.replaceIndex(writableDatabase, 1, this.getClass().getName(), String.valueOf(new Date().getTime()));
        queryLatestActivity();
    }

    private void queryLatestActivity() {
        Cursor cursor = InsuranceDataBaseDao.queryIndex(readableDatabase);
        while (cursor.moveToNext()) {
            String index = cursor.getString(cursor.getColumnIndex(Constants.IndexTable.KEY));
            String temp = cursor.getString(cursor.getColumnIndex(Constants.IndexTable.TEMP));
        }
        cursor.close();
    }

    protected void gotoMainActivty() {
        //返回主页
        Intent intent = new Intent(InsuranceActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);
    }


}
