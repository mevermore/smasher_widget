package com.smasher.example.activity;

import android.animation.ValueAnimator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.smasher.example.R;
import com.smasher.example.databinding.ActivityDialogsBinding;
import com.smasher.widget.base.BaseActivity;

public class DialogsActivity extends BaseActivity {

    ActivityDialogsBinding mBinding;

    ValueAnimator animator;
    Drawable start;
    Drawable finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getRootViewRes() {
        return 0;
    }


    @Override
    public View getRootView() {
        mBinding = ActivityDialogsBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        bindView();
        start = ContextCompat.getDrawable(this, R.drawable.icon_load_start);
        finish = ContextCompat.getDrawable(this, R.drawable.icon_load_finish);
        planB(start, finish);
    }

    @Override
    public void initData() {

    }

    private void planB(Drawable start, Drawable finish) {
        ClipDrawable clipDrawable = new ClipDrawable(finish, Gravity.START, ClipDrawable.HORIZONTAL);
        Drawable[] drawables = new Drawable[]{start, clipDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        mBinding.imageView.setImageDrawable(layerDrawable);
    }


    private void bindView() {
        mBinding.button.setOnClickListener(v -> {
            animator = ValueAnimator.ofInt(0, 10000);
            animator.setDuration(2000);
            animator.addUpdateListener(animation -> {
                int currentValue = (int) animation.getAnimatedValue();
                mBinding.imageView.setImageLevel(currentValue);
            });
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();
        });
    }
}
