package com.example.xinan.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Toast;

import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.util.RxRetrofitUtil;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class URLImageView extends androidx.appcompat.widget.AppCompatImageView {
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    private HelperSubscriber getPIC;
    //子线程不能操作UI，通过Handler设置图片
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(), "没有图片", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
     });


    public URLImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public URLImageView(Context context) {
        super(context);
    }

    public URLImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //设置网络图片
    public void setImageURL(final String path) {
        //开启一个线程用于联网
        new Thread() {
            @Override
            public void run() {
                //同一线程中
                getPIC = new HelperSubscriber<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) throws IOException {
                        InputStream inputStream = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        //利用Message把图片发给Handler
                        Message msg = Message.obtain();
                        msg.obj = bitmap;
                        msg.what = GET_DATA_SUCCESS;
                        handler.sendMessage(msg);
                        inputStream.close();
                    }
                };
                RxRetrofitUtil.getInstance().requestPIC(new MainSubscriber<Response<ResponseBody>>(getPIC,getContext(),false),path);
            }
        }.start();
    }
}
/*
老版获取方式
 */
                //                    //把传过来的路径转成URL
//                    URL url = new URL(path);
//                    //获取连接
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    //使用GET方法访问网络
//                    connection.setRequestMethod("GET");
//                    //超时时间为10秒
//                    connection.setConnectTimeout(10000);
//                    //获取返回码
//                    int code = connection.getResponseCode();
//                    if (code == 200) {
//                        InputStream inputStream = connection.getInputStream();
//                        //使用工厂把网络的输入流生产Bitmap
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        //利用Message把图片发给Handler
//                        Message msg = Message.obtain();
//                        msg.obj = bitmap;
//                        msg.what = GET_DATA_SUCCESS;
//                        handler.sendMessage(msg);
//                        inputStream.close();
//                    }else {
//                        //服务启发生错误
//                        handler.sendEmptyMessage(SERVER_ERROR);
//                    }
//                RetrofitUtil.requestPIC(path, new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        try {
//                            InputStream inputStream = response.body().byteStream();
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            //利用Message把图片发给Handler
//                            Message msg = Message.obtain();
//                            msg.obj = bitmap;
//                            msg.what = GET_DATA_SUCCESS;
//                            handler.sendMessage(msg);
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call call, Throwable t) {
//                        handler.sendEmptyMessage(SERVER_ERROR);
//                    }
//                });

