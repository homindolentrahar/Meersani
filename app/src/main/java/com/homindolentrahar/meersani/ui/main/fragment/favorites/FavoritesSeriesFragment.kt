package com.homindolentrahar.meersani.ui.main.fragment.favorites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.homindolentrahar.meersani.R

/**
 * A simple [Fragment] subclass.
 */
class FavoritesSeriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_series, container, false)
    }


}
