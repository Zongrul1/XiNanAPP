package com.example.xinan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xinan.db.User;
import com.example.xinan.util.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText username;
    private EditText password;
    private ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        background = findViewById(R.id.background);
        //透明动画
        AlphaAnimation alpha = new AlphaAnimation(0.8F, 0.8F);
        alpha.setDuration(0); // Make animation instant
        alpha.setFillAfter(true); // Tell it to persist after the animation
        background.startAnimation(alpha);
        //按键
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().length() == 0||password.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"用户/密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    User user = new User(username.getText().toString(),SHA(password.getText().toString() + username.getText().toString()));
                    String res = Utility.UserToJson();
                    Toast.makeText(getApplicationContext(),res, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public byte[] SHA(final String strText) {
        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                String strType = "SHA-256";
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                return messageDigest.digest();

            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}