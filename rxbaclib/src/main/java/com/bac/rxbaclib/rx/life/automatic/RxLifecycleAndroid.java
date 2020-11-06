

package com.bac.rxbaclib.rx.life.automatic;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.life.automatic.enums.FragmentEvent;
import com.bac.rxbaclib.rx.life.automatic.exceptions.OutsideLifecycleException;
import com.bac.rxbaclib.rx.life.automatic.transformers.LifecycleTransformer;

import rx.Observable;
import rx.functions.Func1;

import static com.bac.rxbaclib.rx.life.automatic.transformers.RxLifecycle.bind;


public class RxLifecycleAndroid {


    private RxLifecycleAndroid() {
        throw new AssertionError("No instances");
    }

    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bindActivity(@NonNull final Observable<ActivityEvent> lifecycle) {
        return bind(lifecycle, ACTIVITY_LIFECYCLE);
    }
    
    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bindFragment(@NonNull final Observable<FragmentEvent> lifecycle) {
        return bind(lifecycle, FRAGMENT_LIFECYCLE);
    }
    
    private static final Func1<ActivityEvent, ActivityEvent> ACTIVITY_LIFECYCLE =
        new Func1<ActivityEvent, ActivityEvent>() {
            @Override
            public ActivityEvent call(ActivityEvent lastEvent) {
                switch (lastEvent) {
                    case CREATE:
                        return ActivityEvent.DESTROY;
                    case START:
                        return ActivityEvent.STOP;
                    case RESUME:
                        return ActivityEvent.PAUSE;
                    case PAUSE:
                        return ActivityEvent.STOP;
                    case STOP:
                        return ActivityEvent.DESTROY;
                    case DESTROY:
                        throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
                    default:
                        throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                }
            }
        };

    
    private static final Func1<FragmentEvent, FragmentEvent> FRAGMENT_LIFECYCLE =
        new Func1<FragmentEvent, FragmentEvent>() {
            @Override
            public FragmentEvent call(
               FragmentEvent lastEvent) {
                switch (lastEvent) {
                    case ATTACH:
                        return FragmentEvent.DETACH;
                    case CREATE:
                        return FragmentEvent.DESTROY;
                    case CREATE_VIEW:
                        return FragmentEvent.DESTROY_VIEW;
                    case START:
                        return FragmentEvent.STOP;
                    case RESUME:
                        return FragmentEvent.PAUSE;
                    case PAUSE:
                        return FragmentEvent.STOP;
                    case STOP:
                        return FragmentEvent.DESTROY_VIEW;
                    case DESTROY_VIEW:
                        return FragmentEvent.DESTROY;
                    case DESTROY:
                        return FragmentEvent.DETACH;
                    case DETACH:
                        throw new OutsideLifecycleException("Cannot bind to Fragment lifecycle when outside of it.");
                    default:
                        throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                }
            }
        };

}
