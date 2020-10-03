package com.example.xinan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinan.Adapter.MyPagerAdapter;
import com.example.xinan.Adapter.NewsAdapter;
import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.db.News;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;
import com.example.xinan.util.XinanApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class MainFragment extends Fragment {
    private ViewPager vp;
    private ArrayList<View> aList;
    private MyPagerAdapter mAdapter;
    private TextView recommend;
    private SwipeRefreshLayout swipe;
    private RecyclerView listView;
    private NewsAdapter adapter;
    private TextView head;
    List<News> news = new ArrayList<>();
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    //rxjava
    private HelperSubscriber getIndex;
    private HelperSubscriber getCookie;


    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GET_DATA_SUCCESS:
                    requestIndex();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(), "服务器发生错误", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onResume() {
        super.onResume();
        //回调加载数据
        requestIndex();
    }

    //绑定handler
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Typeface typeface = ResourcesCompat.getFont(getActivity(),R.font.az);
        swipe = view.findViewById(R.id.swipe);
        recommend = view.findViewById(R.id.recommend);
        head = view.findViewById(R.id.head);
        vp = (ViewPager) view.findViewById(R.id.ViewPager);
        aList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.pager1,null,false));
        aList.add(li.inflate(R.layout.pager2,null,false));
        aList.add(li.inflate(R.layout.pager3,null,false));
        mAdapter = new MyPagerAdapter(aList);
        vp.setAdapter(mAdapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(XinanApplication.getCookie() != null) {//安全措施
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                    Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);
                }
            }
        });
        recommend.setTypeface(typeface);
        head.setTypeface(typeface);
        initRunnable();
        listView = view.findViewById(R.id.list_view);
        adapter = new NewsAdapter(R.layout.index_list, news);
        listView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //RxSubscriber
        getCookie = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) {
                final Headers header = response.headers();
                XinanApplication.setCookie(header.get("Set-Cookie"));
                Message msg = Message.obtain();
                msg.what = GET_DATA_SUCCESS;
                handler.sendMessage(msg);
            }
        };
        getIndex = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                final String responseText = response.body().string();
                news.clear();
                Utility.handleNewsResponse(news, responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        //请求cookie
        if (XinanApplication.getCookie() == null) requestCookie();
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

    public void requestCookie() {
        RxRetrofitUtil.getInstance().requestIndex(new MainSubscriber<Response<ResponseBody>>(getCookie, getActivity(),true), "login");
    }

    public void requestIndex() {
        RxRetrofitUtil.getInstance().requestIndex(new MainSubscriber<Response<ResponseBody>>(getIndex, getActivity(),false), "switch");
    }
}