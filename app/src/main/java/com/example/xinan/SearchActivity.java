package com.example.xinan;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.xinan.View.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity {
    private Button back;
    private Button find;
    private TextView head;
    private EditText search;
    private Handler handler;
    private ToggleButton change;
    private SwipeRefreshLayout swipe;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //字体设置
        Typeface typeface = ResourcesCompat.getFont(this,R.font.az);
        //初始化
        type = 1;
        head = findViewById(R.id.head);
        back = findViewById(R.id.back);
        find = findViewById(R.id.find);
        search = findViewById(R.id.search);
        change = findViewById(R.id.change);
        swipe = findViewById(R.id.swipe);
        //更改字体
        back.setTypeface(typeface);
        head.setTypeface(typeface);
        find.setTypeface(typeface);
        change.setTypeface(typeface);
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
                        bundle.putString("type",String.valueOf(type));
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
        //刷新
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("search",search.getText().toString());
                bundle.putString("type",String.valueOf(type));
                message.setData(bundle);
                handler.sendMessage(message);
                Toast.makeText(getApplicationContext(),"刷新成功", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
        change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(type == 2) type = 1;
                else if(type == 1) type = 2;
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("search",search.getText().toString());
                bundle.putString("type",String.valueOf(type));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }
}