package com.example.xinan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.xinan.View.LoadingDialog;
import com.example.xinan.db.Content;
import com.example.xinan.util.HttpUtil;
import com.example.xinan.util.PictureUtil;
import com.example.xinan.util.Utility;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SendActivity extends AppCompatActivity {
    private Button select;
    private Button back;
    private Button send;
    private Button picButton;
    private ImageView pic;
    private ToggleButton change;
    private TextView contactType;
    private TextView head;
    private EditText title;
    private EditText tag;
    private EditText name;
    private EditText contact;
    private EditText description;
    private EditText price;
    private LinearLayout priceWindow;
    private int type;
    private int contactTypeNumber;
    private String picturePath;
    private static int RESULT_LOAD_IMAGE = 10;//??
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactTypeNumber = 0;
        type = 1;
        Typeface typeface = ResourcesCompat.getFont(this,R.font.az);
        setContentView(R.layout.activity_send);
        head = findViewById(R.id.head);
        back = findViewById(R.id.back);
        select = findViewById(R.id.select);
        send = findViewById(R.id.send);
        picButton = findViewById(R.id.pic_button);
        pic = findViewById(R.id.pic);
        change = findViewById(R.id.change);
        contactType = findViewById(R.id.contactType);
        title = findViewById(R.id.title);
        tag = findViewById(R.id.tag);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        priceWindow = findViewById(R.id.priceWindow);
        //更改字体
        priceWindow.setVisibility(View.GONE);
        price.setText("0");
        back.setTypeface(typeface);
        head.setTypeface(typeface);
        send.setTypeface(typeface);
        change.setTypeface(typeface);
        picButton.setTypeface(typeface);
        select.setTypeface(typeface);
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
                                contactTypeNumber = which;
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
                try {
                    if(picturePath != null) {
                       Log.d("TAG", picturePath);
                        File mFile = new File(picturePath);
                        //压缩图片
                        String targetPath = "/storage/emulated/0/DCIM/Camera/compressPic.jpg";
                        final String compressImage = PictureUtil.compressImage(picturePath, targetPath, 25);
                        final File compressedPic = new File(compressImage);
                        HttpUtil.postImageOkHttpRequest("https://xnxz.top/wc/upload", compressedPic, new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseText = response.body().string();
                                picturePath = Utility.handlePicResponse(responseText);
                                Log.d("TAG", picturePath);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //loading
                                        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(SendActivity.this)
                                                .setMessage("发送中...")
                                                .setCancelable(false)
                                                .setCancelOutside(false);
                                        final LoadingDialog dialog = loadBuilder.create();
                                        dialog.show();
                                        final Timer t = new Timer();
                                        t.schedule(new TimerTask() {
                                            public void run() {
                                                dialog.dismiss();
                                                t.cancel();
                                                SendActivity.this.finish();
                                            }
                                        }, 3000);
                                    }
                                });
                                Content con = new Content(String.valueOf(title.getText()), String.valueOf(tag.getText()), String.valueOf(name.getText()), contactTypeNumber, String.valueOf(contact.getText()), String.valueOf(description.getText()), picturePath,Integer.parseInt(String.valueOf(price.getText())), type);
                                HttpUtil.postOkHttpRequest("https://xnxz.top/wc/post", con, new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseText = response.body().string();
                                        Log.d("TAG", responseText);
                                    }
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //closeProgressDialog();
                                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //closeProgressDialog();
                                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    else {
                        Content con = new Content(String.valueOf(title.getText()), String.valueOf(tag.getText()), String.valueOf(name.getText()), contactTypeNumber, String.valueOf(contact.getText()), String.valueOf(description.getText()), "",Integer.parseInt(String.valueOf(price.getText())), type);
                        HttpUtil.postOkHttpRequest("https://xnxz.top/wc/post", con, new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseText = response.body().string();
                                Log.d("TAG", responseText);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //loading
                                        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(SendActivity.this)
                                                .setMessage("发送中...")
                                                .setCancelable(false)
                                                .setCancelOutside(false);
                                        final LoadingDialog dialog = loadBuilder.create();
                                        dialog.show();
                                        final Timer t = new Timer();
                                        t.schedule(new TimerTask() {
                                            public void run() {
                                                dialog.dismiss();
                                                t.cancel();
                                                SendActivity.this.finish();
                                            }
                                        }, 2000);
                                        picturePath = "";
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //closeProgressDialog();
                                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //closeProgressDialog();
                            Toast.makeText(getApplicationContext(), "请输入正确的金额", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(type == 2) {type = 1;priceWindow.setVisibility(View.GONE);head.setText("拾物登记"); price.setText("0");}
                else if(type == 1) {type = 2;priceWindow.setVisibility(View.VISIBLE);head.setText("闲置发布");}
            }
        });
        picButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //点击事件，而重定向到图片库
                requestMyPermissions();//动态获取权限
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

    }
    //用户选择一张图片，onActivityResult()方法将会被调用，
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//我们需要判断requestCode是否是我们之前传给startActivityForResult()方法的RESULT_LOAD_IMAGE，并且返回的数据不能为空
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //查询我们需要的数据
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            ImageView imageView = (ImageView) findViewById(R.id.pic);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }
    //更改联系方式
    public void changeinfo(String contact){
        contactType.setText(contact);
    }
    //动态获取权限
    private void requestMyPermissions() {
        String TAG = "SendActivity";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(SendActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d(TAG, "requestMyPermissions: 有写SD权限");
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(SendActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            Log.d(TAG, "requestMyPermissions: 有读SD权限");
        }
    }

//后续可能加入压缩
    public  void compress1(File file){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 获取到屏幕对象
        Display display = getWindowManager().getDefaultDisplay();
        // 获取到屏幕的真是宽和高
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        // 计算缩放比例
        int widthScale = opts.outWidth /screenWidth;
        int heightScale = opts.outHeight /screenHeight;
        int samPle = Math.max(widthScale,heightScale);
        opts.inSampleSize = 2;
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SHMedia/Photos/123.jpg", opts);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SHMedia/Photos/123.jpeg")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}