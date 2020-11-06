package com.bac.rxbaclib.rx.life.automatic.exceptions;

import androidx.annotation.Nullable;

public class OutsideLifecycleException extends IllegalStateException {

    public OutsideLifecycleException(@Nullable String detailMessage) {
        super(detailMessage);
    }
}
