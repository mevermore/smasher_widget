package com.smasher.example.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.smasher.example.R;
import com.smasher.example.activity.StructActivity;
import com.smasher.example.databinding.FragmentBlankBinding;
import com.smasher.widget.base.BaseFragment;
import com.smasher.widget.struct.FunctionException;
import com.smasher.widget.struct.FunctionManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends BaseFragment implements View.OnClickListener {


    public static final String tag = BlankFragment.class.getSimpleName();

    private FragmentBlankBinding mBinding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    protected FunctionManager mFunctionManager;
    private StructActivity mBaseActivity;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentBlankBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    /**
     * 为要实现接口的Fragment添加FunctionManager
     *
     * @param functionManager functionManager
     */
    @Override
    public void setFunctionManager(FunctionManager functionManager) {
        this.mFunctionManager = functionManager;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

        mBinding.function1.setOnClickListener(this);
        mBinding.function2.setOnClickListener(this);
        mBinding.function3.setOnClickListener(this);
        mBinding.function4.setOnClickListener(this);
        mBinding.textView2.setOnClickListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mBaseActivity = (StructActivity) context;
        mBaseActivity.setFunctionForFragment(tag);
    }


    public String getDefineTag() {
        return tag;
    }


    @Override
    public void onClick(View view) {
        try {

            if (mFunctionManager == null) {
                return;
            }

            int id = view.getId();
            if (id == R.id.function1) {
                mFunctionManager.invokeFunction("function1");
            } else if (id == R.id.function2) {
                mFunctionManager.invokeFunction("function2");
            } else if (id == R.id.function3) {
                mFunctionManager.invokeFunction("NPWRFunction", String.class);
            } else if (id == R.id.function4) {
                mFunctionManager.invokeFunction("WPNRFunction", "data");
            }
        } catch (FunctionException e) {
            e.printStackTrace();
        }
    }
}
