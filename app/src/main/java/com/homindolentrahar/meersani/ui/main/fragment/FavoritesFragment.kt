package com.homindolentrahar.meersani.ui.main.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.adapter.ViewPagerAdapter
import com.homindolentrahar.meersani.util.Constants
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_favorites.*

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Banner setup
        setupBanner()
//        ViewPager Setup
        viewPagerSetup()
    }

    private fun setupBanner() {
        Glide.with(context!!).load(R.drawable.projector)
            .apply(RequestOptions.bitmapTransform(Constants.getBannerOptions())).into(img_banner)
        btn_settings.setOnClickListener { Constants.navigateToSettings(context!!) }
    }

    private fun viewPagerSetup() {
        val pagerAdapter = ViewPagerAdapter(activity?.supportFragmentManager as FragmentManager)
        pagerAdapter.setFavoritesPage(context as Context)
        favorites_view_pager.adapter = pagerAdapter
        favorites_tab_layout.setupWithViewPager(favorites_view_pager)
    }
}
