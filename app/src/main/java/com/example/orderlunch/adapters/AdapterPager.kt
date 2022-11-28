package com.example.orderlunch.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.orderlunch.pages.chef.*

class AdapterPager(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val pageCount: Int
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentHome()
            1 -> FragmentBusiness()
            2 -> FragmentCalculate()
            3 -> FragmentStatistics()
            else -> FragmentProfile()
        }
    }


}