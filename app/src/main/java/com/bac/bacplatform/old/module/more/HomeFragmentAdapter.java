package com.bac.bacplatform.old.module.more;

import com.bac.bacplatform.R;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 项目名称：BacPlatform
 * 包名：com.bac.bacplatform.adapter
 * 创建人：Wjz
 * 创建时间：2016/9/12
 * 类描述：
 */
public class HomeFragmentAdapter extends BaseItemDraggableAdapter<TitleAndIconBean,BaseViewHolder> {
    public HomeFragmentAdapter(int layoutResId, List<TitleAndIconBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TitleAndIconBean titleAndIconBean) {
        baseViewHolder.setImageResource(R.id.iv_home_fragment_icon, titleAndIconBean.getImg());
    }
}
