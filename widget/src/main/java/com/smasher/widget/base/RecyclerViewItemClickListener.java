package com.smasher.widget.base;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 为recyclerview 定制的点击事件监听器
 *
 * @author matao
 * @date 2019/3/28
 */
public class RecyclerViewItemClickListener extends RecyclerView.SimpleOnItemTouchListener {


    private OnItemClickListener mListener;
    private GestureDetectorCompat mGestureDetector;

    public RecyclerViewItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        if (mGestureDetector == null) {
            initGestureDetector(rv);
        }

        return mGestureDetector.onTouchEvent(e);
    }

    private void initGestureDetector(final RecyclerView recyclerView) {
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {


            /**
             * 单击事件
             * @param e MotionEvent
             * @return boolean
             */
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mListener != null) {
                    mListener.onItemClick(childView, recyclerView.getChildLayoutPosition(childView));
                    return true;
                }
                return false;
            }


            /**
             * 长按事件
             * @param e MotionEvent
             */
            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView, recyclerView.getChildLayoutPosition(childView));
                }
            }


            /**
             * 双击事件
             * @param e MotionEvent
             * @return boolean
             */
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && mListener != null) {
                        mListener.onItemDoubleClick(childView, recyclerView.getChildLayoutPosition(childView));
                        return true;
                    }
                }
                return false;
            }
        });

    }

    /**
     * RecyclerView的Item点击事件监听接口
     */
    public interface OnItemClickListener {

        /**
         * 当ItemView的单击事件触发时调用
         *
         * @param view     view
         * @param position position
         */
        void onItemClick(View view, int position);

        /**
         * 当ItemView的长按事件触发时调用
         *
         * @param view     view
         * @param position position
         */
        void onItemLongClick(View view, int position);

        /**
         * 当ItemView的双击事件触发时调用
         *
         * @param view     view
         * @param position position
         */
        void onItemDoubleClick(View view, int position);


    }
}
