/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.illis.infiniteswipepager.data.Banner

class BannerAdapter: InfiniteScrollPagerAdapter<Banner>() {

    override fun bindData(holder: RecyclerView.ViewHolder, data: Banner) {
        val viewHolder = holder as? InfiniteScrollPagerViewHolder ?: return
        viewHolder.bindImage(data.url)
    }

}