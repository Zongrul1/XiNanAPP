package com.example.xinan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.xinan.Adapter.MyPagerAdapter;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SendActivity.class);
                startActivity(intent);
            }
        });
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            Intent intent = new Intent();// 创建Intent对象
//            intent.setAction(Intent.ACTION_MAIN);// 设置Intent动作
//            intent.addCategory(Intent.CATEGORY_HOME);// 设置Intent种类
//            startActivity(intent);// 将Intent传递给Activity
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}