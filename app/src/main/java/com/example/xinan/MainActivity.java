package com.example.xinan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private Button send;
    private Button search;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp = (ViewPager) findViewById(R.id.ViewPager);
        aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.pager1,null,false));
        aList.add(li.inflate(R.layout.pager1,null,false));
        aList.add(li.inflate(R.layout.pager1,null,false));
        mAdapter = new MyPagerAdapter(aList);
        vp.setAdapter(mAdapter);
        send = (Button)findViewById(R.id.send);
        Drawable[] drawables1 = send.getCompoundDrawables(); //获取图片
        Rect r = new Rect(0, 0, drawables1[1].getMinimumWidth() * 2 / 5, drawables1[1].getMinimumHeight() * 2 / 5); //设置图片参数
        drawables1[1].setBounds(r);
        send.setCompoundDrawables(null,null,drawables1[1],null);  //设置到控件的位置（左，上，右，下）
        search = (Button)findViewById(R.id.search);
        Drawable[] drawables2 = search.getCompoundDrawables(); //获取图片
        r = new Rect(0, 0, drawables2[1].getMinimumWidth() * 2 / 5, drawables2[1].getMinimumHeight() * 2 / 5); //设置图片参数
        drawables2[1].setBounds(r);
        search.setCompoundDrawables(null,null,drawables2[1],null);  //设置到控件的位置（左，上，右，下）
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}