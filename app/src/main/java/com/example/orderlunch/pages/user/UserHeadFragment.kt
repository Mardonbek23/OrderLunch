package com.example.orderlunch.pages.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.orderlunch.R
import com.example.orderlunch.adapters.AdapterPager
import com.example.orderlunch.adapters.AdapterPagerUser
import com.example.orderlunch.databinding.FragmentUserHeadBinding


class UserHeadFragment : Fragment() {


    lateinit var binding: FragmentUserHeadBinding
    lateinit var adapterPager:AdapterPagerUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHeadBinding.inflate(inflater, container, false)
        setComponents()
        return binding.root
    }

    private fun setComponents() {
        binding.apply {
            adapterPager = AdapterPagerUser(
                childFragmentManager,
                lifecycle,
                5
            )
            viewPager.adapter = adapterPager
            viewPager.isUserInputEnabled = false

            //set bottom navigation
            bottomNavigationView.background = null
            bottomNavigationView.menu.getItem(2).isEnabled = false
            bottomNavigationView.menu.getItem(2).isCheckable = false
            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        viewPager.setCurrentItem(0, false)
                    }
                    R.id.user_order -> {
                        viewPager.setCurrentItem(1, false)
                    }
                    R.id.user_stats -> {
                        viewPager.setCurrentItem(3, false)
                    }
                    R.id.user_profile -> {
                        viewPager.setCurrentItem(4, false)
                    }
                }
                return@setOnItemSelectedListener true
            }

            //FAB
            fab.setOnClickListener {
                bottomNavigationView.menu.getItem(2).isChecked = true
                viewPager.setCurrentItem(2, false)
            }
        }
    }

}