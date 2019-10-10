package com.zhumj.recyclerviewhelper.view.loadmore

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.zhumj.recyclerviewhelper.common.LoadMoreStatus
import com.zhumj.recyclerviewhelper.holder.BaseViewHolder

abstract class BaseLoadMoreView {

    var mLoadMoreStatus: LoadMoreStatus = LoadMoreStatus.STATUS_LOADING_DEFAULT

    open fun convert(holder: BaseViewHolder) {
        when (mLoadMoreStatus) {
            LoadMoreStatus.STATUS_LOADING_LOADING -> {
                visibleLoading(holder, true)
                visibleLoadFail(holder, false)
                visibleLoadEnd(holder, false)
            }
            LoadMoreStatus.STATUS_LOADING_FAILED -> {
                visibleLoading(holder, false)
                visibleLoadFail(holder, true)
                visibleLoadEnd(holder, false)
            }
            LoadMoreStatus.STATUS_LOADING_END -> {
                visibleLoading(holder, false)
                visibleLoadFail(holder, false)
                visibleLoadEnd(holder, true)
            }
            else -> {
                visibleLoading(holder, false)
                visibleLoadFail(holder, false)
                visibleLoadEnd(holder, false)
            }
        }
    }

    private fun visibleLoading(holder: BaseViewHolder, visible: Boolean) {
        holder.setGone(getLoadingViewId(), visible)
    }

    private fun visibleLoadFail(holder: BaseViewHolder, visible: Boolean) {
        holder.setGone(getLoadFailViewId(), visible)
    }

    private fun visibleLoadEnd(holder: BaseViewHolder, visible: Boolean) {
        holder.setGone(getLoadEndViewId(), visible)
    }

    /**
     * load more layout
     * @return
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * loading view
     * @return
     */
    @IdRes
    protected abstract fun getLoadingViewId(): Int

    /**
     * load fail view
     * @return
     */
    @IdRes
    protected abstract fun getLoadFailViewId(): Int

    /**
     * load end view
     */
    @IdRes
    protected abstract fun getLoadEndViewId(): Int

}