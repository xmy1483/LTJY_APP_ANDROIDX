package com.bac.bacplatform.activity.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bac.bacplatform.R;

/**
 * 不想用原来的，过于,
 */
public abstract class SimpleBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        initDataBeforeBindView();
        bindView();
        initDataAfterBindView();
        attachViewListener();
    }

    public void initDataBeforeBindView(){} // 在view没初始化之前处理页面数据
    public void initDataAfterBindView(){} // 在。。。。。。。后。。。。。。。
    public abstract int setLayoutId(); // 指定页面的布局文件
    public abstract void bindView(); // 初始化view相关
    public void attachViewListener(){} // 关联view的各种listener

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);
    }
}
