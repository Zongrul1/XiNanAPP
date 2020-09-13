package com.example.xinan;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.xinan.Adapter.MyPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private Button send;
    private Button search;
    private TextView recommend;
    private Handler handler;
    private SwipeRefreshLayout swipe;
//service测试
//    private UpdateService updateBinder;
//
//    private ServiceConnection connection = new ServiceConnection() {
//        //可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            updateBinder = (UpdateService) service;
//            updateBinder.updateMain();
////            chooseFragment fragment = (chooseFragment) getFragmentManager().findFragmentById(R.id.choose_area_fragment);
////            fragment.requestIndex();
//            FragmentManager manager = getSupportFragmentManager();
//            chooseFragment fragment = (chooseFragment)manager.findFragmentById(R.id.choose_area_fragment);
//            fragment.requestIndex();
//        }//onServiceConnected()方法关键，在这里实现对服务的方法的调用
//    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //字体设置
        Typeface typeface = ResourcesCompat.getFont(this,R.font.az);
        setContentView(R.layout.activity_main);
        swipe = findViewById(R.id.swipe);
        recommend = findViewById(R.id.recommend);
        vp = (ViewPager) findViewById(R.id.ViewPager);
        aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.pager1,null,false));
        aList.add(li.inflate(R.layout.pager2,null,false));
        aList.add(li.inflate(R.layout.pager3,null,false));
        mAdapter = new MyPagerAdapter(aList);
        vp.setAdapter(mAdapter);
        send = (Button)findViewById(R.id.send);
        Drawable[] drawables1 = send.getCompoundDrawables(); //获取图片
        Rect r = new Rect(0, 0, drawables1[1].getMinimumWidth() /3, drawables1[1].getMinimumHeight() /3); //设置图片参数
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
        r = new Rect(0, 0, drawables2[1].getMinimumWidth() /3, drawables2[1].getMinimumHeight() /3); //设置图片参数
        drawables2[1].setBounds(r);
        search.setCompoundDrawables(null,null,drawables2[1],null);  //设置到控件的位置（左，上，右，下）
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
                Toast.makeText(getApplicationContext(),"刷新成功", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
        send.setTypeface(typeface);
        search.setTypeface(typeface);
        recommend.setTypeface(typeface);
        initRunnable();
//        Intent intent = new Intent(this, UpdateService.class);
//        startService(intent);
    }
    public void setHandler(Handler handler){
        this.handler = handler;
    }


    /**
     * 定时切换
     */
    private static final int TIME = 3000;
    private Runnable viewpagerRunnable;
    private boolean pointer = true;
    protected void initRunnable() {
        viewpagerRunnable = new Runnable() {

            @Override
            public void run() {
                int nowIndex = vp.getCurrentItem();
                int count = vp.getAdapter().getCount();
                // 如果下一张的索引大于最后一张，则切换到第一张
                if(!pointer){
                    vp.setCurrentItem(nowIndex - 1);
                } else {
                    vp.setCurrentItem(nowIndex + 1);
                }
                if (nowIndex + 1 >= count) {
                    pointer = false;
                }
                if(nowIndex - 1 < 0){
                    pointer = true;
                }
                handler.postDelayed(viewpagerRunnable, TIME);
            }
        };
        handler.postDelayed(viewpagerRunnable, TIME);
    }

}