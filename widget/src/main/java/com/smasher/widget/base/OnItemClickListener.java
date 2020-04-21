package com.smasher.widget.base;

import android.view.View;

/**
 * @author matao
 * @date 2019/5/10
 */
public interface OnItemClickListener {
    /**
     * listener
     *
     * @param view     view
     * @param position position
     */
    void onClick(View view, int position);


    /**
     * onEditFinish
     *
     * @param view     view
     * @param position position
     * @param content  content
     */
    void onEditFinish(View view, int position, String content);
}
