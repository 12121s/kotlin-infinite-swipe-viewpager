/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoadingInterface {

    fun loadImage(url: String, imageView: ImageView)

    fun loadImage(@DrawableRes resource: Int, imageView: ImageView)

    fun loadImage(url: String, @DrawableRes placeHolder: Int, @DrawableRes errorDrawable: Int, imageView: ImageView)
}