package com.bac.rxbaclib.rx.life.automatic.transformers;


import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 处理生命周期
 */
final class TakeUntilGenerator {

    static <T> Observable<T> takeUntilEvent(final Observable<T> lifecycle, final T event) {
        return lifecycle
                // 只发射第一项数据，或者满足某种条件的第一项数据
                .takeFirst(new Func1<T, Boolean>() {
                    @Override
                    public Boolean call(T lifecycleEvent) {
                        //判断是否满足条件
                        return lifecycleEvent.equals(event);
                    }
                });
    }

    /**
     * 处理相应的事件
     *
     * @param lifecycle
     * @param correspondingEvents
     * @param <T>
     * @return
     */
    static <T> Observable<Boolean> takeUntilCorrespondingEvent(final Observable<T> lifecycle,
                                                               final Func1<T, T> correspondingEvents) {

        /*
        *   当两个Observables中的任何一个发射了一个数据时，通过一个指
            定的函数组合每个Observable发射的最新数据（ 一共两个数据） ，然后发射这个函数的
            结果
        * */
        return Observable.combineLatest(
                lifecycle.take(1).map(correspondingEvents),// 转换订阅时的Events
                lifecycle.skip(1),// 与之后生命周期改变后的Events
                new Func2<T, T, Boolean>() {
                    @Override
                    public Boolean call(T bindUntilEvent, T lifecycleEvent) {
                        return lifecycleEvent.equals(bindUntilEvent);// 比较是否一致
                    }
                })
                // 换时的错误
                .onErrorReturn(Functions.RESUME_FUNCTION)// 指示Observable在遇到错误时发射一个特定的数据
                // 判断时候停止
                .takeFirst(Functions.SHOULD_COMPLETE);
    }

    private TakeUntilGenerator() {
        throw new AssertionError("No instances!");
    }
}
