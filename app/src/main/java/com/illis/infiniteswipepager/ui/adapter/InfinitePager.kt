/**
 * Created by laivantrach1190@gmail.com
 * Copyright (c) 2019 . All rights reserved.
 */
package com.illis.infiniteswipepager.ui.adapter

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.illis.infiniteswipepager.R
import com.illis.infiniteswipepager.ui.ImageLoadingInterface
import com.illis.infiniteswipepager.ui.indicators.IndicatorsGroup
import com.trach.herobannerlib.config.Config
import java.util.*

class InfinitePager : FrameLayout {

    private lateinit var config: Config
    private lateinit var viewPager2: ViewPager2
    private lateinit var timer: Timer

    private var adapter: InfinitePagerAdapter<*>? = null
    private var indicatorsGroup: IndicatorsGroup? = null

    private var currentPagePosition = 0

    constructor(context: Context) : super(context) {
        initViews(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initViews(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initViews(attrs)
    }

    private fun initViews(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InfinitePager)
        config = Config.Builder()
            .infinite(typedArray.getBoolean(R.styleable.InfinitePager_is_infinite, false))
            .autoScroll(typedArray.getBoolean(R.styleable.InfinitePager_is_auto_scroll, false))
            .indicatorSize(
                typedArray.getDimensionPixelSize(
                    R.styleable.InfinitePager_indicator_size,
                    12
                )
            )
            .showIndicator(
                typedArray.getBoolean(
                    R.styleable.InfinitePager_is_show_indicator,
                    false
                )
            )
            .interval(typedArray.getInt(R.styleable.InfinitePager_interval, 1500).toLong())
            .build(context)
        typedArray.recycle()
        setupViews()
    }

    private fun setupViews() {
        timer = Timer()
        if (config.isShowIndicator) {
            indicatorsGroup = IndicatorsGroup(
                context,
                ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_selected, null),
                ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_unselected, null),
                config.indicatorSize
            )
        }
        viewPager2 = ViewPager2(context)
        viewPager2.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {
                if (state == SCROLL_STATE_IDLE) {
                    if (config.isInfinite) {
                        if (viewPager2.adapter == null) return
                        val itemCount = viewPager2.adapter?.itemCount ?: 0
                        if (itemCount < 2) {
                            return
                        }
                        val index = viewPager2.currentItem
                        if (index == 0) {
                            viewPager2.setCurrentItem(itemCount - 2, false)
                        } else if (index == itemCount - 1) {
                            viewPager2.setCurrentItem(1, false)
                        }
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                onInfinitePagerChange(position)
            }
        })
    }

    fun setAdapter(adapter: InfinitePagerAdapter<*>) {
        if (::viewPager2.isInitialized) {
            this.adapter = adapter
            this.adapter?.imageViewLayoutParams = viewPager2.layoutParams

            if (indexOfChild(viewPager2) == -1) {
                addView(viewPager2)
            }

            viewPager2.isNestedScrollingEnabled = false

            adapter.setItemTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    stopTimer()
                } else if (event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP) {
                    startTimer()
                }
                false
            }

            viewPager2.adapter = adapter

            if (config.isInfinite) {
                viewPager2.setCurrentItem(1, false)
            }

            if (indicatorsGroup != null && adapter.getListCount() > 1) {
                if (indexOfChild(indicatorsGroup) == -1) {
                    addView(indicatorsGroup)
                }
                indicatorsGroup!!.setIndicators(adapter.getListCount())
                indicatorsGroup!!.onIndicatorChange(0)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun refreshIndicators() {
        indicatorsGroup
            ?: throw IllegalStateException("Indicators group must be not null!")
        val adapter = adapter
            ?: throw IllegalStateException("Adapter must be not null!")

        if (adapter.getListCount() > 1) {
            if (indexOfChild(indicatorsGroup) != -1) {
                removeView(indicatorsGroup)
            }

            indicatorsGroup = IndicatorsGroup(
                context,
                ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_selected, null),
                ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_unselected, null),
                config.indicatorSize
            )
            addView(indicatorsGroup)
            indicatorsGroup?.setIndicators(adapter.getListCount())
            indicatorsGroup?.onIndicatorChange(0)

            invalidate()
        }
    }

    fun onInfinitePagerChange(position: Int) {
        Log.d("InfinitePager", "onInfinitePagerChange() --->>> position = [$position]")
        currentPagePosition = position
        val adapter = adapter ?: return
        val userSlidePosition = adapter.getListPosition(position)
        indicatorsGroup?.onIndicatorChange(userSlidePosition)
    }

    interface IndicatorChangeListener {
        fun onIndicatorChange(position: Int)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTimer()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }


    fun startTimer() {
        if (config.interval > 0) {
            stopTimer()
            timer = Timer()
            timer.schedule(SliderTimerTask(), config.interval, config.interval)
        }
    }

    private inner class SliderTimerTask : TimerTask() {
        override fun run() {
            (context as? Activity)?.runOnUiThread {
                val itemCount = viewPager2.adapter?.itemCount ?: 0
                if (viewPager2.adapter == null || !config.isAutoScroll ||
                    itemCount < 2 && imageLoadingInterface != null
                ) return@runOnUiThread

                if (!config.isInfinite && itemCount - 1 == currentPagePosition) {
                    currentPagePosition = 0
                } else {
                    currentPagePosition++
                }
                viewPager2.setCurrentItem(currentPagePosition, true)
            }
        }
    }

    fun stopTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
            timer.purge()
        }
    }

    private fun resetAutoScroll() {
        stopTimer()
        startTimer()
    }

    fun setInterval(interval: Long) {
        config = config.newBuilder()
            .interval(interval)
            .build(context)
        resetAutoScroll()
    }

    fun setIndicatorSize(size: Int) {
        config = config.newBuilder()
            .indicatorSize(size)
            .build(context)
        refreshIndicators()
    }

    fun isInfinite(checked: Boolean) {
        config = config.newBuilder()
            .infinite(checked)
            .build(context)
        adapter?.isInfinite = checked
        adapter?.notifyDataSetChanged()
        resetAutoScroll()
    }

    fun isAutoScroll(isAutoScroll: Boolean) {
        config = config.newBuilder()
            .autoScroll(isAutoScroll)
            .build(context)
        resetAutoScroll()
    }

    fun showIndicators(isShow: Boolean) {
        config = config.newBuilder()
            .showIndicator(isShow)
            .build(context)
        indicatorsGroup?.visibility = if (isShow) View.VISIBLE else View.GONE
        invalidate()
    }

    companion object {

        private var imageLoadingInterface: ImageLoadingInterface? = null

        fun init(imageLoadingService: ImageLoadingInterface) {
            InfinitePager.imageLoadingInterface = imageLoadingService
        }

        fun getImageLoadingInterface(): ImageLoadingInterface {
            return imageLoadingInterface
                ?: throw IllegalStateException("ImageLoadingInterface must be not null, you should call init method first")
        }
    }
}