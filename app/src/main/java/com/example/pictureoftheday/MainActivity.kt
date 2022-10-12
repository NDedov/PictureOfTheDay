package com.example.pictureoftheday

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pictureoftheday.databinding.ActivityMainBinding
import com.example.pictureoftheday.utils.SP_DB_THEME
import com.example.pictureoftheday.utils.SP_DB_THEME_TAG
import com.example.pictureoftheday.view.BottomSettingsFragment
import com.example.pictureoftheday.view.OnChangeThemeListener
import com.example.pictureoftheday.view.PictureOfTheDayPagerFragment
import com.example.pictureoftheday.view.WikiFragment

class MainActivity : AppCompatActivity(), OnChangeThemeListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_pod -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_container, PictureOfTheDayPagerFragment())
                        .commit()
                    true
                }
                R.id.navigation_wiki -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_container, WikiFragment())
                        .commit()
                    true
                }
                R.id.navigation_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_container, BottomSettingsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigationView.selectedItemId = R.id.navigation_pod
        setThemeFromSP()
    }

    private fun setThemeFromSP() {
        val sp = getSharedPreferences(SP_DB_THEME, Context.MODE_PRIVATE)
        val currentTheme = sp.getInt(SP_DB_THEME_TAG, R.style.Theme_PictureOfTheDay)
        setTheme(currentTheme)
    }

    override fun changeTheme(theme: Int) {
        val sp = getSharedPreferences(SP_DB_THEME, Context.MODE_PRIVATE)
        sp.edit().apply() {
            putInt(SP_DB_THEME_TAG, theme)
            apply()
        }
        recreate()
    }
}

sealed class Days{
    object Today:Days()
    object Yesterday:Days()
    object TDBY: Days()
}
