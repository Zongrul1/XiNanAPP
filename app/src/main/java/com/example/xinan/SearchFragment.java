package com.example.xinan;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.xinan.Adapter.ContentAdapter;
import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.db.Content;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    private RecyclerView listView;
    private ContentAdapter adapter;
    private HelperSubscriber getSearch;
    private Button find;
    private TextView head;
    private EditText search;
    private ToggleButton change;
    private SwipeRefreshLayout swipe;
    private int type;
    List<Content> cons = new ArrayList<>();
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            requestContent(bundle.getString("search"),bundle.getString("type"));
            return false;
            //在这里实现ui更新的效果
        }
    });


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        listView = view.findViewById(R.id.list_view);
        adapter = new ContentAdapter(R.layout.search_content, cons,getActivity());
        listView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        listView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        listView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        listView.setAdapter(adapter);
        //字体设置
        Typeface typeface = ResourcesCompat.getFont(getActivity(),R.font.az);
        //初始化
        type = 1;
        head = view.findViewById(R.id.head);
        find = view.findViewById(R.id.find);
        search = view.findViewById(R.id.search);
        change = view.findViewById(R.id.change);
        swipe = view.findViewById(R.id.swipe);
        //更改字体
        head.setTypeface(typeface);
        find.setTypeface(typeface);
        change.setTypeface(typeface);
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
                Toast.makeText(getActivity(),"刷新成功", Toast.LENGTH_SHORT).show();
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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSearch = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                try {
                    final String responseText = response.body().string();
                    cons.clear();
                    Utility.handleContentResponse(cons, responseText);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //获取信息
        requestContent("","1");
    }

    public void requestContent(String key,String type) {
        RxRetrofitUtil.getInstance().requestContent(new MainSubscriber<Response<ResponseBody>>(getSearch, getActivity(), true), type, "1", key);
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
