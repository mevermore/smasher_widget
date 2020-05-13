package com.smasher.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.smasher.example.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.banner_example)
    Button mBannerExample;
    @BindView(R.id.struct_example)
    Button mStructExample;
    @BindView(R.id.dialog_example)
    Button mDialogExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
}
