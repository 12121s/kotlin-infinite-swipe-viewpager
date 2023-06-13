/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui

import android.widget.ImageView
import com.bumptech.glide.Glide


class ImageLoadingService : ImageLoadingInterface {

    override fun loadImage(url: String, imageView: ImageView) {
        Glide.with(imageView).load(url).into(imageView)
    }

    override fun loadImage(resource: Int, imageView: ImageView) {
        Glide.with(imageView).load(resource).into(imageView)
    }

    override fun loadImage(url: String, placeHolder: Int, errorDrawable: Int, imageView: ImageView) {
        Glide.with(imageView).load(url).placeholder(placeHolder).error(errorDrawable).into(imageView)
    }
}