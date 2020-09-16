package com.example.xinan.util;

import com.example.xinan.Service.RetrofitService;
import com.example.xinan.Service.RxService;
import com.example.xinan.db.Content;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private static RxService rxService;
    private RxRetrofitUtil(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        rxService = retrofit.create(RxService.class);
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
                observable = rxService.getCookie(formBody);
                break;
            case "switch":
                observable = rxService.getNews();
                break;
        }
        if(observable != null) {toSubscribe(observable,subscriber);}
    }

    public void requestContent(Subscriber<retrofit2.Response<ResponseBody>> subscriber,String type,String page,String search) {
        Observable<retrofit2.Response<ResponseBody>> observable = rxService.getContent(XinanApplication.getCookie(),type,page,search);
        if(observable != null) toSubscribe(observable,subscriber);
    }

    public void requestDetail(Subscriber<retrofit2.Response<ResponseBody>> subscriber,String id){
        Observable<retrofit2.Response<ResponseBody>> observable = rxService.getDetail(XinanApplication.getCookie(),id);
        if(observable != null) toSubscribe(observable,subscriber);
    }

    public void sendPIC(Subscriber<retrofit2.Response<ResponseBody>> subscriber,File file){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pic", file.getName(), requestBody)
                .build();
        Observable<retrofit2.Response<ResponseBody>> observable = rxService.sendPic(XinanApplication.getCookie(),multipartBody);
        if(observable != null) toSubscribe(observable,subscriber);
    }

    public void sendContent(Subscriber<retrofit2.Response<ResponseBody>> subscriber,final Content con){
        RequestBody formBody = new FormBody.Builder()
                .add("title", con.getTitle())
                .add("price", String.valueOf(con.getPrice()))
                .add("desc", con.getDescription())
                .add("tag", con.getTag())
                .add("pic", con.getPic())
                .add("name", con.getName())
                .add("contact_type", String.valueOf(con.getContactType()))
                .add("contact", con.getContact())
                .add("type", String.valueOf(con.getType()))
                .build();
        Observable<retrofit2.Response<ResponseBody>> observable = rxService.sendContent(XinanApplication.getCookie(),formBody);
        if(observable != null) toSubscribe(observable,subscriber);
    }

    public void requestPIC(Subscriber<retrofit2.Response<ResponseBody>> subscriber,String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://img.xnxz.top/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        rxService = retrofit.create(RxService.class);
        Observable<retrofit2.Response<ResponseBody>> observable = rxService.getPIC(url);
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
