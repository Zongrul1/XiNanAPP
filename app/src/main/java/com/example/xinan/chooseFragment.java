package com.example.xinan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.xinan.db.Message;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class chooseFragment extends Fragment {
    private ListView listView;
    private MessageAdapter adapter;
    List<Message> messages = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.choose_area, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new MessageAdapter(getContext(), R.layout.list, messages);
        listView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String indexString = prefs.getString("index", null);
        if(indexString == null) {
            requestIndex();
        }
        else{
            Utility.handleMessageResponse(messages,indexString);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message m = messages.get(position);
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

    public void requestIndex() {
        String Url = "https://xnxz.top/wc/switch";
        HttpUtil.sendOkHttpRequest(Url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Utility.handleMessageResponse(messages,responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("index", responseText);
                        editor.apply();
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
