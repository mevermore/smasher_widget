package com.smasher.widget.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author Smasher
 * on 2020/4/21 0021
 */
public class ClipImageView extends AppCompatImageView {

    ValueAnimator animator;

    public ClipImageView(Context context) {
        super(context);
    }

    public ClipImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void buildAnimation(Drawable start, Drawable end, int gravity, int orientation) {
        ClipDrawable clipDrawable = new ClipDrawable(end, gravity, orientation);
        Drawable[] drawables = new Drawable[]{start, clipDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        setImageDrawable(layerDrawable);
    }


    public void start() {
        animator = ValueAnimator.ofInt(0, 10000);
        animator.setDuration(2000);
        animator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            setImageLevel(currentValue);
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    public void cancel() {
        if (animator.isStarted()) {
            animator.cancel();
        }
    }

}
