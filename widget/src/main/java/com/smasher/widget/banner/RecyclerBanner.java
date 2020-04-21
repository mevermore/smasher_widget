package com.smasher.widget.banner;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smasher.widget.banner.callback.CreateViewCaller;
import com.smasher.widget.banner.core.BaseBanner;
import com.smasher.widget.banner.pager.RecyclerViewPager;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * @author Administrator
 */
public class RecyclerBanner extends BaseBanner<RecyclerBanner> {


    private LoopRecyclerViewPager mRecyclerView;
    private RecyclerViewAdapter adapter;
    private HandlerTask task;


    public RecyclerBanner(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecyclerBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
        mHandler = new Handler();
        task = new HandlerTask(this);

        mRecyclerView = new LoopRecyclerViewPager(context);
        mRecyclerView.setSinglePageFling(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);

        onPageChanged(mRecyclerView);
        runDirection(mRecyclerView);

        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void runDirection(RecyclerViewPager mRecyclerView) {
        if (mRecyclerView == null) {
            return;
        }
        if (isVertical) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public RecyclerBanner setOrientation(int orientation) {
        this.isVertical = orientation == VERTICAL;
        runDirection(mRecyclerView);
        return this;
    }


    @Override
    public RecyclerBanner setLoop(boolean loop) {
        this.isLoop = loop;
        return this;
    }

    @Override
    protected int positionIndex(int postion) {

        int count = getBannerData().size();
        int index = 0;
        if (count != 0) {
            index = mCurrentItem % getBannerData().size();
        }
        return index;
    }

    private void onPageChanged(RecyclerViewPager mRecyclerView) {
        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                int size = getBannerData().size();
                mCurrentItem = newPosition;
                if (mIndicatorAble != null && size > 0) {
                    int index = positionIndex(mCurrentItem);
                    mIndicatorAble.onBannerSelected(index, size, getBannerData().get(index));
                }
            }
        });
    }


    /**
     * 返回当前page 索引
     *
     * @return
     */
    @Override
    public int getCurrentItem() {
        return positionIndex(mCurrentItem);
    }

    @Override
    public void setCurrentItem(int index) {
        if (getBannerData().size() == 0) {
            return;
        }
        if (isLoop) {
            int offset = mCurrentItem % getBannerData().size();
            mRecyclerView.scrollToPosition(mCurrentItem + index - offset);
        } else {
            mRecyclerView.scrollToPosition(index);
        }
    }

    /**
     * RecyclerViewPager extends RecyclerView
     *
     * @return RecyclerViewPager
     */
    public RecyclerViewPager getPageView() {
        return mRecyclerView;
    }


    @Override
    public void startAutoPlay() {
        if (isAutoPlay && isLoop && getBannerData().size() > 2) {
            mHandler.removeCallbacks(task);
            mHandler.postDelayed(task, interval);
        }
    }

    @Override
    public void stopAutoPlay() {
        mHandler.removeCallbacks(task);
    }

    @Override
    public void execute(List datas) {
        setBannerData(datas);
    }


    /**
     * 设置数据
     *
     * @param data
     * @return
     */
    private void setBannerData(List data) {
        stopAutoPlay();
        getBannerData().clear();
        if (data != null) {
            getBannerData().addAll(data);
        }

        if (mIndicatorAble != null) {
            if (mIndicatorAble instanceof IndicatorLayout) {
                ((IndicatorLayout) mIndicatorAble).setIndicatorFlow(false);
            }
            mIndicatorAble.initIndicator(data.size());
        }

        mRecyclerView.setCanLoop(isLoop);
        adapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(adapter);

        startAutoPlay();
        adapter.notifyDataSetChanged();
    }


    /**
     * RecyclerView适配器
     */
    private class RecyclerViewAdapter extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (defBannerView) {
                createViewCallBack = CreateViewCaller.build();
            }

            View view = createViewCallBack.createView(parent.getContext(), parent, viewType);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickBannerListener != null) {
                        int index = positionIndex(mCurrentItem);
                        if (defBannerView) {
                            //noinspection unchecked
                            onClickBannerListener.onClickBanner(CreateViewCaller.findImageView(v), getItemData(index), index);
                        } else {
                            //noinspection unchecked
                            onClickBannerListener.onClickBanner(v, getItemData(index), index);
                        }

                    }
                }
            });
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (bindViewCallBack != null) {
                if (defBannerView) {
                    //noinspection unchecked
                    bindViewCallBack.bindView(CreateViewCaller.findImageView(holder.itemView), getItemData(position), position);
                } else {

                    //noinspection unchecked
                    bindViewCallBack.bindView(holder.itemView, getItemData(position), position);
                }
            }
        }

        @Override
        public int getItemCount() {
            return getBannerData().size();
        }
    }


    /**
     * 获取数据
     *
     * @param postion
     * @return
     */
    public Object getItemData(int postion) {
        if (getBannerData() == null || getBannerData().size() <= postion || postion < 0) {
            return null;
        }
        return getBannerData().get(postion);
    }


    /**
     * 定时轮播
     */
    private static class HandlerTask implements Runnable {

        WeakReference<RecyclerBanner> bannerWeakRef;

        HandlerTask(RecyclerBanner recyclerBanner) {
            bannerWeakRef = new WeakReference<RecyclerBanner>(recyclerBanner);
        }

        @Override
        public void run() {
            RecyclerBanner banner = bannerWeakRef.get();
            if (banner == null) {
                return;
            }
            ++banner.mCurrentItem;
            banner.mRecyclerView.smoothScrollToPosition(banner.mCurrentItem);
            banner.mHandler.postDelayed(this, banner.interval);
        }
    }


    public static class LoopRecyclerAdapter<VH extends RecyclerView.ViewHolder>
            extends RecyclerViewPager.RecyclerAdapter<VH> {


        public LoopRecyclerAdapter(RecyclerViewPager viewPager, RecyclerView.Adapter<VH> adapter) {
            super(viewPager, adapter);
        }


        public int getActualItemCount() {
            return super.getItemCount();
        }

        public int getActualItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public long getActualItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            if (getActualItemCount() > 0) {
                return Integer.MAX_VALUE;
            } else {
                return super.getItemCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (getActualItemCount() > 0) {
                return super.getItemViewType(getActualPosition(position));
            } else {
                return 0;
            }
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(getActualPosition(position));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            super.onBindViewHolder(holder, getActualPosition(position));
            // because of getCurrentPosition may return ViewHolder‘s position,
            // so we must reset mPosition if exists.
            //ViewHolderDelegate.setPosition(holder, position);
        }

        public int getActualPosition(int position) {
            int actualPosition = position;
            if (getActualItemCount() > 0 && position >= getActualItemCount()) {
                actualPosition = position % getActualItemCount();
            }
            return actualPosition;
        }
    }


    public static class LoopRecyclerViewPager extends RecyclerViewPager {
        private boolean canLoop = true;

        public void setCanLoop(boolean canLoop) {
            this.canLoop = canLoop;
        }

        public LoopRecyclerViewPager(Context context) {
            this(context, null);
        }

        public LoopRecyclerViewPager(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public LoopRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void setAdapter(Adapter adapter) {
            super.setAdapter(adapter);
            if (canLoop) {
                super.scrollToPosition(getMiddlePosition());
            }
        }

        @Override
        public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
            super.swapAdapter(adapter, removeAndRecycleExistingViews);
            if (canLoop) {
                super.scrollToPosition(getMiddlePosition());
            }
        }

        @Override
        @NonNull
        protected RecyclerViewPager.RecyclerAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {

            if (canLoop) {
                return (adapter instanceof LoopRecyclerAdapter)
                        ? (LoopRecyclerAdapter) adapter
                        : new LoopRecyclerAdapter(this, adapter);
            }
            return super.ensureRecyclerViewPagerAdapter(adapter);


        }


        private int getActualItemCountFromAdapter() {
            return ((LoopRecyclerAdapter) getWrapperAdapter()).getActualItemCount();
        }


        private int getMiddlePosition() {
            int middlePosition = Integer.MAX_VALUE / 2;
            final int actualItemCount = getActualItemCountFromAdapter();
            if (actualItemCount > 0 && middlePosition % actualItemCount != 0) {
                middlePosition = middlePosition - middlePosition % actualItemCount;
            }

            return middlePosition;
        }
    }
}