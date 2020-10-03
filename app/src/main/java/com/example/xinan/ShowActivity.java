package com.example.xinan;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.View.LoadingDialog;
import com.example.xinan.View.URLImageView;
import com.example.xinan.db.Content;
import com.example.xinan.util.RetrofitUtil;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowActivity extends AppCompatActivity {
    private Button back;
    private TextView head;
    private TextView nickname;
    private TextView time;
    private TextView tag;
    private TextView price;
    private TextView fulltext;
    private ImageView pic;
    private String id;
    private Content con;
    private HelperSubscriber getDetail;
    private String HttpsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        Typeface typeface = ResourcesCompat.getFont(this, R.font.az);
        HttpsUrl = "http://img.xnxz.top/";
        id = bundle.getString("id");
        setContentView(R.layout.activity_show);
        back = findViewById(R.id.back);
        head = findViewById(R.id.head);
        nickname = findViewById(R.id.nickname);
        time = findViewById(R.id.time);
        tag = findViewById(R.id.tag);
        price = findViewById(R.id.price);
        fulltext = findViewById(R.id.fulltext);
        pic = findViewById(R.id.pic);
        back.setTypeface(typeface);
        head.setTypeface(typeface);
        getDetail = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                try {
                    final String responseText = response.body().string();
                    con = Utility.handleContentResponse(responseText);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            showinfo(con);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        requestDetail();
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowActivity.this.finish();
                    }
                });

    }

    public void requestDetail(){
        RxRetrofitUtil.getInstance().requestDetail(new MainSubscriber<Response<ResponseBody>>(getDetail, this,true), id);
    }

    public void showinfo(Content con) {
        nickname.setText(con.getNick());
        time.setText(con.getDate());
        fulltext.setText(con.getDescription());
        price.setText("￥" + con.getPrice());
        tag.setText(con.getTitle());
//        if(con.getPic() != null && con.getPic().length() > 0) pic.setImageURL(con.getPic());
//        else pic.setBackground(getResources().getDrawable((R.drawable.banner3)));
        Glide.with(this)
                .load(HttpsUrl + con.getPic())
                .apply(new RequestOptions().placeholder(R.drawable.banner1).error(R.drawable.banner3))//加载前图片，加载失败图片
                .transition(DrawableTransitionOptions.withCrossFade())//渐变
                .into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        pic.setBackground(resource);
                    }
                });
    }
}

//    public void requestContent() {
////        String Url = "https://xnxz.top/wc/getDetail?id=" + name;
////        HttpUtil.sendOkHttpRequest(Url, new Callback() {
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////                final String responseText = response.body().string();
////                con = Utility.handleContentResponse(responseText);
////                runOnUiThread(new Runnable()
////                {
////                    @Override
////                    public void run() {
////                        showinfo(con);
////                    }
////                });
////
////            }
////            @Override
////            public void onFailure(Call call, IOException e) {
////                e.printStackTrace();
////                Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
////            }
////        });
//        RetrofitUtil.requestDetail(id, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    final String responseText = response.body().string();
//                    con = Utility.handleContentResponse(responseText);
//                    runOnUiThread(new Runnable()
//                    {
//                        @Override
//                        public void run() {
//                            showinfo(con);
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "detail加载失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//}