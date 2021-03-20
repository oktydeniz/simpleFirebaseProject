package com.oktydeniz.instagramklon.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
class Utils {
    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
        }
    }
}
/**     @BindingAdapter("loadImage")
fun loadImage(imageView: ImageView?, url: String?) {
imageView!!.setImageResource(R.drawable.ic_android_black_24dp)
}
 * @set:BindingAdapter("visible")
var View.visible
get() = visibility == VISIBLE
set(value) {
visibility = if (value) VISIBLE else GONE
}
 *
 * @BindingAdapter("imageUrl")
fun ImageView.loadImage(url:String) { ... }
 *
 */