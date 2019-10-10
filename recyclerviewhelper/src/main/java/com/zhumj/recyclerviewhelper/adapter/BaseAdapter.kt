package com.zhumj.recyclerviewhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhumj.recyclerviewhelper.common.LoadMoreStatus
import com.zhumj.recyclerviewhelper.holder.BaseViewHolder
import com.zhumj.recyclerviewhelper.view.empty.BaseEmptyView
import com.zhumj.recyclerviewhelper.view.empty.SimpleEmptyView
import com.zhumj.recyclerviewhelper.view.loadmore.BaseLoadMoreView
import com.zhumj.recyclerviewhelper.view.loadmore.SimpleLoadMoreView
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

abstract class BaseAdapter<T, K : BaseViewHolder>(private val dates: ArrayList<T>, private val isLoadMoreEnable: Boolean = false): RecyclerView.Adapter<K>() {

    companion object {
        const val TYPE_LOAD_MORE_VIEW = -1
        const val TYPE_EMPTY_VIEW = -2
    }

//    var isLoadMoreEnable: Boolean = false
    var isLoading: Boolean = false

    var loadMoreView: BaseLoadMoreView = SimpleLoadMoreView()
    var emptyView: BaseEmptyView = SimpleEmptyView()

    override fun getItemCount(): Int {
        return if (dates.size <= 0) {
            1
        } else {
            dates.size + getLoadMoreViewCount()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dates.size <= 0) {
            TYPE_EMPTY_VIEW
        } else {
            if (isLoadMoreEnable && position == itemCount-1) {
                TYPE_LOAD_MORE_VIEW
            } else {
                super.getItemViewType(position)
            }
        }
    }

    private fun getLoadMoreViewCount(): Int = if (isLoadMoreEnable) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        return when (viewType) {
            TYPE_EMPTY_VIEW -> {
                createBaseViewHolder(getItemView(parent, emptyView.getLayoutId()))
            }
            TYPE_LOAD_MORE_VIEW -> {
                createBaseViewHolder(getItemView(parent, loadMoreView.getLayoutId()))
            }
            else -> {
                createBaseViewHolder(getItemView(parent, getItemViewLayoutResId()))
            }
        }
    }

    override fun onBindViewHolder(holder: K, position: Int) {
        when (holder.itemViewType) {
            TYPE_EMPTY_VIEW -> {
                emptyView.convert(holder)
            }
            TYPE_LOAD_MORE_VIEW -> {
                loadMoreView.convert(holder)
            }
            else -> {
                convert(holder, dates[position])
            }
        }
    }

    private fun getItemView(parent: ViewGroup, @LayoutRes layoutResId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    }

    /**
     * StaggeredGridLayoutManager模式时，LoadMoreView 和 EmptyView 可占据一行
     */
    override fun onViewAttachedToWindow(holder: K) {
        super.onViewAttachedToWindow(holder)
        holder.itemViewType
        if (holder.itemViewType == TYPE_EMPTY_VIEW || holder.itemViewType == TYPE_LOAD_MORE_VIEW) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    /**
     * GridLayoutManager模式时，LoadMoreView 和 EmptyView 可占据一行
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == TYPE_EMPTY_VIEW || getItemViewType(position) == TYPE_LOAD_MORE_VIEW) {
                        layoutManager.spanCount
                    } else {
                        1
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun createBaseViewHolder(view: View): K {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        val k: K?
        // 泛型擦除会导致z为null
        k = if (z == null) {
            BaseViewHolder(view) as K
        } else {
            createGenericKInstance(z, view)
        }
        return k ?: BaseViewHolder(view) as K
    }

    @Suppress("UNCHECKED_CAST")
    private fun createGenericKInstance(z: Class<*>, view: View): K? {
        try {
            // inner and unstatic class
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                val constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as K
            } else {
                val constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as K
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    if (BaseViewHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && BaseViewHolder::class.java.isAssignableFrom(rawType)) {
                        return rawType
                    }
                }
            }
        }
        return null
    }

    fun setEmptyView(emptyView: BaseEmptyView) : BaseAdapter<T, K> {
        this.emptyView = emptyView
        return this
    }

    fun setLoadMoreView(loadMoreView: BaseLoadMoreView) : BaseAdapter<T, K> {
        this.loadMoreView = loadMoreView
        return this
    }

    fun setLoadMoreStatus(status: LoadMoreStatus) {
        if (!isLoadMoreEnable) return
        loadMoreView.mLoadMoreStatus = status
        isLoading = status == LoadMoreStatus.STATUS_LOADING_LOADING
        notifyItemChanged(itemCount - 1)
    }

    fun setNewData(newDates: ArrayList<T>) {
        this.dates.clear()
        this.dates.addAll(newDates)
        if (isLoadMoreEnable) {
            isLoading = false
            loadMoreView.mLoadMoreStatus = LoadMoreStatus.STATUS_LOADING_DEFAULT
        }
        notifyDataSetChanged()
    }

    fun addDates(newDates: ArrayList<T>) {
        dates.addAll(newDates)
        notifyItemRangeInserted(dates.size - newDates.size, newDates.size)
        compatibilityDataSizeChanged(newDates.size)
    }

    private fun compatibilityDataSizeChanged(size: Int) {
        if (dates.size == size) {
            notifyDataSetChanged()
        }
    }

    @LayoutRes
    abstract fun getItemViewLayoutResId(): Int

    abstract fun convert(holder: K, itemData: T)

}