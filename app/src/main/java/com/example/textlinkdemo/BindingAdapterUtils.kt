package com.example.textlinkdemo

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapterUtils {

    private const val TAG = "BindingAdapter"

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(view: ImageView, url: String?) {
        Log.d(TAG, "setImageUrl $url")
        if (!url.isNullOrEmpty()) {
            view.visibility = View.VISIBLE
            Glide.with(view.context)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.error)
                    .into(view as ImageView)
        } else {
            view.visibility = View.GONE
        }
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImage(view: ImageView, res: Int) {
        view.setImageResource(res)
    }

    @BindingAdapter("imageUrl", "defaultRes", requireAll = false)
    @JvmStatic
    fun setBoth(view: ImageView, url: String?, res:Int?) {
        Log.d(TAG, "setBoth: $url $res")
    }

    @BindingAdapter("defaultRes")
    @JvmStatic
    fun setImageRes(view: ImageView, res: Int?) {
        Log.d(TAG, "setImageRes: $res")
        res?.let {
            view.setImageResource(it)
        }
    }

}