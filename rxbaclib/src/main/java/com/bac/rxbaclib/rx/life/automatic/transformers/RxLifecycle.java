package com.bac.rxbaclib.rx.life.automatic.transformers;


import rx.Observable;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public class RxLifecycle {

    private RxLifecycle() {
        throw new AssertionError("No instances");
    }

    /**
     * 指定事件
     */
    public static <T, R> LifecycleTransformer<T> bindUntilEvent(final Observable<R> lifecycle,
                                                                final R event) {
        checkNotNull(lifecycle, "lifecycle == null");
        checkNotNull(event, "event == null");

        return new UntilEventObservableTransformer<>(lifecycle, event);
    }

    /**
     * rx android 中使用
     * Corresponding 相应
     */
    public static <T, R> LifecycleTransformer<T> bind(Observable<R> lifecycle,
                                                       final Func1<R, R> correspondingEvents) {
        checkNotNull(lifecycle, "lifecycle == null");
        checkNotNull(correspondingEvents, "correspondingEvents == null");
        // share() 将一个"冷"的Observable变为"热"的Observable
        return new UntilCorrespondingEventObservableTransformer<>(lifecycle.share(), correspondingEvents);
    }
}
