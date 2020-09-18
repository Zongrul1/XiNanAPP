package com.example.xinan.Subscriber;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.xinan.View.LoadingDialog;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liukun on 16/3/10.
 */
public class MainSubscriber<T> extends Subscriber<T> {

    private HelperSubscriber mSubscriberOnNextListener;
    private LoadingDialog dialog;
    private boolean flag;//是否显示dialog
    //private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public MainSubscriber(HelperSubscriber mSubscriberOnNextListener, Context context,boolean flag) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.flag = flag;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if(flag) {
            LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(context)
                    .setMessage("加载中...")
                    .setCancelable(false)
                    .setCancelOutside(false);
            dialog = loadBuilder.create();
            dialog.show();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if(flag) dialog.dismiss();
        //Toast.makeText(context, "这次请求使用了Rxjava", Toast.LENGTH_SHORT).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(flag) dialog.dismiss();
        ((Activity)context).finish();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            try {
                mSubscriberOnNextListener.onNext(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
//     */
//    @Override
//    public void onCancelProgress() {
//        if (!this.isUnsubscribed()) {
//            this.unsubscribe();
//        }
//    }
}
