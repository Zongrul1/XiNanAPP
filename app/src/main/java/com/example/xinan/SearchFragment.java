package com.example.xinan;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.xinan.Adapter.ContentAdapter;
import com.example.xinan.Adapter.FmPagerAdapter;
import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.db.Content;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    private Button find;
    private TextView head;

    public EditText getSearch() {
        return search;
    }

    private EditText search;
    private SwipeRefreshLayout swipe;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FmPagerAdapter pagerAdapter;





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //字体设置
        Typeface typeface = ResourcesCompat.getFont(getActivity(),R.font.az);
        //初始化
        head = view.findViewById(R.id.head);
        find = view.findViewById(R.id.find);
        search = view.findViewById(R.id.search);
        swipe = view.findViewById(R.id.swipe);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        //更改字体
        head.setTypeface(typeface);
        find.setTypeface(typeface);
        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("search",search.getText().toString());
                        message.setData(bundle);
                        pagerAdapter.getCurrentFragment().getHandler().sendMessage(message);
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
                pagerAdapter.getCurrentFragment().getHandler().sendMessage(message);
                Toast.makeText(getActivity(),"刷新成功", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
        tabinit();
        return view;
    }

    private void tabinit() {
        tabLayout.addTab(tabLayout.newTab().setText("失物招领"));
        tabLayout.addTab(tabLayout.newTab().setText("闲置物品"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("search",search.getText().toString());
                message.setData(bundle);
                pagerAdapter.getCurrentFragment().getHandler().sendMessage(message);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pagerAdapter = new FmPagerAdapter(this.getChildFragmentManager(),tabLayout.getTabCount(),SearchFragment.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


}

    /*
    *okhttp + retrofit
     */
//    public void requestSearch(String key,String type) {
////        String Url = "https://xnxz.top/wc/getCard?type=" + type +"&page=1&search="+key;
////        HttpUtil.sendOkHttpRequest(Url, new Callback() {
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////                final String responseText = response.body().string();
////                cons.clear();
////                Utility.handleContentResponse(cons,responseText);
////                getActivity().runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        adapter.notifyDataSetChanged();
////                        listView.setSelection(0);
////                    }
////                });
////            }
////            @Override
////            public void onFailure(Call call, IOException e) {
////                e.printStackTrace();
////                getActivity().runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
////                    }
////                });
////            }
////        });
//        RetrofitUtil.requestContent(type, "1", key, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    final String responseText = response.body().string();
//                    Log.d("TAG",responseText);
//                    cons.clear();
//                    Utility.handleContentResponse(cons,responseText);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.notifyDataSetChanged();
//                            listView.setSelection(0);
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "search加载失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//}
