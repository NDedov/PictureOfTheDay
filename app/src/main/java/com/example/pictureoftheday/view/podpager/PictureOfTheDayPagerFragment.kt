package com.example.pictureoftheday.view.podpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayPagerBinding
import com.example.pictureoftheday.utils.TDBY
import com.example.pictureoftheday.utils.TODAY
import com.example.pictureoftheday.utils.YESTERDAY
import com.google.android.material.tabs.TabLayoutMediator


class PictureOfTheDayPagerFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayPagerBinding? = null
    private val binding: FragmentPictureOfTheDayPagerBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureOfTheDayPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        setTabs()
    }

    private fun setTabs() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                TODAY -> {
                    getString(R.string.today_tab_text)
                }
                YESTERDAY -> {
                    getString(R.string.yesterday_tab_text)
                }
                TDBY -> {
                    getString(R.string.tdby_tab_text)
                }
                else -> {
                    getString(R.string.today_tab_text)
                }
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

sealed class Days{
    object Today:Days()
    object Yesterday:Days()
    object TDBY: Days()
}