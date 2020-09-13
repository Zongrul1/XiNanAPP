package com.example.xinan.util;

import com.example.xinan.Service.RetrofitService;
import com.example.xinan.db.Content;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static String baseUrl = "https://xnxz.top/wc/";
    private static RetrofitService retrofitService;
    private static void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
       retrofitService = retrofit.create(RetrofitService.class);
    }
    public static void requestIndex(String address,retrofit2.Callback callback) {
        init();
        switch(address){
            case "login":
                RequestBody formBody = new FormBody.Builder()
                        .add("account", "test")
                        .add("password", "test")
                        .build();
                retrofitService.getCookie(formBody).enqueue(callback);
                break;
            case "switch":
                retrofitService.getNews().enqueue(callback);
        }
    }

    public static void requestContent(String type,String page,String search,retrofit2.Callback callback) {
        init();
        retrofitService.getContent(XinanApplication.getCookie(),type,page,search).enqueue(callback);
    }

    public static void requestDetail(String id,retrofit2.Callback callback){
        init();
        retrofitService.getDetail(XinanApplication.getCookie(),id).enqueue(callback);
    }

    public static void sendWithPIC(File file, final Content con, final retrofit2.Callback callback){
        init();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pic", file.getName(), requestBody)
                .build();
        Call<ResponseBody> call = retrofitService.sendPic(XinanApplication.getCookie(),multipartBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    final String responseText = response.body().string();
                    con.setPic(Utility.handlePicResponse(responseText));
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
                    retrofitService.sendContent(XinanApplication.getCookie(),formBody).enqueue(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void sendWithoutPIC(final Content con, final retrofit2.Callback callback){
        init();
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
        retrofitService.sendContent(XinanApplication.getCookie(),formBody).enqueue(callback);
    }

    public static void requestPIC(String url,retrofit2.Callback callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://img.xnxz.top/")//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        retrofitService.getPIC(url).enqueue(callback);;

    }


}
