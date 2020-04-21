package com.smasher.example.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.smasher.example.R;
import com.smasher.example.fragment.BlankFragment;
import com.smasher.widget.struct.FunctionManager;
import com.smasher.widget.struct.communication.FunctionNoParamAndResult;
import com.smasher.widget.struct.communication.FunctionWithParamAndResult;
import com.smasher.widget.struct.communication.FunctionWithParamOnly;
import com.smasher.widget.struct.communication.FunctionWithResultOnly;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author moyu
 */
public class StructActivity extends AppCompatActivity {

    private static final String TAG = "StructActivity";
    FragmentManager manager;
    @BindView(R.id.button)
    Button mButton;
    @BindView(R.id.container)
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struct);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();
        initFragment();
    }

    private void initFragment() {
        BlankFragment blankFragment = BlankFragment.newInstance("param1", "param2");
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, blankFragment, blankFragment.getDefineTag());
        transaction.show(blankFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logCounts();
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        logCounts();
        initFragment();
    }

    private void logCounts() {
        List<Fragment> list = manager.getFragments();
        Log.d(TAG, "onViewClicked: " + list.size());
    }


    public void setFunctionForFragment(String tag) {
        BlankFragment fragment = (BlankFragment) manager.findFragmentByTag(tag);
        FunctionManager functionManager = FunctionManager.getInstance();

        functionManager.add(new FunctionNoParamAndResult("function1") {
            @Override
            public void function() {
                Toast.makeText(StructActivity.this, "无参数无返回值　function1", Toast.LENGTH_SHORT).show();
            }
        });
        functionManager.add(new FunctionNoParamAndResult("function2") {
            @Override
            public void function() {
                Toast.makeText(StructActivity.this, "无参数无返回值 function2", Toast.LENGTH_SHORT).show();
            }
        });
        functionManager.add(new FunctionWithResultOnly("NPWRFunction") {
            @Override
            public String function() {
                Log.d(TAG, "function: " + "NoParamWithResault");
                return "NoParamWithResault";
            }
        });
        functionManager.add(new FunctionWithParamOnly<String>("WPNRFunction") {
            @Override
            public void function(String data) {
                Toast.makeText(StructActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });
        functionManager.add(new FunctionWithParamAndResult<String, String>("WPWRFunction") {
            @Override
            public String function(String data) {
                Toast.makeText(StructActivity.this, "有参数有返回值", Toast.LENGTH_SHORT).show();
                return "withParamWithResault";
            }
        });
        fragment.setFunctionManager(functionManager);
        Log.d(TAG, "MainActivity setFunctionForFragment end");
    }


}
