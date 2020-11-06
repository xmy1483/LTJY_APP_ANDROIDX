package com.bac.rxbaclib.rx.life.automatic.transformers;

import rx.Observable;

public interface LifecycleTransformer<T> extends Observable.Transformer<T, T> {

}
