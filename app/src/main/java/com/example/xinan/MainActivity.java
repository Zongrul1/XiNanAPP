package com.example.xinan;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private  Fragment fragment;

    public BottomNavigationView getNavView() {
        return navView;
    }

    private BottomNavigationView navView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_search: {
                    fragment = new SearchFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, fragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                case R.id.navigation_home: {
                    fragment = new MainFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, fragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                case R.id.navigation_send: {
                    fragment = new SendFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, fragment)
                            .addToBackStack(null)
                             .commit();
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(navView.getMenu().getItem(1).getItemId());
        fragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
//service测试
//    private UpdateService updateBinder;
//
//    private ServiceConnection connection = new ServiceConnection() {
//        //可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            updateBinder = (UpdateService) service;
//            updateBinder.updateMain();
////            chooseFragment fragment = (chooseFragment) getFragmentManager().findFragmentById(R.id.choose_area_fragment);
////            fragment.requestIndex();
//            FragmentManager manager = getSupportFragmentManager();
//            chooseFragment fragment = (chooseFragment)manager.findFragmentById(R.id.choose_area_fragment);
//            fragment.requestIndex();
//        }//onServiceConnected()方法关键，在这里实现对服务的方法的调用
//    };