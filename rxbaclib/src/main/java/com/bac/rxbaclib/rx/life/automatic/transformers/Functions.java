package com.bac.rxbaclib.rx.life.automatic.transformers;

import com.bac.rxbaclib.rx.life.automatic.exceptions.OutsideLifecycleException;

import java.util.concurrent.CancellationException;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

final class Functions {

    static final Func1<Throwable, Boolean> RESUME_FUNCTION = new Func1<Throwable, Boolean>() {
        @Override
        public Boolean call(Throwable throwable) {
            if (throwable instanceof OutsideLifecycleException) {
                return true;
            }
            Exceptions.propagate(throwable);
            return false;
        }
    };

    static final Func1<Boolean, Boolean> SHOULD_COMPLETE = new Func1<Boolean, Boolean>() {
        @Override
        public Boolean call(Boolean shouldComplete) {
            return shouldComplete;
        }
    };

    static final Func1<Object, Observable<Object>> CANCEL_COMPLETABLE = new Func1<Object, Observable<Object>>() {
        @Override
        public Observable<Object> call(Object ignore) {
            return Observable.error(new CancellationException());
        }
    };

    private Functions() {
        throw new AssertionError("No instances!");
    }
}
