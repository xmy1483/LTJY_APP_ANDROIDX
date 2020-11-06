package com.bac.bacplatform.module.kaiyoubao.adapter;

import android.util.SparseArray;
import android.view.View;

import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Wjz on 2017/5/4.
 */

public class KaiYouBaoAdapter extends BaseMultiItemQuickAdapter<KaiYouBaoBean, BaseViewHolder> {
    private View itemView;

    public KaiYouBaoAdapter(List<KaiYouBaoBean> data) {
        super(data);
        addItemType(Constants.Adapter.HEADER, R.layout.kaiyoubao_item_header);
        addItemType(Constants.Adapter.CONTEXT, R.layout.kaiyoubao_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, KaiYouBaoBean kaiYouBaoBean) {
        SparseArray<String> sparseArray = null;
        switch (kaiYouBaoBean.getItemType()) {
            case Constants.Adapter.HEADER:
                sparseArray = kaiYouBaoBean.getSparseArray();
                break;

            case Constants.Adapter.CONTEXT:
                sparseArray = kaiYouBaoBean.getSparseArray();
                baseViewHolder.setText(R.id.tv_11, sparseArray.get(R.id.tv_11));
                break;
        }
        baseViewHolder.setText(R.id.tv_01, sparseArray.get(R.id.tv_01))
                .setText(R.id.tv_02, sparseArray.get(R.id.tv_02));
    }


}
