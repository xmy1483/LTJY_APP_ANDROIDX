package com.bac.bacplatform.extended.base.components;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.module.main.MainActivity;
import com.bac.bacplatform.service.SecureService;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;

/**
 * 项目名称：Bacplatform
 * 包名：com.bac.bacplatform.base
 * 创建人：Wjz
 * 创建时间：2017/4/12
 * 类描述：
 * activity的封装类
 * 抽取公共方法
 * 每个页面都是由托管activity下的fragment完成
 * 提供重写父类的方法
 */

public abstract class AutomaticBaseActivity extends AutomaticRxAppCompatActivity {

    //private static final List<AutomaticBaseActivity> sAppList = new ArrayList<>();
    private static long sPreClickTime;
    protected AutomaticBaseActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        //sAppList.add(this);
        // 初始化toolbar
        //initStatusBarColor();
        initView();

        initFragment();


        // fragment 是否被销毁
        // Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    /**
     * 处理toolbar
     *
     * @param s
     * @param listener
     * @return
     */
    protected TextView initToolBar(String s, View.OnClickListener listener) {
        return UIUtils.initToolBar(this, s, listener);
    }

    protected TextView initToolBar(String s) {
        return initToolBar(s, null);
    }


    protected abstract void initView();

    protected abstract void initFragment();

    /**
     * 处理StatusBar
     */
    protected void initStatusBarColor() {
        // StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(new Intent(this, SecureService.class));
//        } else {
            startService(new Intent(this, SecureService.class));
//        }
    }

    @Override
    protected void onDestroy() {
        //sAppList.remove(this);
        super.onDestroy();
    }

    /**
     * 完全退出
     */
    private void exit() {
        /*for (AutomaticBaseActivity automaticBaseActivity : sAppList) {
            automaticBaseActivity.finish();
        }*/
    }

    /**
     * 首页双击返回
     */
    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {// 主页
            if (System.currentTimeMillis() - sPreClickTime > 2000) {// 两次点击的间隔大于2s中
                Toast.makeText(getApplicationContext(), R.string.base_exit, Toast.LENGTH_SHORT).show();
                sPreClickTime = System.currentTimeMillis();
                return;
            } else {
                // 完全退出
                // exit();
                this.finish();
            }
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.cl_slide_left_in, R.anim.cl_slide_right_out);
        }
    }


}
