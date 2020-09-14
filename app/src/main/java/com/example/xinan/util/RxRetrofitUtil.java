package com.example.xinan.util;

import com.example.xinan.Service.RetrofitService;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;


//单例
public class RxRetrofitUtil {
    private static String baseUrl = "https://xnxz.top/wc/";
    private static RetrofitService retrofitService;
    private RxRetrofitUtil(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    /*
    *内部类方式建立单例
     */
    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RxRetrofitUtil INSTANCE = new RxRetrofitUtil();
    }

    //获取单例
    public static RxRetrofitUtil getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void requestIndex(Subscriber<retrofit2.Response<ResponseBody>> subscriber, String address) {
        Observable<retrofit2.Response<ResponseBody>> observable = null;
        switch(address){
            case "login":
                RequestBody formBody = new FormBody.Builder()
                        .add("account", "test")
                        .add("password", "test")
                        .build();
                observable = retrofitService.getRxCookie(formBody);;
                break;
            case "switch":
                observable = retrofitService.getRxNews();
                break;
        }
        if(observable != null) toSubscribe(observable,subscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

//    /**
//     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
//     *
//     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
//     */
//    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{
//
//        @Override
//        public T call(HttpResult<T> httpResult) {
//            if (httpResult.getCount() == 0) {
//                throw new ApiException(100);
//            }
//            return httpResult.getSubjects();
//        }
//    }
}
