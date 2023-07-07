package com.app.test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.test.fragment.LinkFragment
import com.app.test.R
import com.app.test.databinding.ActivityMainBinding
import com.app.test.fragment.CourseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var fragmentManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        replaceFragment(LinkFragment())
        setTab()
    }

    private fun setTab() {
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.link).setIcon(R.drawable.link_icon))
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.courses).setIcon(R.drawable.course_icon))
        binding.tabs.addTab(binding.tabs.newTab())
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.campaigns).setIcon(R.drawable.campaigns_icon))
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.profile).setIcon(R.drawable.profile_icon))

        binding.tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.tabs.selectedTabPosition) {
                    0 -> {
                        replaceFragment(LinkFragment())
                    }
                    1 -> {
                        replaceFragment(CourseFragment())
                    }
                    3 -> {
                        replaceFragment(CourseFragment())
                    }
                    4 -> {
                        replaceFragment(CourseFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (tab.position == 0) tab.setIcon(R.drawable.link_icon)
                else if (tab.position == 1) tab.setIcon(R.drawable.course_icon
                ) else if (tab.position == 3) { tab.setIcon(R.drawable.campaigns_icon)
                } else if (tab.position == 4) tab.setIcon(R.drawable.profile_icon)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}