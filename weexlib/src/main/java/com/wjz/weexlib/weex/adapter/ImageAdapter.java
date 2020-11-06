package com.wjz.weexlib.weex.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * Created by wujiazhen on 2017/6/12.
 */

public class ImageAdapter implements IWXImgLoaderAdapter {
    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        Log.d("loglog1483", "setImage: "+url);
        Glide.with(WXEnvironment.getApplication())
                .load(url)
                .centerCrop()
                .into(view);
    }
}
