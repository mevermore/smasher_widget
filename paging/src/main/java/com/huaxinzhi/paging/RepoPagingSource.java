package com.huaxinzhi.paging;

import androidx.annotation.NonNull;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import org.jetbrains.annotations.Nullable;

import kotlin.coroutines.Continuation;

/**
 * ClassName:RepoPagingSource
 * Package:com.huaxinzhi.paging
 * Description:
 * date:  2021/7/1 11:24
 */
public class RepoPagingSource extends PagingSource<Integer, Object> {

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Object> pagingState) {
        return null;
    }

    @Nullable
    @Override
    public Object load(@NonNull LoadParams<Integer> loadParams, @NonNull Continuation<? super LoadResult<Integer, Object>> continuation) {
//        continuation.resumeWith();
        return null;
    }

    // int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
    //        int pageSize = loadParams.getLoadSize();



}
