package com.example.xinan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.xinan.View.LoadingDialog;
import com.example.xinan.db.Content;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SendActivity extends AppCompatActivity {
    private Button select;
    private Button back;
    private Button send;
    private TextView contactType;
    private TextView head;
    private EditText title;
    private EditText tag;
    private EditText name;
    private EditText contact;
    private EditText description;
    private int contactTypeNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactTypeNumber = 0;
        Typeface typeface = ResourcesCompat.getFont(this,R.font.az);
        setContentView(R.layout.activity_send);
        head = findViewById(R.id.head);
        back = findViewById(R.id.back);
        select = findViewById(R.id.select);
        send = findViewById(R.id.send);
        contactType = findViewById(R.id.contactType);
        title = findViewById(R.id.title);
        tag = findViewById(R.id.tag);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        description = findViewById(R.id.description);
        //更改字体
        back.setTypeface(typeface);
        head.setTypeface(typeface);
        send.setTypeface(typeface);
        select.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                        builder.setTitle("选择一种联系方式");
                        //    指定下拉列表的显示数据
                        final String[] contacts = {"手机", "qq", "微信"};
                        //    设置一个下拉的列表选择项
                        builder.setItems(contacts, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                changeinfo(contacts[which]);
                                contactTypeNumber = which;
                            }
                        });
                        builder.show();
                    }
                }
        );
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SendActivity.this.finish();}
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Content con = new Content(String.valueOf(title.getText()),String.valueOf(tag.getText()),String.valueOf(name.getText()),contactTypeNumber,String.valueOf(contact.getText()),String.valueOf(description.getText()),1);
                HttpUtil.postOkHttpRequest("https://xnxz.top/wc/post",con,new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //loading
                                LoadingDialog.Builder loadBuilder=new LoadingDialog.Builder(SendActivity.this)
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
                                        SendActivity.this.finish();
                                    }
                                }, 2000);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //closeProgressDialog();
                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }
    public void changeinfo(String contact){
        contactType.setText(contact);
    }
}