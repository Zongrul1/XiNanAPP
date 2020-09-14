package com.example.xinan.Subscriber;

import java.io.IOException;

import rx.Subscriber;

public interface RxSubscriber<T>  {
    void onNext(T t) throws IOException;
}
