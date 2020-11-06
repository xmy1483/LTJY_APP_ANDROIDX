package com.bac.rxbaclib.rx.life.automatic.transformers;

import rx.Observable;


import static com.bac.rxbaclib.rx.life.automatic.transformers.TakeUntilGenerator.takeUntilEvent;

final class UntilEventObservableTransformer<T, R> implements LifecycleTransformer<T> {

    final Observable<R> lifecycle;
    final R event;

    public UntilEventObservableTransformer(Observable<R> lifecycle, R event) {
        this.lifecycle = lifecycle;
        this.event = event;
    }

    @Override
    public Observable<T> call(Observable<T> source) {
        // 终止发射的关键方法
        // 当第二个Observable发射了一项数据或者终止时，丢弃原始Observable发射的任何数据
        return source.takeUntil(takeUntilEvent(lifecycle, event));
    }
}
