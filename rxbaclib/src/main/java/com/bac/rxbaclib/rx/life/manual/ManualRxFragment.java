package com.bac.rxbaclib.rx.life.manual;

import androidx.fragment.app.Fragment;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wjz on 2017/4/21.
 */

public abstract class ManualRxFragment extends Fragment {
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public CompositeSubscription getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }
}
