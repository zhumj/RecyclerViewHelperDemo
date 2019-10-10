package com.zhumj.recyclerviewhelperdemo.adapter

import com.zhumj.recyclerviewhelper.adapter.BaseAdapter
import com.zhumj.recyclerviewhelperdemo.holder.ItemHolder
import com.zhumj.recyclerviewhelperdemo.R
import com.zhumj.recyclerviewhelperdemo.model.ItemModel

class SimpleAdapter(dates: ArrayList<ItemModel> = ArrayList(), isLoadMoreEnable: Boolean = false) : BaseAdapter<ItemModel, ItemHolder>(dates, isLoadMoreEnable) {

    override fun getItemViewLayoutResId(): Int = R.layout.item_main

    override fun convert(holder: ItemHolder, itemData: ItemModel) {

        holder
            .setItemHolderText(R.id.tvBody, itemData.body)
            .setText(R.id.tvTitle, itemData.title)

    }
}