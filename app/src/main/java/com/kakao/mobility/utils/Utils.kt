package com.kakao.mobility.utils

import android.animation.Animator
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

fun createAnimatorEndListener(onAnimatorEnd : (() -> Unit)) = object : Animator.AnimatorListener{
    override fun onAnimationEnd(animation: Animator?) = onAnimatorEnd.invoke()
    override fun onAnimationStart(animation: Animator?) {
    }
    override fun onAnimationCancel(animation: Animator?) {
    }
    override fun onAnimationRepeat(animation: Animator?) {
    }
}

@BindingAdapter("imageUrl")
fun AppCompatImageView.imageUrl(url: String?) = url?.let {
    Glide.with(context).load(it).into(this)
}

@BindingAdapter("verticalDivider")
fun RecyclerView.verticalDivider(drawable: Drawable){
    addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL).apply {
        setDrawable(drawable)
    })
}

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
