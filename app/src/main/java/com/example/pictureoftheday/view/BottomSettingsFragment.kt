package com.example.pictureoftheday.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.BottomSettingsLayoutBinding
import com.example.pictureoftheday.utils.SP_DB_THEME
import com.example.pictureoftheday.utils.SP_DB_THEME_TAG
import com.example.pictureoftheday.view.pod.OnChangeThemeListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSettingsFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSettingsLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var changeThemeListener: OnChangeThemeListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSettingsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChipsChecks()
        initChipGroup()
        initSwitchNightMode()
        initChangeThemeListener()
    }

    private fun initChangeThemeListener() {
        if (requireActivity() is OnChangeThemeListener)
            changeThemeListener = requireActivity() as OnChangeThemeListener
    }

    private fun initSwitchNightMode() {
        binding.switchNightMode.isChecked = isDarkTheme(requireActivity())
        binding.switchNightMode.setOnClickListener {
            if (binding.switchNightMode.isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun isDarkTheme(activity: Activity): Boolean {
        return (activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) ||
                (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun initChipGroup() {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.defaultThemeChip.id -> {
                    changeThemeListener.changeTheme(R.style.Theme_PictureOfTheDay)
                }
                binding.marsThemeChip.id -> {
                    changeThemeListener.changeTheme(R.style.MarsTheme)
                }
                binding.venusThemeChip.id -> {
                    changeThemeListener.changeTheme(R.style.VenusTheme)
                }
            }
        }
    }

    private fun initChipsChecks() {
        val sp = requireActivity().getSharedPreferences(SP_DB_THEME, Context.MODE_PRIVATE)
        when (sp.getInt(SP_DB_THEME_TAG, R.style.Theme_PictureOfTheDay)){
            R.style.Theme_PictureOfTheDay -> binding.defaultThemeChip.isChecked = true
            R.style.MarsTheme -> binding.marsThemeChip.isChecked = true
            R.style.VenusTheme -> binding.venusThemeChip.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}