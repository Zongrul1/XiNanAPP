package com.example.xinan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.xinan.Adapter.ContentAdapter;
import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.View.LoadingDialog;
import com.example.xinan.db.Content;
import com.example.xinan.util.RetrofitUtil;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class searchFragment extends Fragment {
    private RecyclerView listView;
    private ContentAdapter adapter;
    private SearchActivity searchActivity;
    private HelperSubscriber getSearch;
    private HelperSubscriber getDetail;
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
        searchActivity = (SearchActivity) context;
        searchActivity.setHandler(handler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_area, container, false);
        listView = view.findViewById(R.id.list_view);
        adapter = new ContentAdapter(R.layout.search_content, cons);
        listView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        listView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        listView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
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
                    System.out.println(responseText);
                    cons.clear();
                    Utility.handleContentResponse(cons, responseText);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
//                            listView.setSelection(0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //获取信息
        requestContent("","1");
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Content m = cons.get(position);
//                Intent intent = new Intent(getActivity(), ShowActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("id", String.valueOf(m.getId()));
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

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
