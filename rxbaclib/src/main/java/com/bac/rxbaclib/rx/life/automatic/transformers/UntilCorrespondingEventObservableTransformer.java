package com.bac.rxbaclib.rx.life.automatic.transformers;

import rx.Observable;
import rx.functions.Func1;

import static com.bac.rxbaclib.rx.life.automatic.transformers.TakeUntilGenerator.takeUntilCorrespondingEvent;


final class UntilCorrespondingEventObservableTransformer<T, R> implements LifecycleTransformer<T> {

    final Observable<R> sharedLifecycle;
    final Func1<R, R> correspondingEvents;

    public UntilCorrespondingEventObservableTransformer(Observable<R> sharedLifecycle,
                                                        Func1<R, R> correspondingEvents) {
        this.sharedLifecycle = sharedLifecycle;
        this.correspondingEvents = correspondingEvents;
    }

    @Override
    public Observable<T> call(Observable<T> source) {
        return source.takeUntil(takeUntilCorrespondingEvent(sharedLifecycle, correspondingEvents));
    }
}
