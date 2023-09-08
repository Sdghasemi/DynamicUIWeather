package com.hirno.weather.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon

private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
private fun lcm(a: Int, b: Int) = a * (b / gcd(a, b))
fun lcm(input: Set<Int>) = input.reduce { total, next -> lcm(total, next) }

fun View.iconicsDrawable(icon: IIcon) = IconicsDrawable(context, icon)

inline fun GridLayoutManager.spanSizeLookup(crossinline spanSizePredicate: GridLayoutManager.(Int) -> Int) = apply {
    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) = spanSizePredicate(position)
    }
}

fun @receiver:ColorInt Int.toStateList() = ColorStateList.valueOf(this)

val View.inflater: LayoutInflater
    get() = LayoutInflater.from(context)
var View.backgroundRes: Int
    get() = 0
    set(@DrawableRes value) = setBackgroundResource(value)

var TextView.drawableStart: Drawable?
    get() = compoundDrawablesRelative[0]
    set(value) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(value, drawableTop, drawableEnd, drawableBottom)
    }
var TextView.drawableTop: Drawable?
    get() = compoundDrawablesRelative[1]
    set(value) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, value, drawableEnd, drawableBottom)
    }
var TextView.drawableEnd: Drawable?
    get() = compoundDrawablesRelative[2]
    set(value) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, drawableTop, value, drawableBottom)
    }
var TextView.drawableBottom: Drawable?
    get() = compoundDrawablesRelative[3]
    set(value) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, value)
    }
var TextView.textRes: Int
    get() = 0
    set(value) = setText(value)
var TextView.textColorRes: Int
    get() = 0
    set(value) = setTextColor(ContextCompat.getColor(context, value))

var ImageView.imageDrawable: Drawable?
    get() = drawable
    set(value) = setImageDrawable(value)
var ImageView.drawableTintStateList: ColorStateList?
    get() = ImageViewCompat.getImageTintList(this)
    set(value) = ImageViewCompat.setImageTintList(this, value)
var ImageView.drawableTint: Int
    get() = drawableTintStateList?.defaultColor ?: 0
    set(@ColorInt value) {
        drawableTintStateList = value.toStateList()
    }
var ImageView.drawableTintRes: Int
    get() = 0
    set(@ColorRes value) {
        drawableTint = ContextCompat.getColor(context, value)
    }

fun ViewBinding.getString(@StringRes resId: Int, vararg formatArgs: Any) = root.getString(resId, *formatArgs)
fun View.getString(@StringRes resId: Int, vararg formatArgs: Any) = context.getString(resId, *formatArgs)

inline fun <V : View, O : Any> V.showIfNotNull(obj: O?, bindBlock: V.(O) -> Unit) {
    isVisible = if (obj != null) {
        bindBlock(obj)
        true
    } else false
}