package com.smasher.widget.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smasher.core.other.BusProvider;
import com.smasher.widget.struct.FunctionManager;


/**
 * @author matao
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected FunctionManager mFunctionManager;
    protected Context mContext;
    protected View root;

    private boolean mFirstLoad = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BusProvider.getInstance().register(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = LayoutInflater.from(mContext).inflate(getLayoutRes(), container, false);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }


    public FunctionManager getFunctionManager() {
        return mFunctionManager;
    }


    public void setFunctionManager(FunctionManager functionManager) {
        this.mFunctionManager = functionManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        //懒加载
        if (mFirstLoad) {
            initData();
            mFirstLoad = false;
        }

    }


    /**
     * 加载数据
     */
    protected abstract void initData();


    /**
     * 加载数据
     */
    protected abstract void initView();


    /**
     * 构建页面
     *
     * @return LayoutRes
     */
    @LayoutRes
    protected abstract int getLayoutRes();


}
