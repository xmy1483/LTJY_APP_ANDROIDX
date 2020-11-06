package com.bac.bacplatform.module.kaiyoubao.presenter;

import android.util.SparseArray;

import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.bacplatform.module.kaiyoubao.adapter.KaiYouBaoBean;
import com.bac.bacplatform.module.kaiyoubao.contract.KaiYouBaoContract;
import com.bac.bacplatform.module.kaiyoubao.view.KaiYouBaoFragment;

import java.util.ArrayList;


/**
 * Created by maibenben on 2017/05/04
 */

public class KaiYouBaoPresenterImpl implements KaiYouBaoContract.Presenter {

    private final KaiYouBaoFragment view;
    private final KaiYouBaoContract.Model model;

    public KaiYouBaoPresenterImpl(KaiYouBaoFragment view, KaiYouBaoContract.Model model) {

        this.view =view;
        this.model = model;

        view.setPresenter(this);
    }



    @Override
    public void loadData(BacHttpBean bean, boolean refreshData, boolean showLoading) {

    }

    @Override
    public void loadData() {
        // 加载数据
        ArrayList<KaiYouBaoBean> arrayList = new ArrayList<>();
        SparseArray<String> headerSA = new SparseArray<>();
        headerSA.put(R.id.tv_01, "最近交易记录");
        headerSA.put(R.id.tv_02, "查看明细");
        arrayList.add(new KaiYouBaoBean(Constants.Adapter.HEADER, headerSA));

        SparseArray<String> contextSA = new SparseArray<>();
        contextSA.put(R.id.tv_01, "XXXXXXXXXXXXX");
        contextSA.put(R.id.tv_11, "XX-XX");
        contextSA.put(R.id.tv_02, "+XX");
        arrayList.add(new KaiYouBaoBean(Constants.Adapter.CONTEXT, contextSA));
        arrayList.add(new KaiYouBaoBean(Constants.Adapter.CONTEXT, contextSA));
        arrayList.add(new KaiYouBaoBean(Constants.Adapter.CONTEXT, contextSA));
        arrayList.add(new KaiYouBaoBean(Constants.Adapter.CONTEXT, contextSA));
        arrayList.add(new KaiYouBaoBean(Constants.Adapter.CONTEXT, contextSA));

        view.showData(arrayList);
    }
}