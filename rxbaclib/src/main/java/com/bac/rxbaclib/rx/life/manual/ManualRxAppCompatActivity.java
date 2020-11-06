package com.bac.rxbaclib.rx.life.manual;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.extended.sup
 * 创建人：Wjz
 * 创建时间：2017/4/19
 * 类描述：
 * 处理rx 的生命周期回收
 */

public abstract class ManualRxAppCompatActivity extends AppCompatActivity {
    // 非静态 rx 管理类
    private CompositeSubscription subscriptions = new CompositeSubscription();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    public CompositeSubscription getCompositeSubscription() {
        return subscriptions;
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }
}
