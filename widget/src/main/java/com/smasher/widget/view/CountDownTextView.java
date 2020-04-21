package com.smasher.widget.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

/**
 * @author Smasher
 * on 2019/12/12 0012
 */
public class CountDownTextView extends AppCompatTextView {

    public static final String TAG = "CountDownTextView";

    /**
     * 提示文字
     */
    private String mHintText = "重新发送";

    /**
     * 倒计时时间
     */
    private long mCountDownMillis = 60_000;

    /**
     * 间隔时间差(两次发送handler)
     */
    private long mIntervalMillis = 1_000;

    /**
     * 可用状态下字体颜色Id
     */
    private int usableColorId = android.R.color.holo_blue_light;

    /**
     * 不可用状态下字体颜色Id
     */
    private int unusableColorId = android.R.color.darker_gray;

    private CountDownTimer countDownTimer;


    private boolean mSatisfied;


    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 设置是否可用
     *
     * @param usable   usable
     * @param lastTime lastTime
     */
    public void setUsable(boolean usable, long lastTime) {

        if (usable) {
            //可用
            if (!isClickable()) {
                setClickable(usable);
                setTextColor(ContextCompat.getColor(getContext(), usableColorId));
                setText(mHintText);
                release();
            }
        } else {
            //不可用
            if (isClickable()) {
                setClickable(usable);
                setTextColor(ContextCompat.getColor(getContext(), unusableColorId));
            }
            String text = mHintText + "(" + lastTime / 1000 + ")";
            setText(text);
        }

    }

    /**
     * 设置倒计时颜色
     *
     * @param usableColorId   可用状态下的颜色
     * @param unusableColorId 不可用状态下的颜色
     */
    public void setCountDownColor(@ColorRes int usableColorId, @ColorRes int unusableColorId) {
        this.usableColorId = usableColorId;
        this.unusableColorId = unusableColorId;
    }

    /**
     * 设置倒计时时间
     *
     * @param millis 毫秒值
     */
    public void setCountDownMillis(long millis) {
        mCountDownMillis = millis;
    }


    public void setSatisfied(boolean satisfied) {
        mSatisfied = satisfied;
    }


    /**
     * 开始倒计时
     */
    public void start() {
        countDownTimer = new CountDownTimer(mCountDownMillis, mIntervalMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: " + millisUntilFinished);
                setUsable(false, millisUntilFinished);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: ");
                setUsable(true, 0);
            }
        };
        countDownTimer.start();

    }

    /**
     * 重置倒计时
     */
    public void release() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    @Override
    public void setOnClickListener(@Nullable final OnClickListener onClickListener) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }

                if (mSatisfied) {
                    start();
                }
            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }


}
