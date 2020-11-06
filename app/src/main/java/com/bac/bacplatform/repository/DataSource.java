package com.bac.bacplatform.repository;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.commonlib.domain.BacHttpBean;

import rx.Observable;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.repository
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * 数据处理的接口
 */

public interface DataSource {
    // 请求数据
    Observable<String> getData(BacHttpBean bean, boolean refresh, AppCompatActivity activity,
                               DialogInterface.OnClickListener positive,
                               DialogInterface.OnClickListener negative,
                               DialogInterface.OnCancelListener cancel);
}
