package com.example.orderlunch.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.orderlunch.pages.chef.*
import com.example.orderlunch.pages.user.*

class AdapterPagerUser(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val pageCount: Int
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserHomeFragment()
            1 -> UserOrdersFragment()
            2 -> UserCreateOrderFragment()
            3 -> UserStatisticsFragment()
            else -> UserProfileFragment()
        }
    }


}