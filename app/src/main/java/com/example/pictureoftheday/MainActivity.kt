package com.example.pictureoftheday

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.databinding.ActivityMainBinding
import com.example.pictureoftheday.utils.SP_DB_THEME
import com.example.pictureoftheday.utils.SP_DB_THEME_TAG
import com.example.pictureoftheday.view.BottomSettingsFragment
import com.example.pictureoftheday.view.pod.OnChangeThemeListener
import com.example.pictureoftheday.view.podpager.PictureOfTheDayPagerFragment
import com.example.pictureoftheday.view.WikiFragment

class MainActivity : AppCompatActivity(), OnChangeThemeListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeFromSP()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation(savedInstanceState)
    }

    private fun initBottomNavigation(savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_pod -> { navigateTo(PictureOfTheDayPagerFragment()); true }
                R.id.navigation_wiki -> { navigateTo(WikiFragment()); true }
                R.id.navigation_settings -> { navigateTo(BottomSettingsFragment()); true }
                else -> false
            }
        }
        if (savedInstanceState == null)
            binding.bottomNavigationView.selectedItemId = R.id.navigation_pod
    }

    private fun navigateTo(fr: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_navigation_container, fr)
            .commit()
    }

    private fun setThemeFromSP() {
        val sp = getSharedPreferences(SP_DB_THEME, Context.MODE_PRIVATE)
        val currentTheme = sp.getInt(SP_DB_THEME_TAG, R.style.Theme_PictureOfTheDay)
        setTheme(currentTheme)
    }

    override fun changeTheme(theme: Int) {
        val sp = getSharedPreferences(SP_DB_THEME, Context.MODE_PRIVATE)
        sp.edit().apply {
            putInt(SP_DB_THEME_TAG, theme)
            apply()
        }
        recreate()
    }
}

