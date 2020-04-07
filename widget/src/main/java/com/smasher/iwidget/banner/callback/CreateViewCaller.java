package com.smasher.iwidget.banner.callback;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by lin on 2018/1/4.
 * 加载默认布局
 */
public class CreateViewCaller implements CreateViewCallBack<FrameLayout> {

    private ScaleType scaleType;

    public static CreateViewCaller build() {
        return new CreateViewCaller(ScaleType.FIT_XY);
    }

    public static CreateViewCaller build(ScaleType scaleType) {
        return new CreateViewCaller(scaleType);
    }

    public CreateViewCaller(ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public static AppCompatImageView findImageView(View view) {
        if (view instanceof ViewGroup) {
            for(int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                View imageView = ((ViewGroup)view).getChildAt(i);
                if (imageView instanceof AppCompatImageView) {
                    return (AppCompatImageView)imageView;
                }
            }
            return null;
        }else{
            return null;
        }
    }

    @Override
    public FrameLayout createView(Context context, ViewGroup parent, int viewType) {
        return createImageView(context);
    }


    private FrameLayout createImageView(Context context) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        FrameLayout layout = new FrameLayout(context);
        layout.setLayoutParams(params);

        AppCompatImageView view = new AppCompatImageView(context);
        view.setLayoutParams(params);
        /* AppCompatImageView.ScaleType.FIT_XY*/
        view.setScaleType(scaleType);

        layout.addView(view, params);
        return layout;
    }
}
