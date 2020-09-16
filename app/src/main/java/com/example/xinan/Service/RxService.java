package com.example.xinan.Service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface RxService {
    @POST("login")
    Observable<Response<ResponseBody>> getCookie(@Body RequestBody body);

    @POST("upload")
    Observable<retrofit2.Response<ResponseBody>> sendPic(@Header("Cookie") String cookie, @Body MultipartBody body);

    @POST("post")
    Observable<retrofit2.Response<ResponseBody>>sendContent(@Header("Cookie") String cookie,@Body RequestBody body);

    @GET("switch")
    Observable<retrofit2.Response<ResponseBody>> getNews();

    @GET("getCard")
    Observable<retrofit2.Response<ResponseBody>>getContent(@Header("Cookie") String cookie, @Query("type") String type, @Query("page") String page, @Query("search") String search);

    @GET("getDetail")
    Observable<retrofit2.Response<ResponseBody>> getDetail(@Header("Cookie") String cookie,@Query("id") String id);

    @GET
    Observable<retrofit2.Response<ResponseBody>> getPIC(@Url String url);
}
