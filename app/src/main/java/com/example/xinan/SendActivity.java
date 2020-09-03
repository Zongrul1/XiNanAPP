package com.example.xinan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xinan.db.Content;
import com.example.xinan.util.Utility;

public class SendActivity extends AppCompatActivity {
    private Button select;
    private Button back;
    private Button send;
    private TextView number_text;
    private EditText title;
    private EditText tag;
    private EditText name;
    private EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        back = findViewById(R.id.back);
        select = findViewById(R.id.select);
        send = findViewById(R.id.send);
        number_text = findViewById(R.id.number_text);
        title = findViewById(R.id.title);
        tag = findViewById(R.id.tag);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        select.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendActivity.this);
                        builder.setTitle("选择一种联系方式");
                        //    指定下拉列表的显示数据
                        final String[] contacts = {"手机", "qq", "微信"};
                        //    设置一个下拉的列表选择项
                        builder.setItems(contacts, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                changeinfo(contacts[which]);
                                //Toast.makeText(SendActivity.this, "选择的联系方式为：" + contacts[which], Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                }
        );
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SendActivity.this.finish();}
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Content con = new Content(String.valueOf(title.getText()),String.valueOf(tag.getText()),String.valueOf(name.getText()),String.valueOf(number.getText()));
                String res = Utility.ContentToJson(con);
                Toast.makeText(getApplicationContext(),res, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void changeinfo(String contact){
        number_text.setText(contact);
    }
}