package com.bac.bacplatform.module.main.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bac.bacplatform.R;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    public NetworkImageHolderView() {
    }

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context).load(data).placeholder(R.mipmap.banner_default).into(imageView);
    }
}
