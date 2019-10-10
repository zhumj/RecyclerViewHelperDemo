package com.zhumj.recyclerviewhelper.view.empty

import androidx.annotation.LayoutRes
import com.zhumj.recyclerviewhelper.holder.BaseViewHolder

abstract class BaseEmptyView {

    open fun convert(holder: BaseViewHolder) {

    }

    /**
     * load more layout
     * @return
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
}