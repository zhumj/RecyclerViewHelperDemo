package com.zhumj.recyclerviewhelperdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zhumj.recyclerviewhelper.common.LoadMoreStatus
import com.zhumj.recyclerviewhelper.listener.OnLoadMoreListener
import com.zhumj.recyclerviewhelperdemo.R
import com.zhumj.recyclerviewhelperdemo.adapter.SimpleAdapter
import com.zhumj.recyclerviewhelperdemo.model.ItemModel
import com.zhumj.recyclerviewhelperdemo.utils.AutoSwipeRefresUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var adapter: SimpleAdapter? = null
    private var itemDates = ArrayList<ItemModel>()
    private var pageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    private fun initView() {
        adapter = SimpleAdapter(ArrayList(), true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener(this)
        recyclerView.addOnScrollListener(object: OnLoadMoreListener(){
            override fun onLoadMore() {
                if (!swipeRefreshLayout.isRefreshing && adapter?.isLoading != true && adapter?.loadMoreView?.mLoadMoreStatus != LoadMoreStatus.STATUS_LOADING_END) {
                    pageIndex += 1
                    adapter?.setLoadMoreStatus(LoadMoreStatus.STATUS_LOADING_LOADING)
                    requestData()
                }
            }
        })
    }

    fun upTopClick(v: View) {
        recyclerView.post {
            recyclerView.scrollToPosition(0)
        }
    }

    override fun onRefresh() {
        if (adapter?.isLoading == false) {
            pageIndex = 0
            adapter?.setLoadMoreStatus(LoadMoreStatus.STATUS_LOADING_DEFAULT)
            requestData()
        } else {
            AutoSwipeRefresUtil.closeSwipeRefres(swipeRefreshLayout)
        }
    }

    private fun requestData() {
        Handler().postDelayed({
            when {
                pageIndex == 3 -> runOnUiThread {
                    adapter?.setLoadMoreStatus(LoadMoreStatus.STATUS_LOADING_FAILED)
                }
                pageIndex > 5 -> runOnUiThread {
                    adapter?.setLoadMoreStatus(LoadMoreStatus.STATUS_LOADING_END)
                }
                else -> {
                    if(pageIndex == 0) {
                        itemDates.clear()
                    }
                    val dates = ArrayList<ItemModel>()
                    for (i in 0 .. Random().nextInt(4)+3) {
                        val model = ItemModel()
                        model.title = "标题${itemDates.size + 1}"
                        model.body = "内容${itemDates.size + 1}"
                        dates.add(model)
                        itemDates.add(model)
                    }
                    runOnUiThread {
                        if(pageIndex == 0) {
                            adapter?.setNewData(dates)
                            AutoSwipeRefresUtil.closeSwipeRefres(swipeRefreshLayout)
                        } else {
                            adapter?.addDates(dates)
                            adapter?.setLoadMoreStatus(LoadMoreStatus.STATUS_LOADING_DEFAULT)
                        }
                    }
                }
            }
        }, 3000)
    }

}
