package com.bac.bacplatform.module.bills;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.bacplatform.module.bills.adapter.QueryKaiYouBaoAdapter;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.bac.bacplatform.conf.Constants.CommonProperty._0;
import static com.bac.bacplatform.conf.Constants.CommonProperty._1;
import static com.bac.bacplatform.conf.Constants.CommonProperty._10;

/**
 * Created by Wjz on 2017/5/25.
 */

public class QueryKaiYouBaoActivity extends AutomaticBaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView rv;
    private QueryKaiYouBaoAdapter adapter;
    private int currentCount;
    private int PAGE_NUM = _1;
    private int PAGE_SIZE = _10;
    private boolean isEnd;

    @Override
    protected void initView() {

        setContentView(R.layout.layout_rv_toolbar);

        initToolBar("揩油宝");
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new QueryKaiYouBaoAdapter(R.layout.kaiyoubao_item);

        adapter.setOnLoadMoreListener(this, rv);
        currentCount = adapter.getData().size();
        rv.setAdapter(adapter);

        CARD_XML_QUERY_OIL_ORDER(true);

    }

    private void CARD_XML_QUERY_OIL_ORDER(final boolean b) {
        HttpHelper.getInstance().bacNetWithContext(this, new BacHttpBean()
                .setMethodName("CARD_XML.QUERY_ACCOUNT_BILL")
                .put("PAGE_NUM", PAGE_NUM)
                .put("PAGE_SIZE", PAGE_SIZE))
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(new Observable.Transformer<String, String>() {
                    @Override
                    public Observable<String> call(Observable<String> stringObservable) {

                        if (b) {
                            return stringObservable.compose(new RxDialog<String>().rxDialog(QueryKaiYouBaoActivity.this));
                        } else {
                            return stringObservable;
                        }

                    }
                })
                .observeOn(RxScheduler.RxPoolScheduler())
                .map(new JsonFunc1<String, List<Map<String, Object>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Map<String, Object>>>() {
                    @Override
                    public void call(List<Map<String, Object>> mapList) {

                        if (mapList.size() > _0) {
                            adapter.addData(mapList);
                            isEnd = false;
                        } else {
                            isEnd = true;
                        }
                        adapter.loadMoreComplete();
                    }
                });
    }

    @Override
    protected void initFragment() {

    }

    @Override
    public void onLoadMoreRequested() {

        if (!isEnd) {
            PAGE_NUM++;
            CARD_XML_QUERY_OIL_ORDER(false);
        } else {
            adapter.loadMoreEnd(true);
        }

    }

}
