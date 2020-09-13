package com.example.xinan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.xinan.Adapter.NewsAdapter;
import com.example.xinan.View.LoadingDialog;
import com.example.xinan.db.News;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.RetrofitUtil;
import com.example.xinan.util.Utility;
import com.example.xinan.util.XinanApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Callback;


public class chooseFragment extends Fragment {
    private MainActivity MainActivity;
    private ListView listView;
    private NewsAdapter adapter;
    List<News> news = new ArrayList<>();
    private boolean isFirstLoading = true;
    public static final int GET_DATA_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int SERVER_ERROR = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    requestIndex();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getContext(),"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public Handler getHandler(){
        return handler;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            requestIndex();
        }

        isFirstLoading = false;
    }
    //绑定handler
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity = (MainActivity) context;
        MainActivity.setHandler(handler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_area, container, false);
        listView = view.findViewById(R.id.list_view);
        adapter = new NewsAdapter(getContext(), R.layout.index_list, news);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(HttpUtil.getToken() == null) requestCookie();
        //loading
        LoadingDialog.Builder loadBuilder=new LoadingDialog.Builder(getContext())
                .setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false);
        final LoadingDialog dialog=loadBuilder.create();
        dialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 6000);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News m = news.get(position);
                Intent intent =new Intent(getActivity(),ShowActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", String.valueOf(m.getId()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    public void requestCookie(){
//        HttpUtil.postOkHttpRequest("https://xnxz.top/wc/login",new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final ResponseBody responseText = response.body();
//                final Headers header = response.headers();
//                HttpUtil.setToken(header.get("Set-Cookie"));
//                Message msg = Message.obtain();
//                msg.what = GET_DATA_SUCCESS;
//                handler.sendMessage(msg);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //closeProgressDialog();
//                        Toast.makeText(getContext(), "cookie加载失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        RetrofitUtil.requestIndex("login", new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                final Headers header = response.headers();
                XinanApplication.setCookie(header.get("Set-Cookie"));
                Message msg = Message.obtain();
                msg.what = GET_DATA_SUCCESS;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "cookie获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void requestIndex() {
//        String Url = "https://xnxz.top/wc/switch";
//        HttpUtil.sendOkHttpRequest(Url, new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseText = response.body().string();
//                news.clear();
//                Utility.handleNewsResponse(news,responseText);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.notifyDataSetChanged();
//                        listView.setSelection(0);
//                    }
//                });
//            }
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "index加载失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        RetrofitUtil.requestIndex("switch", new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    final String responseText = response.body().string();
                    news.clear();
                    Utility.handleNewsResponse(news,responseText);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(retrofit2.Call call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "index加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
