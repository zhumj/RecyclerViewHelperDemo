package com.zhumj.recyclerviewhelper.view.empty

import com.zhumj.recyclerviewhelper.R
import com.zhumj.recyclerviewhelper.holder.BaseViewHolder

class SimpleEmptyView : BaseEmptyView() {
    override fun getLayoutId(): Int = R.layout.layout_base_empty_view

    override fun convert(holder: BaseViewHolder) {
        super.convert(holder)
    }
}