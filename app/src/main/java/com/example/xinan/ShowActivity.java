package com.example.xinan;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.xinan.View.LoadingDialog;
import com.example.xinan.View.URLImageView;
import com.example.xinan.db.Content;
import com.example.xinan.util.RetrofitUtil;
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
    private URLImageView pic;
    private String id;
    private Content con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        Typeface typeface = ResourcesCompat.getFont(this,R.font.az);
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
        //loading
        LoadingDialog.Builder loadBuilder=new LoadingDialog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false);
        final LoadingDialog dialog=loadBuilder.create();
        dialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 2000);
        requestContent();
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            ShowActivity.this.finish();
                    }
                });

    }

    public void requestContent() {
//        String Url = "https://xnxz.top/wc/getDetail?id=" + name;
//        HttpUtil.sendOkHttpRequest(Url, new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseText = response.body().string();
//                con = Utility.handleContentResponse(responseText);
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run() {
//                        showinfo(con);
//                    }
//                });
//
//            }
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
//            }
//        });
        RetrofitUtil.requestDetail(id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "detail加载失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void showinfo(Content con){
        nickname.setText(con.getNick());
        time.setText(con.getDate());
        fulltext.setText(con.getDescription());
        price.setText("￥" + con.getPrice());
        tag.setText(con.getTitle());
        pic.setImageURL(con.getPic());
    }
}