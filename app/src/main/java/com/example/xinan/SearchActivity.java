package com.example.xinan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    private Button back;
    private Button find;
    private TextView head;
    private EditText search;
    private Handler handler;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //字体设置
        Typeface typeface = ResourcesCompat.getFont(this,R.font.az);
        //初始化
        head = findViewById(R.id.head);
        back = findViewById(R.id.back);
        find = findViewById(R.id.find);
        search = findViewById(R.id.search);
        swipe = findViewById(R.id.swipe);
        //更改字体
        back.setTypeface(typeface);
        head.setTypeface(typeface);
        Drawable[] drawables2 = search.getCompoundDrawables(); //获取图片
        Rect r = new Rect(0, 0, drawables2[1].getMinimumWidth() / 8, drawables2[1].getMinimumHeight() / 8); //设置图片参数
        drawables2[1].setBounds(r);
        search.setCompoundDrawables(drawables2[1], null, null, null);  //设置到控件的位置（左，上，右，下）
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SearchActivity.this.finish();
                    }
                });
        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("search",search.getText().toString());
                        message.setData(bundle);
                        handler.sendMessage(message);
                        Toast.makeText(getApplicationContext(),search.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        //刷新
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("search",search.getText().toString());
                message.setData(bundle);
                handler.sendMessage(message);
                Toast.makeText(getApplicationContext(),"刷新成功", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }
}