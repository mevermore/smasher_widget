package com.smasher.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * @author Smasher
 * on 2020/3/24 0024
 */
public class ShimmerTextView extends AppCompatTextView {
    private Paint mPaint;

    private int mDx;

    private LinearGradient mLinearGradient;
    private Matrix mMatrix;

    public ShimmerTextView(Context context) {
        this(context, null);
    }

    public ShimmerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShimmerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = getPaint();
        mMatrix = new Matrix();

        int length = (int) mPaint.measureText(getText().toString());
        createAnim(length);
        createLinearGradient(length);
    }

    private void createLinearGradient(int length) {
        int[] colors = new int[]{getCurrentTextColor(), 0xff00ff00, getCurrentTextColor()};
        float[] positions = new float[]{0, 0.5f, 1};
        mLinearGradient =
                new LinearGradient(-length, 0, 0, 0, colors, positions, Shader.TileMode.CLAMP);
    }

    private void createAnim(int length) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 2 * length);
        animator.addUpdateListener(animation -> {
            mDx = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mMatrix.setTranslate(mDx, 0);
        mLinearGradient.setLocalMatrix(mMatrix);
        mPaint.setShader(mLinearGradient);
        super.onDraw(canvas);
    }
}
