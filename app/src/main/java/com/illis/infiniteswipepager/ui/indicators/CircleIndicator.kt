/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui.indicators

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.illis.infiniteswipepager.R


@SuppressLint("ViewConstructor")
class CircleIndicator(context: Context, indicatorSize: Int) : HeroIndicator(context, indicatorSize) {
    init {
        background = ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_unselected, null)
    }

    override fun onCheckedChange(isChecked: Boolean) {
        val drawableId = if (isChecked) R.drawable.indicator_circle_selected else R.drawable.indicator_circle_unselected
        background = ResourcesCompat.getDrawable(resources, drawableId, null)
    }
}