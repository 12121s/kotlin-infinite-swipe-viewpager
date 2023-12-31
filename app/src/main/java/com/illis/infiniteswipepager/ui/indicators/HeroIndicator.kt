/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui.indicators

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.illis.infiniteswipepager.R

abstract class HeroIndicator(context: Context, indicatorSize: Int): ImageView(context) {

    init {
        val margin = resources.getDimensionPixelSize(R.dimen.default_indicator_margin)
        layoutParams = LinearLayout.LayoutParams(indicatorSize, indicatorSize).apply {
            gravity = Gravity.CENTER_VERTICAL
            setMargins(margin, 0, margin, 0)
        }
    }
    abstract fun onCheckedChange(isChecked: Boolean)
}
