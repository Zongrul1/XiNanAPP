package com.example.xinan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    private Button back;
    private Button find;
    private EditText search;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        back = findViewById(R.id.back);
        find = findViewById(R.id.find);
        search = findViewById(R.id.search);
        Drawable[] drawables2 = search.getCompoundDrawables(); //获取图片
        Rect r = new Rect(0, 0, drawables2[1].getMinimumWidth() / 5, drawables2[1].getMinimumHeight() / 5); //设置图片参数
        drawables2[1].setBounds(r);
        search.setCompoundDrawables(drawables2[1], null, null, null);  //设置到控件的位置（左，上，右，下）
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SearchActivity.this.finish();
                    }
                });
        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("search",search.getText().toString());//这里的values就是我们要传的值
                        message.setData(bundle);//这里模拟的是传递对象数据
                        handler.sendMessage(message);
                        Toast.makeText(getApplicationContext(),search.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }
}