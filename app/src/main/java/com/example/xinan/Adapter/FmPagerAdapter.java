package com.example.xinan.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.xinan.ContentFragment;
import com.example.xinan.SearchFragment;

public class FmPagerAdapter extends FragmentPagerAdapter {
    int num;
    SearchFragment searchFragment;
    ContentFragment mCurrentFragment;
    @SuppressLint("WrongConstant")
    public FmPagerAdapter(@NonNull FragmentManager fm,int num,SearchFragment searchFragment) {
        super(fm,0);
        this.num = num;
        this.searchFragment = searchFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",String.valueOf(position + 1));
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public int getCount() {
        return num;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (ContentFragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public ContentFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
