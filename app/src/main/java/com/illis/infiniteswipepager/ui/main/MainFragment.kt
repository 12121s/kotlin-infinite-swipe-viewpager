package com.illis.infiniteswipepager.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.illis.infiniteswipepager.R
import com.illis.infiniteswipepager.data.Banner
import com.illis.infiniteswipepager.ui.InfinitePager
import com.illis.infiniteswipepager.ui.ImageLoadingService
import com.illis.infiniteswipepager.ui.adapter.BannerAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var infinitePager : InfinitePager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        InfinitePager.init(ImageLoadingService())
        val adapter = BannerAdapter().apply {
            setItemList(
                listOf(
                    Banner("https://static.chotot.com.vn/storage/admin-centre/buyer_collection_y_homepage_banner/buyer_collection_y_homepage_banner_1577353107451.jpg"),
                    Banner("https://static.chotot.com.vn/storage/admin-centre/buyer_collection_y_homepage_banner/buyer_collection_y_homepage_banner_1577355774976.jpg"),
                    Banner("https://static.chotot.com.vn/storage/admin-centre/buyer_collection_y_homepage_banner/buyer_collection_y_homepage_banner_1577353202973.jpg")
                )
            )
        }
        infinitePager = view.findViewById(R.id.infinitePager)
        infinitePager.setAdapter(adapter)
        infinitePager.isAutoScroll(false)
    }

    override fun onPause() {
        super.onPause()
        infinitePager.stopTimer()
    }

}