package com.smasher.example.activity;

import android.animation.ValueAnimator;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
    Drawable start;
    Drawable finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        ButterKnife.bind(this);

        start = ContextCompat.getDrawable(this, R.drawable.icon_load_start);
        finish = ContextCompat.getDrawable(this, R.drawable.icon_load_finish);

        planB(start, finish);

    }

    private void planB(Drawable start, Drawable finish) {
        ClipDrawable clipDrawable = new ClipDrawable(finish, Gravity.START, ClipDrawable.HORIZONTAL);
        Drawable[] drawables = new Drawable[]{start, clipDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        mImageView.setImageDrawable(layerDrawable);
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
