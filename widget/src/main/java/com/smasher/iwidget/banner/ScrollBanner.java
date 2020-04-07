package com.smasher.iwidget.banner;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.viewpager.widget.ViewPager.PageTransformer;

import com.smasher.iwidget.banner.callback.CreateViewCaller;
import com.smasher.iwidget.banner.core.BaseBanner;
import com.smasher.iwidget.banner.pager.BannerPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * @author lin
 * @date 2018/1/4
 */

public class ScrollBanner extends BaseBanner<ScrollBanner> implements OnPageChangeListener {


    private BannerPager mBannerPage;
    private List<View> mItemViews;
    private HandlerTask mHandlerTask;

    private OnPageChangeListener onPageChangeListener;

    private BannerPagerAdapter mPagerAdapter;

    public ScrollBanner(@NonNull Context context) {
        this(context, null);
    }

    public ScrollBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
        mHandler = new Handler();
        mHandlerTask = new HandlerTask(this);
        mBannerPage = new BannerPager(getContext());
        mBannerPage.setVertical(isVertical);
        mBannerPage.setFocusable(true);
        mBannerPage.addOnPageChangeListener(this);
        addView(mBannerPage, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

    }


    /**
     * 设置转换动画
     *
     * @param reverseDrawingOrder reverseDrawingOrder
     * @param transformer         transformer
     * @return Banner
     */
    public ScrollBanner setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        if (mBannerPage != null) {
            mBannerPage.setPageTransformer(reverseDrawingOrder, transformer);
        }
        return this;
    }

    public ScrollBanner setPageTransformer(PageTransformer transformer) {
        return setPageTransformer(true, transformer);
    }


    /**
     * 设置垂直滚动 ，此时PageTransformer会被重置
     * （原因，水平的PageTransformer可能不是你需要的,所以会重置成banner提供的垂直的PageTransformer。）
     *
     * @param orientation 滚动方向 HORIZONTAL VERTICAL
     * @return Banner
     */
    @Override
    public ScrollBanner setOrientation(int orientation) {
        this.isVertical = orientation == VERTICAL;
        if (mBannerPage != null) {
            mBannerPage.setVertical(this.isVertical);
        }

        return this;
    }


    /**
     * 循环滚动
     *
     * @param loop true
     * @return ScrollBanner
     */
    @Override
    public ScrollBanner setLoop(boolean loop) {
        isLoop = loop;
        int dataSize = getBannerData().size();
        int viewSize = getItemViews().size();
        if (dataSize != viewSize) {
            if (viewSize < this.mineLoopLimitItem) {
                return this;
            }
            getItemViews().remove(viewSize - 1);
            getItemViews().remove(0);
            PagerAdapter adapter = mBannerPage.getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        return this;
    }

    public ScrollBanner setScroll(boolean scroll) {
        this.isScroll = scroll;
        this.mBannerPage.setCanScroll(scroll);
        return this;
    }


    /**
     * BannerPager  onPageChangeListener
     *
     * @param onPageChangeListener onPageChangeListener
     * @return Banner
     */
    public ScrollBanner setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        return this;
    }

    /**
     * BannerPager extends viewpager
     *
     * @return BannerPager
     */
    public BannerPager getPageView() {
        return mBannerPage;
    }

    public ScrollBanner setOffscreenPageLimit(int limit) {
        this.getPageView().setOffscreenPageLimit(limit);
        return this;
    }


    @Override
    public <T> void execute(List<T> imagesData) {
        stopAutoPlay();
        getItemViews().clear();
        getBannerData().clear();
        if (imagesData != null && imagesData.size() > 0) {
            getBannerData().addAll(imagesData);
        }
        generateItemViews();
        if (mIndicatorAble != null) {
            mIndicatorAble.initIndicator(getBannerData().size());
        }

        mBannerPage.setOffscreenPageLimit(getBannerData().size());
        mPagerAdapter = new BannerPagerAdapter();
        mBannerPage.setAdapter(mPagerAdapter);
        if (getBannerData().size() > 0) {
            mCurrentItem = isLoop ? 1 : 0;
            mBannerPage.setCurrentItem(mCurrentItem);
        }

        mBannerPage.setCanScroll(this.isScroll);
        startAutoPlay();
    }

    /**
     * 返回当前page 索引
     *
     * @return int
     */
    @Override
    public int getCurrentItem() {
        return positionIndex(mCurrentItem);
    }

    @Override
    public void setCurrentItem(int index) {
        mBannerPage.setCurrentItem(index);
    }

    @Override
    public void startAutoPlay() {
        if (isAutoPlay && isLoop && getBannerData().size() >= mineLoopLimitItem) {
            mHandler.removeCallbacks(mHandlerTask);
            mHandler.postDelayed(mHandlerTask, interval);
        }
    }

    @Override
    public void stopAutoPlay() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    @SuppressWarnings("unchecked")
    private void generateItemViews() {
        int size = getBannerData().size();
        if (size <= 0) {
            return;
        }

        for (int i = 0; i <= (isLoop ? size + 1 : size - 1); ++i) {
            if (this.defBannerView) {
                this.createViewCallBack = CreateViewCaller.build();
            }

            View view = createViewCallBack.createView(getContext(), null, 0);
            getItemViews().add(view);
            final int index = positionIndex(i);
            if (bindViewCallBack != null && getBannerData().size() > 0) {
                if (defBannerView) {
                    bindViewCallBack.bindView(CreateViewCaller.findImageView(view), getBannerData().get(index), index);
                } else {
                    bindViewCallBack.bindView(view, getBannerData().get(index), index);
                }
            }

            if (onClickBannerListener != null && getBannerData().size() > 0) {
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (defBannerView) {
                            onClickBannerListener.onClickBanner(CreateViewCaller.findImageView(view), getBannerData().get(index), index);
                        } else {
                            onClickBannerListener.onClickBanner(view, getBannerData().get(index), index);
                        }
                    }
                });
            }
        }
    }


    @Override
    protected int positionIndex(int position) {
        if (!isLoop) {
            return position;
        }
        int count = getBannerData().size();
        int index = position - 1;
        if (position == 0) {
            index = count - 1;
        } else if (position == count + 1) {
            index = 0;
        }
        return index;
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.onBannerScrollStateChanged(state);
        }
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
            case ViewPager.SCROLL_STATE_DRAGGING:
                computeCurrentItem();
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
            default:
                break;
        }

    }

    private void computeCurrentItem() {
        if (!isLoop) {
            return;
        }
        int size = getBannerData().size();
        if (mCurrentItem == size + 1) {
            mBannerPage.setCurrentItem(mCurrentItem = 1, false);
        } else if (mCurrentItem == 0) {
            mBannerPage.setCurrentItem(mCurrentItem = size, false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        position = positionIndex(position);
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        if (mIndicatorAble != null) {
            mIndicatorAble.onBannerScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentItem = position;
        position = positionIndex(position);
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        if (mIndicatorAble != null && getBannerData().size() > 0) {
            mIndicatorAble.onBannerSelected(position, getBannerData().size(), getBannerData().get(position));
        }
    }


    private List<View> getItemViews() {
        if (mItemViews == null) {
            mItemViews = new ArrayList<>();
            if (mPagerAdapter != null) {
                mPagerAdapter.notifyDataSetChanged();
            }
        }
        return mItemViews;
    }


    private class BannerPagerAdapter extends PagerAdapter {
        private BannerPagerAdapter() {

        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return getItemViews().size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = container.findViewWithTag(position);
            if (view == null) {
                view = getItemViews().get(position);
                view.setTag(position);
                container.addView(getItemViews().get(position));
            }

            view.setVisibility(VISIBLE);
            return view;
        }

        @Override
        public void finishUpdate(@NonNull ViewGroup container) {
            super.finishUpdate(container);
            View view = container.findViewWithTag(mBannerPage.getCurrentItem());
            if (view != null && getPageView().getOffscreenPageLimit() > mBannerPage.getCurrentItem()) {
                view.setEnabled(true);
            }
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            view.setVisibility(INVISIBLE);
        }
    }

    private class HandlerTask implements Runnable {
        WeakReference<ScrollBanner> bannerRef;

        HandlerTask(ScrollBanner banner) {
            bannerRef = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            ScrollBanner banner = bannerRef.get();
            if (banner == null) {
                return;
            }
            int size = banner.getBannerData().size();
            if (size > mineLoopLimitItem && banner.isAutoPlay && banner.isLoop) {
                banner.mCurrentItem = banner.mCurrentItem % (size + 1) + 1;
                if (banner.mCurrentItem == 1) {
                    banner.mBannerPage.setCurrentItem(banner.mCurrentItem, false);
                    banner.mHandler.post(this);
                } else {
                    banner.mBannerPage.setCurrentItem(banner.mCurrentItem);
                    banner.mHandler.postDelayed(this, banner.interval);
                }
            }
        }
    }
}
