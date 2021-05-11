package com.smasher.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.smasher.core.log.Logger;
import com.smasher.example.R;
import com.smasher.example.databinding.ActivityMainBinding;
import com.smasher.widget.base.BaseActivity;
import com.smasher.widget.state.StateViewHelper;


public class MainActivity extends BaseActivity implements View.OnClickListener {


    ActivityMainBinding mBinding;
    private StateViewHelper mStateViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getRootView() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public int getRootViewRes() {
        return 0;
    }

    @Override
    public void initView() {
        mStateViewHelper = new StateViewHelper(mBinding.container);
        mStateViewHelper.onReloadClickListener(this);
        mStateViewHelper.stateLoading();

        mBinding.container.postDelayed(() -> mStateViewHelper.stateNormal(), 2000);

        mBinding.stepper.setEditAble(true);
        mBinding.stepper.setOnValueChangeListener((view, value) -> Logger.d("value:" + value));

        mBinding.bannerExample.setOnClickListener(this);
        mBinding.structExample.setOnClickListener(this);
        mBinding.dialogExample.setOnClickListener(this);
        mBinding.downloadExample.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_state, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.stateNormal) {//显示数据视图
            mStateViewHelper.stateNormal();
        } else if (itemId == R.id.stateLoading) {//显示加载中视图
            mStateViewHelper.stateLoading();
        } else if (itemId == R.id.stateEmpty) {//显示空数据视图
            mStateViewHelper.stateEmpty();
        } else if (itemId == R.id.stateNetError) {//显示网络错误视图
            mStateViewHelper.stateNetError();
        } else if (itemId == R.id.stateError) {//显示加载错误视图
            mStateViewHelper.stateError();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        int id = view.getId();
        if (id == R.id.banner_example) {
        } else if (id == R.id.struct_example) {
            intent.setClass(this, StructActivity.class);
        } else if (id == R.id.dialog_example) {
            intent.setClass(this, DialogsActivity.class);
        } else if (id == R.id.download_example) {
            intent.setClass(this, DownloadAct.class);
        }
        startActivity(intent);
    }
}
