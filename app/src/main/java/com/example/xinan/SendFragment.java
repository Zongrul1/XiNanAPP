package com.example.xinan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.xinan.Subscriber.HelperSubscriber;
import com.example.xinan.Subscriber.MainSubscriber;
import com.example.xinan.View.LoadingDialog;
import com.example.xinan.db.Content;
import com.example.xinan.util.PictureUtil;
import com.example.xinan.util.RxRetrofitUtil;
import com.example.xinan.util.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class SendFragment extends Fragment {
    private Button select;
    private Button send;
    private Button picButton;
    private ImageView pic;
    private ToggleButton change;
    private TextView contactType;
    private TextView textType;
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
    private HelperSubscriber sendContent;
    private HelperSubscriber sendPIC;
    private BottomNavigationView navView;
    private MainActivity mainActivity;
    private static int RESULT_LOAD_IMAGE = 10;//??

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        picturePath = "";
        contactTypeNumber = 0;
        type = 1;
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.az);
        head = view.findViewById(R.id.head);
        select = view.findViewById(R.id.select);
        send = view.findViewById(R.id.send);
        picButton = view.findViewById(R.id.pic_button);
        pic = view.findViewById(R.id.pic);
        change = view.findViewById(R.id.change);
        contactType = view.findViewById(R.id.contactType);
        textType = view.findViewById(R.id.textType);
        title = view.findViewById(R.id.title);
        tag = view.findViewById(R.id.tag);
        name = view.findViewById(R.id.name);
        contact = view.findViewById(R.id.contact);
        price = view.findViewById(R.id.price);
        description = view.findViewById(R.id.description);
        priceWindow = view.findViewById(R.id.priceWindow);
        pic = (ImageView) view.findViewById(R.id.pic);
        //更改字体
        priceWindow.setVisibility(View.GONE);
        price.setText("0");
        head.setTypeface(typeface);
        send.setTypeface(typeface);
        change.setTypeface(typeface);
        picButton.setTypeface(typeface);
        select.setTypeface(typeface);
        mainActivity = (MainActivity) getActivity();
        navView = mainActivity.getNavView();
        if(picturePath.length() == 0) pic.setVisibility(View.GONE);
        sendContent = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(final Response<ResponseBody> response) throws IOException {
                //loading
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("TAG",response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(getActivity())
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
                            }
                        }, 2000);
                    }
                });
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment, new MainFragment())
                        .addToBackStack(null)
                        .commit();
                navView.setSelectedItemId(navView.getMenu().getItem(1).getItemId());
            }
        };
        sendPIC = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                //loading
                final String responseText = response.body().string();
                picturePath = Utility.handlePicResponse(responseText);
                sendContent();
            }
        };
        select.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("选择一种联系方式");
                        //    指定下拉列表的显示数据
                        final String[] contacts = {"手机", "qq", "微信"};
                        //    设置一个下拉的列表选择项
                        builder.setItems(contacts, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changeinfo(contacts[which]);
                                contactTypeNumber = which;
                            }
                        });
                        builder.show();
                    }
                }
        );
        change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (type == 2) {
                    type = 1;
                    priceWindow.setVisibility(View.GONE);
                    head.setText("拾物登记");
                    price.setText("0");
                    textType.setText("我捡到了");
                } else if (type == 1) {
                    type = 2;
                    priceWindow.setVisibility(View.VISIBLE);
                    head.setText("闲置发布");
                    textType.setText("我要卖");
                }
            }
        });
        picButton.setOnClickListener(new View.OnClickListener() {

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
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picturePath.length() != 0) {
                    //压缩图片
                    String targetPath = "/storage/emulated/0/DCIM/Camera/compressPic.jpg";
                    final String compressImage = PictureUtil.compressImage(picturePath, targetPath, 25);
                    final File compressedPic = new File(compressImage);
                    RxRetrofitUtil.getInstance().sendPIC(new MainSubscriber<Response<ResponseBody>>(sendPIC, getActivity(), false), compressedPic);
                } else {
                    sendContent();
                }
            }
        });
        return view;
    }

    public void sendContent() {
        try {
            Content con = new Content(String.valueOf(title.getText()), String.valueOf(tag.getText()), String.valueOf(name.getText()), contactTypeNumber,
                    String.valueOf(contact.getText()), String.valueOf(description.getText()), picturePath, Integer.parseInt(String.valueOf(price.getText())), type);
            RxRetrofitUtil.getInstance().sendContent(new MainSubscriber<Response<ResponseBody>>(sendContent, getActivity(), false), con);
        } catch (Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "请输入正确的金额", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //用户选择一张图片，onActivityResult()方法将会被调用，
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //我们需要判断requestCode是否是我们之前传给startActivityForResult()方法的RESULT_LOAD_IMAGE，并且返回的数据不能为空
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //查询我们需要的数据
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            pic.setVisibility(View.VISIBLE);
        }
    }

    //更改联系方式
    public void changeinfo(String contact) {
        contactType.setText(contact);
    }

    //动态获取权限
    private void requestMyPermissions() {
        String TAG = "SendActivity";
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            //Log.d(TAG, "requestMyPermissions: 有写SD权限");
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            //Log.d(TAG, "requestMyPermissions: 有读SD权限");
        }
    }
}
