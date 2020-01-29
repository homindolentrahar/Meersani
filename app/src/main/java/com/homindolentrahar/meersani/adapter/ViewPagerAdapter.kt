package com.homindolentrahar.meersani.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.homindolentrahar.meersani.R
import com.homindolentrahar.meersani.ui.main.fragment.favorites.FavoritesMoviesFragment
import com.homindolentrahar.meersani.ui.main.fragment.favorites.FavoritesSeriesFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val listFragment = mutableListOf<Fragment>()
    private val listTitle = mutableListOf<String>()

    fun setFavoritesPage(context: Context) {
        listFragment.apply {
            add(FavoritesMoviesFragment())
            add(FavoritesSeriesFragment())
        }
        listTitle.apply {
            add(context.getString(R.string.movies))
            add(context.getString(R.string.series))
        }
    }

    override fun getItem(position: Int): Fragment = listFragment[position]

    override fun getCount(): Int = listFragment.size

    override fun getPageTitle(position: Int): CharSequence? = listTitle[position]
}