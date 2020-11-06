package com.bac.commonlib.capture.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

import com.bac.commonlib.capture.ClipImageBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by wujiazhen on 2017/6/26.
 */
public class ClipImageLayout extends RelativeLayout {
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 0;

    /**
     * 初始化
     *
     * @param context
     */
    public ClipImageLayout(Context context, ClipImageBean param) {
        super(context);
        mZoomImageView = new ClipZoomImageView(context, param);
        mClipImageView = new ClipImageBorderView(context, param);
        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        /**
         * 使用Glide 加载图片
         */
        Glide.with(context)
                .load(param.getUrl())
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mZoomImageView);
        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

}
