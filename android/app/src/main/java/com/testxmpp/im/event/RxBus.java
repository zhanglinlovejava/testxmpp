package com.testxmpp.im.event;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {
        private static RxBus defaultInstance;
    // 主题
    private final Subject bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    // 单例RxBus
    public static RxBus getDefault() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance;
    }

    // 提供了一个新的事件
    public void post(Object o) {
        bus.onNext(o);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T extends Object> Observable<T> toObserverable(final Class<T> eventType) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }




//    private static volatile RxBus mDefaultInstance;
//
//    private RxBus() {
//    }
//
//    public static RxBus getDefault() {
//        if (mDefaultInstance == null) {
//            synchronized (RxBus.class) {
//                if (mDefaultInstance == null) {
//                    mDefaultInstance = new RxBus();
//                }
//            }
//        }
//        return mDefaultInstance;
//    }
//
//    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
//
//    public void post(Object o) {
//        _bus.onNext(o);
//    }
//
//    public Observable<Object> toObserverable() {
//        return _bus;
//    }
}