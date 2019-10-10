package com.zhumj.recyclerviewhelperdemo.utils;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by zhumj on 2017/3/13.
 * Function:
 */
public class AutoSwipeRefresUtil {
    public static void openSwipeRefres(final SwipeRefreshLayout swipeRefreshLayout){
        if (swipeRefreshLayout == null || swipeRefreshLayout.isRefreshing()) return;
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    public static void closeSwipeRefres(final SwipeRefreshLayout swipeRefreshLayout){
        if (swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing()) return;
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
