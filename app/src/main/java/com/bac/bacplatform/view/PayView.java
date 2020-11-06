package com.bac.bacplatform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bac.bacplatform.R;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.view
 * 创建人：Wjz
 * 创建时间：2016/12/1
 * 类描述：
 */

public class PayView extends RelativeLayout {

    private ImageView ivPay;
    private TextView tvPay;
    private LinearLayout llPay;
    private int          mResourceId;
    private String mTextStr;
    private boolean      mABoolean;
    private View mView;
    private LinearLayout mLlPay2;

    public PayView(Context context) {
        this(context, null);
    }

    public PayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
        initData(attrs);
        initEvent();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initView() {
        //绑定资源文件
        mView = View.inflate(getContext(), R.layout.view_pay_layout, this);

        ivPay = (ImageView) mView.findViewById(R.id.iv_pay);
        tvPay = (TextView) mView.findViewById(R.id.tv_pay);
        llPay = (LinearLayout) mView.findViewById(R.id.ll_pay);
        mLlPay2 = (LinearLayout) mView.findViewById(R.id.ll_pay_2);

    }

    private void initData(AttributeSet attrs) {
        // Get attributes
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.PayLayout);

        mResourceId = a.getResourceId(R.styleable.PayLayout_imgView, R.mipmap.weixin2);
        mTextStr = a.getString(R.styleable.PayLayout_textStr);

        a.recycle();

        ivPay.setImageResource(mResourceId);
        tvPay.setText(mTextStr);

    }

    public void setLabelText(String mTextStr){
        tvPay.setText(mTextStr);
    }

    public void setViewGone(boolean b){
        llPay.setVisibility(b? View.GONE: View.VISIBLE);
        mLlPay2.setVisibility(b? View.VISIBLE: View.GONE);
    }

    private void initEvent() {

    }

}
