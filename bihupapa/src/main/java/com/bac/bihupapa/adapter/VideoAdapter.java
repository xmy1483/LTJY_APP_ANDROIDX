package com.bac.bihupapa.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bac.bihupapa.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Map;

import static com.bac.bihupapa.util.UIUtils.callbackBitmap_4;


/**
 * Created by Wjz on 2017/5/17.
 */

public class VideoAdapter extends BaseQuickAdapter<Map<String, Object>,BaseViewHolder> {

    public VideoAdapter(int layoutResId, List<Map<String, Object>> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Map<String, Object> map) {
        baseViewHolder.setText(R.id.tv, String.valueOf(map.get("image_name")))
                .addOnClickListener(R.id.btn)
                .addOnClickListener(R.id.iv);
        ImageView iv = baseViewHolder.getView(R.id.iv);

        if (map.get("path") != null) {
            Bitmap bitmap = callbackBitmap_4(map.get("path").toString());
            if (bitmap != null) {
                iv.setImageBitmap(bitmap);
            }
        }
    }
}