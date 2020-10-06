package com.example.xinan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xinan.Adapter.ContentAdapter;
import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.db.Content;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;
import com.example.xinan.util.XinanApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class ContentFragment extends Fragment {

    private RecyclerView listView;
    private ContentAdapter adapter;
    private HelperSubscriber getSearch;
    private String search;
    List<Content> cons = new ArrayList<>();
    private String type;

    public Handler getHandler() {
        return handler;
    }

    public String getType() {
        return type;
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            search = bundle.getString("search");
            requestContent(search,type);
            return false;
            //在这里实现ui更新的效果
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        Bundle args = getArguments();
        type = args.getString("type");
        adapter = new ContentAdapter(R.layout.search_content, cons,getActivity());
        listView = view.findViewById(R.id.list_view);
        listView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        listView.setAdapter(adapter);
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
        requestContent("",type);
    }

    public void requestContent(String key,String type) {
        RxRetrofitUtil.getInstance().requestContent(new MainSubscriber<Response<ResponseBody>>(getSearch, getActivity(), true), type, "1", key);
    }
}