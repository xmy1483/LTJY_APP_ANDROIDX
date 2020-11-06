package com.bac.bacplatform.old.module.cards;


import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseFragment;
import com.bac.bacplatform.old.base.SuperActivity;
import com.bac.bacplatform.old.module.cards.fragment.active.FragmentCardActive2;
import com.jakewharton.rxbinding.view.RxView;

import rx.functions.Action1;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.activity.card
 * 创建人：Wjz
 * 创建时间：2016/10/25
 * 类描述：
 */

public class ActivityCardsPhone extends SuperActivity implements View.OnClickListener {

    private TextView tvLeft;
    //private TextView tvRight;
   // private FragmentCardEnable mFragmentCardEnable;
    private FragmentCardActive2 mFragmentCardActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_cards_activity);

        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(this);
//        tvRight = (TextView) findViewById(R.id.tv_right);
//        tvRight.setOnClickListener(this);

        selectedColorAndBg(tvLeft);
        initData();
        initEvent();
    }


    private void initData() {

        Bundle bundle = new Bundle();

        bundle.putString("login_phone", BacApplication.getLoginPhone());
        //2个Fragment
        mFragmentCardActive = new FragmentCardActive2();
        mFragmentCardActive.setArguments(bundle);
//        mFragmentCardEnable = new FragmentCardEnable();
//        mFragmentCardEnable.setArguments(bundle);
        selectedFragment(mFragmentCardActive);
    }

    private void selectedFragment(AutomaticBaseFragment fm) {

        FragmentManager fragmentManager     = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_cards, fm);
        fragmentTransaction.commit();

    }

    private void initEvent() {
        RxView.clicks(findViewById(R.id.iv)).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ActivityCardsPhone.this.onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                restColorAndBg();
                selectedColorAndBg(tvLeft);
                selectedFragment(mFragmentCardActive);
                break;
//            case R.id.tv_right:
//                restColorAndBg();
//                selectedColorAndBg(tvRight);
//                selectedFragment(mFragmentCardEnable);
//                break;
        }
    }

    public void backLeft(){
        restColorAndBg();
        selectedColorAndBg(tvLeft);
        selectedFragment(mFragmentCardActive);
    }

    private void selectedColorAndBg(TextView tv) {
//        tv.setBackgroundColor(getResources().getColor(R.color.white));
//        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void restColorAndBg() {
       // tvLeft.setBackgroundColor(getResources().getColor(R.color.gray_8f));
       // tvRight.setBackgroundColor(getResources().getColor(R.color.gray_8f));
        //tvLeft.setTextColor(getResources().getColor(R.color.white));
       // tvRight.setTextColor(getResources().getColor(R.color.white));
    }


}
