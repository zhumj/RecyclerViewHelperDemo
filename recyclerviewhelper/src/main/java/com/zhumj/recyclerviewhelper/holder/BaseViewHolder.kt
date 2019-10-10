package com.zhumj.recyclerviewhelper.holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mCached = SparseArray<View>()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findViewById(@IdRes resId: Int): T {
        val pos = mCached.indexOfKey(resId)
        val v: View
        if (pos < 0) {
            v = itemView.findViewById(resId)
            mCached.put(resId, v)
        } else {
            v = mCached.valueAt(pos)
        }
        return v as T
    }

    fun setText(@IdRes viewId: Int, text: CharSequence): BaseViewHolder {
        findViewById<TextView>(viewId).text = text
        return this
    }

    fun setText(@IdRes viewId: Int, @StringRes strId: Int): BaseViewHolder {
        findViewById<TextView>(viewId).setText(strId)
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): BaseViewHolder {
        findViewById<ImageView>(viewId).setImageResource(imageResId)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): BaseViewHolder {
        findViewById<View>(viewId).setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseViewHolder {
        findViewById<View>(viewId).setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): BaseViewHolder {
        findViewById<TextView>(viewId).setTextColor(textColor)
        return this
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable): BaseViewHolder {
        findViewById<ImageView>(viewId).setImageDrawable(drawable)
        return this
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): BaseViewHolder {
        findViewById<ImageView>(viewId).setImageBitmap(bitmap)
        return this
    }

    fun setGone(@IdRes viewId: Int, visible: Boolean): BaseViewHolder {
        findViewById<View>(viewId).visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun setVisible(@IdRes viewId: Int, visible: Boolean): BaseViewHolder {
        findViewById<View>(viewId).visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    fun setOnClickListener(@IdRes viewId: Int, listener: View.OnClickListener): BaseViewHolder {
        findViewById<View>(viewId).setOnClickListener(listener)
        return this
    }

}
