/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui.adapter

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.illis.infiniteswipepager.ui.InfinitePager


class InfinitePagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView = itemView as ImageView

    fun bindImage(imageUrl: String?) {
        if (imageUrl != null) {
            InfinitePager.getImageLoadingInterface().loadImage(imageUrl, imageView)
        }
    }

    fun bindImage(@DrawableRes imageResourceId: Int) {
        InfinitePager.getImageLoadingInterface().loadImage(imageResourceId, imageView)
    }

    fun bindImage(url: String, @DrawableRes placeHolder: Int, @DrawableRes error: Int) {
        InfinitePager.getImageLoadingInterface().loadImage(url, placeHolder, error, imageView)
    }

}