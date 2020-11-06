package com.bac.rxbaclib.rx.life.automatic.components;

import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bac.rxbaclib.rx.life.automatic.RxLifecycleAndroid;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.life.automatic.transformers.LifecycleTransformer;
import com.bac.rxbaclib.rx.life.automatic.transformers.RxLifecycle;

import rx.subjects.BehaviorSubject;

public abstract class AutomaticRxAppCompatActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {

    // 保存生命周期的订阅
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();


    /**
     * 指定事件
     */
    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    /**
     * 绑定生命周期
     * @param <T>
     * @return
     */
    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        // Android life
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    /*activity的生命周期*/

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }
}
