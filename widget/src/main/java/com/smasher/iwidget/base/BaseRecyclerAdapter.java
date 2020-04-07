package com.smasher.iwidget.base;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author moyu
 */
public abstract class BaseRecyclerAdapter<T, VH extends BaseRecyclerViewHolder<T>> extends RecyclerView.Adapter<VH> {

    public static final String TAG = "Adapter";
    protected Context mContext;
    protected List<T> mList;
    protected OnItemClickListener mOnItemClickListener;


    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
    }


    public void setData(List<T> list) {
        if (list != null) {
            mList = list;
        } else {
            mList = new ArrayList<>();
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        return onCreateDefineViewHolder(viewGroup, type);
    }


    /**
     * 创建ViewHolder
     *
     * @param viewGroup viewGroup
     * @param type      type
     * @return VH
     */
    public abstract VH onCreateDefineViewHolder(@NonNull ViewGroup viewGroup, int type);


    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int position) {
        if (mList.isEmpty() || position < 0 || position >= mList.size()) {
            return;
        }
        T item = mList.get(position);
        if (item == null) {
            Log.e(TAG, "data item is null");
            return;
        }
        viewHolder.setOnItemClickListener(mOnItemClickListener);
        viewHolder.setIndex(position);
        viewHolder.setItem(item);
        viewHolder.bindView();
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            Log.e(TAG, "you have not bind data yet!");
            return 0;
        }
        return mList.size();
    }


    public T getItem(int position) {
        if (mList.isEmpty() || position < 0 || position >= mList.size()) {
            return null;
        }
        return mList.get(position);
    }


    public View build(@LayoutRes int resource, ViewGroup viewGroup, boolean attachToRoot) {
        return LayoutInflater.from(mContext).inflate(resource, viewGroup, attachToRoot);
    }
}
