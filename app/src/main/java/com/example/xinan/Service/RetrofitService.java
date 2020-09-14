package com.example.xinan.Service;

import java.io.InputStream;
import rx.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitService {
    //retrofit原生
    @POST("login")
    Call<ResponseBody> getCookie(@Body RequestBody body);

    @POST("upload")
    Call<ResponseBody> sendPic(@Header("Cookie") String cookie,@Body MultipartBody body);

    @POST("post")
    Call<ResponseBody> sendContent(@Header("Cookie") String cookie,@Body RequestBody body);

    @GET("switch")
    Call<ResponseBody> getNews();

    @GET("getCard")
    Call<ResponseBody> getContent(@Header("Cookie") String cookie,@Query("type") String type,@Query("page") String page,@Query("search") String search);

    @GET("getDetail")
    Call<ResponseBody> getDetail(@Header("Cookie") String cookie,@Query("id") String id);

    @GET
    Call<ResponseBody> getPIC(@Url String url);

    //rx
    @POST("login")
    Observable<retrofit2.Response<ResponseBody>> getRxCookie(@Body RequestBody body);

    @GET("switch")
    Observable<retrofit2.Response<ResponseBody>> getRxNews();
}
