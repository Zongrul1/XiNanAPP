package com.example.xinan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.xinan.db.Content;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShowActivity extends AppCompatActivity {
    private Button back;
    private ImageView avater;
    private TextView nickname;
    private TextView time;
    private TextView tag;
    private TextView price;
    private TextView fulltext;
    private MyImageView pic;
    private String name;
    private Content con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        name = bundle.getString("id");
        setContentView(R.layout.activity_show);
        back = findViewById(R.id.back);
        avater = findViewById(R.id.avater);
        nickname = findViewById(R.id.nickname);
        time = findViewById(R.id.time);
        tag = findViewById(R.id.tag);
        price = findViewById(R.id.price);
        fulltext = findViewById(R.id.fulltext);
        pic = findViewById(R.id.pic);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String indexString = prefs.getString(name, null);
        //loading
        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                .setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false);
        final LoadingDailog dialog=loadBuilder.create();
        dialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 2000);
        if(indexString == null) {
            requestContent();
        }
        else{
            showinfo(Utility.handleContentResponse(indexString));
            //requestContent();
        }

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            ShowActivity.this.finish();
                    }
                });
    }

    public void requestContent() {
        String Url = "https://xnxz.top/wc/getDetail?id=" + name;
        HttpUtil.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                con = Utility.handleContentResponse(responseText);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        editor.putString(name, responseText);
                        editor.apply();
                        showinfo(con);
                    }
                });

            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showinfo(Content con){
        nickname.setText(con.getNick());
        time.setText(con.getDate());
        fulltext.setText(con.getDescription());
        price.setText("￥" + String.valueOf(con.getPrice()));
        tag.setText(con.getTitle());
        pic.setImageURL("http://img.xnxz.top/"+ con.getPic());
    }
}