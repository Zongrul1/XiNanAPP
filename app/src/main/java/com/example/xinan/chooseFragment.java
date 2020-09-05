package com.example.xinan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.xinan.Adapter.NewsAdapter;
import com.example.xinan.db.News;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class chooseFragment extends Fragment {
    private ListView listView;
    private NewsAdapter adapter;
    List<News> news = new ArrayList<>();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_area, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new NewsAdapter(getContext(), R.layout.index_list, news);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String indexString = prefs.getString("index", null);
//        if(indexString == null) {
//            requestIndex();
//        }
//        else{
//            Utility.handleNewsResponse(news,indexString);
//        }
        requestCookie();
        //loading
        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(getContext())
                .setMessage("加载中...")
                .setCancelable(false)
                .setCancelOutside(false);
        final LoadingDailog dialog=loadBuilder.create();
        dialog.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                t.cancel();
            }
        }, 4000);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News m = news.get(position);
                Intent intent =new Intent(getActivity(),ShowActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", String.valueOf(m.getId()));
                intent.putExtras(bundle);
                startActivity(intent);
//                Intent intent = new Intent();
//                intent.setData(Uri.parse());//Url 就是你要打开的网址
//                intent.setAction(Intent.ACTION_VIEW);
//                startActivity(intent);
            }
        });
    }
    public void requestCookie(){
        HttpUtil.postOkHttpRequest("https://xnxz.top/wc/login",new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ResponseBody responseText = response.body();
                final Headers header = response.headers();
                HttpUtil.setToken(header.get("Set-Cookie"));
                Message msg = Message.obtain();
                msg.what = GET_DATA_SUCCESS;
                handler.sendMessage(msg);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void requestIndex() {
        String Url = "https://xnxz.top/wc/switch";
        HttpUtil.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Utility.handleNewsResponse(news,responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
//                        editor.putString("index", responseText);
//                        editor.apply();
                        adapter.notifyDataSetChanged();
                        listView.setSelection(0);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //loadBingPic();
    }

}
