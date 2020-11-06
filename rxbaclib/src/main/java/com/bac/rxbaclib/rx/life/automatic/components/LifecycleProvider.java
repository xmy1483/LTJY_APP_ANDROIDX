package com.bac.rxbaclib.rx.life.automatic.components;


import androidx.annotation.NonNull;

import com.bac.rxbaclib.rx.life.automatic.transformers.LifecycleTransformer;

public interface LifecycleProvider<E> {

    @NonNull
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull E event);

    @NonNull
    <T> LifecycleTransformer<T> bindToLifecycle();
}
