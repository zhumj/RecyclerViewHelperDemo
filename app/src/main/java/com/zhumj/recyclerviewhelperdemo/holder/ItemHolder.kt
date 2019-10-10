package com.zhumj.recyclerviewhelperdemo.holder

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import com.zhumj.recyclerviewhelper.holder.BaseViewHolder

class ItemHolder(itemView: View) : BaseViewHolder(itemView) {
    fun setItemHolderText(@IdRes viewId: Int, text: String): ItemHolder {
        findViewById<TextView>(viewId).text = text
        return this
    }
}