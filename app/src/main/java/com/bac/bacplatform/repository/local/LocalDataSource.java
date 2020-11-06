package com.bac.bacplatform.repository.local;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.repository.DataSource;

import rx.Observable;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.repository.local
 * 创建人：Wjz
 * 创建时间：2017/4/7
 * 类描述：
 * 本地缓存数据，将远程数据缓存至本地
 */

public class LocalDataSource implements DataSource{


    public void saveData() {

        // 将数据保存到数据库
    }


    @Override
    public Observable<String> getData(BacHttpBean bean, boolean refresh, AppCompatActivity activity, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative, DialogInterface.OnCancelListener cancel) {
        return null;
    }
}