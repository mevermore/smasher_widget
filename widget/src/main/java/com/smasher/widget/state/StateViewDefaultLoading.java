package com.smasher.widget.state;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.smasher.widget.R;

/**
 * function:默认状态视图：加载中
 *
 * <p>
 * Created by Leo on 2017/11/23.
 */
/*package*/ class StateViewDefaultLoading extends LinearLayout {
    public StateViewDefaultLoading(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.state_view_layout_loading, this, true);
    }
}
