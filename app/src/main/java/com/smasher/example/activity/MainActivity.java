package com.smasher.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.smasher.core.log.Logger;
import com.smasher.example.R;
import com.smasher.widget.state.StateViewHelper;
import com.smasher.widget.stepper.SnappingStepper;
import com.smasher.widget.stepper.ValueChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.banner_example)
    Button mBannerExample;
    @BindView(R.id.struct_example)
    Button mStructExample;
    @BindView(R.id.dialog_example)
    Button mDialogExample;
    @BindView(R.id.stepper)
    SnappingStepper stepper;

    private StateViewHelper mStateViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mStateViewHelper = new StateViewHelper(mContainer);
        mStateViewHelper.onReloadClickListener(this);
        mStateViewHelper.stateLoading();


        stepper.setEditAble(true);

        mContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStateViewHelper.stateNormal();
            }
        }, 2000);

        stepper.setOnValueChangeListener(new ValueChangeListener() {

            @Override
            public void onValueChange(View view, int value) {
                Logger.d("value:" + value);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_state, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stateNormal://显示数据视图
                mStateViewHelper.stateNormal();
                break;
            case R.id.stateLoading://显示加载中视图
                mStateViewHelper.stateLoading();
                break;
            case R.id.stateEmpty://显示空数据视图
                mStateViewHelper.stateEmpty();
                break;
            case R.id.stateNetError://显示网络错误视图
                mStateViewHelper.stateNetError();
                break;
            case R.id.stateError://显示加载错误视图
                mStateViewHelper.stateError();
                break;
        }
        return true;
    }

    @OnClick({R.id.banner_example, R.id.struct_example, R.id.dialog_example})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.banner_example:
                break;
            case R.id.struct_example:
                intent.setClass(this, StructActivity.class);
                break;
            case R.id.dialog_example:
                intent.setClass(this, DialogsActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
