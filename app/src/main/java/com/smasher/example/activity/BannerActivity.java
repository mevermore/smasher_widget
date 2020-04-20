package com.smasher.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smasher.example.R;
import com.smasher.iwidget.banner.RecyclerBanner;
import com.smasher.iwidget.banner.callback.BindViewCallBack;
import com.smasher.iwidget.banner.callback.CreateViewCallBack;
import com.smasher.iwidget.banner.callback.OnClickBannerListener;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    RecyclerBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        banner = findViewById(R.id.banner);
        init();
    }

    private void init() {

        List<Integer> list = new ArrayList<>();
        list.add(new Integer(5));
        list.add(new Integer(6));
        list.add(new Integer(7));
        list.add(new Integer(8));
        list.add(new Integer(9));
        list.add(new Integer(10));


        banner.createView(new CreateViewCallBack() {
            //创建布局
            @Override
            public View createView(Context context, ViewGroup parent, int viewType) {
                return LayoutInflater.from(context).inflate(R.layout.custom_banner_page, null);
            }
        }).bindView(new BindViewCallBack<View, Integer>() {
            //布局处理
            @Override
            public void bindView(View view, Integer data, int position) {

//                ImageView imageView = view.findViewById(R.id.iv_image);
                TextView tvTitle = view.findViewById(R.id.tv_title);

//                Glide.with(view.getContext()).load(data.getUrl()).into(imageView);
                tvTitle.setText("第" + data.intValue() + "位");
            }

        }).setOnClickBannerListener(new OnClickBannerListener<View, Integer>() {
            //点击事件
            @Override
            public void onClickBanner(View view, Integer data, int position) {
                Toast.makeText(view.getContext(), "click position ：" + position + "\n标题：" + data.intValue(), Toast.LENGTH_SHORT).show();
            }

        })
                //填充数据
                .execute(list);

    }


}
