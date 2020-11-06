package com.bac.rxbaclib.rx.rxbus;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * 项目名称：wjz-RxJava
 * 包名：com.wjz.www.wjz_rxjava.rxbus
 * 创建人：Wjz
 * 创建时间：2016/12/14
 * 类描述：
 */

public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    public static RxBus get() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private RxBus() {
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (subjectList == null) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T, T> subject = PublishSubject.create();
        subjectList.add(subject);
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (subjects != null) {
            if (observable != null && subjects.contains(observable)) {
                subjects.remove(observable);
            } else {
                subjects.clear();
            }
            // 集合为空从map中请出
            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
            }
        }

    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (!isEmpty(subjectList)) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }

    private boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

}
