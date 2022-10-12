package com.example.pictureoftheday

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pictureoftheday.utils.SP_DB_THEME
import com.example.pictureoftheday.utils.SP_DB_THEME_TAG
import com.example.pictureoftheday.view.OnChangeThemeListener
import com.example.pictureoftheday.view.PictureOfTheDayFragment
import com.example.pictureoftheday.view.PictureOfTheDayPagerFragment

class MainActivity : AppCompatActivity(), OnChangeThemeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayPagerFragment())
                .commitNow()
        }
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