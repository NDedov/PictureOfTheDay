package com.example.pictureoftheday.view.podpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pictureoftheday.view.pod.PictureOfTheDayFragment

class ViewPagerAdapter(fr: Fragment) :
    FragmentStateAdapter(fr) {

    private val fragments = arrayOf(
        PictureOfTheDayFragment.newInstance(Days.Today),
        PictureOfTheDayFragment.newInstance(Days.Yesterday),
        PictureOfTheDayFragment.newInstance(Days.TDBY)
    )

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
    override fun getItemCount(): Int {
        return fragments.size
    }
}
