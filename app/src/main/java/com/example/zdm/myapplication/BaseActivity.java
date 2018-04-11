package com.example.zdm.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * author:Created by WangZhiQiang on 2018/4/9.
 */

public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initHttp();
        initData();

    }

    public abstract void initData();

    public abstract void initHttp();

    public abstract void initView();



}
