package com.smasher.example.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.smasher.example.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogsActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.button)
    Button mButton;

    ValueAnimator animator;
    ClipDrawable clipDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        ButterKnife.bind(this);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.icon_load_finish);
        clipDrawable = new ClipDrawable(drawable, Gravity.START, ClipDrawable.HORIZONTAL);
        mImageView.setImageDrawable(clipDrawable);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {

        animator = ValueAnimator.ofInt(0, 10000);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                mImageView.setImageLevel(currentValue);
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();

    }
}
