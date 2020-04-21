package com.smasher.widget.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.smasher.core.activityoptions.ActivityManager;
import com.smasher.core.other.BusProvider;
import com.smasher.widget.R;

import java.lang.reflect.Method;



/**
 * @author matao
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected View mRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加到Activity工具类
        ActivityManager.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }

        initParams(bundle);

        mRoot = getRootView();
        setContentView(mRoot);

        //设置灰度
        setMonochrome();

        BusProvider.getInstance().register(this);

        initView();

        initData();
    }

    /**
     * 黑白化 设置饱和度
     */
    protected void setMonochrome() {
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(1);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    /**
     * 获取是否存在NavigationBar
     * HUAWEI
     *
     * @param context context
     * @return boolean
     */
    @SuppressLint("PrivateApi")
    @SuppressWarnings("unchecked")
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                //不存在虚拟按键
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                //存在虚拟按键
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }


    /**
     * 隐藏虚拟按键
     */
    public static void hideBottomUIMenu(Activity activity) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_IMMERSIVE |
                        // Hide the nav bar and status bar
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

    }

    /**
     * 显示虚拟按键
     */
    public static void showBottomUIMenu(Activity activity) {
        //for new api versions.
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    /**
     * 进行获取根部的View
     *
     * @return RootView
     */
    public View getRootView() {
        return LayoutInflater.from(this).inflate(getRootViewRes(), null);
    }

    /**
     * 获取传递信息
     *
     * @param bundle bundle
     */
    public void initParams(Bundle bundle) {

    }

    /**
     * 进行获取根部的View
     *
     * @return RootView
     */
    public abstract @LayoutRes int getRootViewRes();

    /**
     * 进行初始化相关的View
     */
    public abstract void initView();

    /**
     * 进行初始化相关的数据
     */
    public abstract void initData();


}
