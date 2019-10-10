package com.zhumj.recyclerviewhelper.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class OnLoadMoreListener: RecyclerView.OnScrollListener() {

    // 用来标记是否是向上滑动
    private var isSlidingUpward = false

    override fun onScrollStateChanged(recyclerV: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerV, newState)
        // 当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val layoutManager = recyclerV.layoutManager
            if (layoutManager != null) {
                //获取最后一个完全显示的itemPosition
                val lastItemPosition = findLastVisibleItemPosition(layoutManager)
                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == layoutManager.itemCount - 1 && isSlidingUpward) {
                    //到达底部开始刷新
                    onLoadMore()
                }
            }
        }
    }

    /**
     * 获取最后展示的位置
     */
    private fun findLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int =
        when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val lastPositions = layoutManager.findLastCompletelyVisibleItemPositions(IntArray(layoutManager.spanCount))
                findMax(lastPositions)
            }
            else -> 0
        }

    /**
     * StaggeredGridLayoutManager时，查找position最大的列
     */
    private fun findMax(lastVisiblePositions: IntArray): Int =
        lastVisiblePositions.max() ?: lastVisiblePositions[0]

    override fun onScrolled(recyclerV: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerV, dx, dy)
        isSlidingUpward = dy > 0
    }

    /**
     * 加载更多回调
     */
    abstract fun onLoadMore()

}