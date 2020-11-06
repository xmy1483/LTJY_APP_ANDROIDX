package com.bac.bacplatform.module.center.adapter;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bac.bacplatform.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;

/**
 * Created by Wjz on 2017/5/4.
 */

public class UserCenterAdapter extends BaseSectionQuickAdapter<UserCenterSectionBean, BaseViewHolder> {


    public UserCenterAdapter(int layoutResId, int sectionHeadResId, List<UserCenterSectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, UserCenterSectionBean userCenterSectionBean) {
        baseViewHolder.setText(R.id.tv_01, userCenterSectionBean.header);


        String more = userCenterSectionBean.getMore();

        if (TextUtils.isEmpty(more)) {
            baseViewHolder.setVisible(R.id.tv_02, false);
        } else {
            baseViewHolder.setVisible(R.id.tv_02, true)
                    .setText(R.id.tv_02, more);
        }
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserCenterSectionBean userCenterSectionBean) {
        UserCenterSectionInnerBean innerBean = userCenterSectionBean.t;

        if (innerBean.getIndex() > _0) {

            Glide.with(baseViewHolder.itemView.getContext())
                    .load(innerBean.getLabel_1())
                    .override(90,90)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_01));

            baseViewHolder.setText(R.id.tv_01,innerBean.getLabel_2());

        } else {
            baseViewHolder.setImageResource(R.id.iv_01, innerBean.getId());
        }


        StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) baseViewHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(innerBean.isFull());


    }
}
