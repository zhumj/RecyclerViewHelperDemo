package com.zhumj.recyclerviewhelper.view.loadmore

import com.zhumj.recyclerviewhelper.R
import com.zhumj.recyclerviewhelper.holder.BaseViewHolder

class SimpleLoadMoreView: BaseLoadMoreView() {

    override fun getLayoutId(): Int = R.layout.layout_load_more_view

    override fun getLoadingViewId(): Int = R.id.lnytLoading

    override fun getLoadFailViewId(): Int = R.id.tvLoadFailed

    override fun getLoadEndViewId(): Int = R.id.tvLoadEnd

    override fun convert(holder: BaseViewHolder) {
        super.convert(holder)
    }
}