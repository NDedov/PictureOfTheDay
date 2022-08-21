package com.example.pictureoftheday.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pictureoftheday.PODApp
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.BottomSettingsLayoutBinding
import com.example.pictureoftheday.utils.SP_DB_THEME
import com.example.pictureoftheday.utils.SP_DB_THEME_TAG
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSettingsFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSettingsLayoutBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun initChipGroup() {
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.defaultThemeChip.id -> {
                    saveThemeToSP(R.style.Theme_PictureOfTheDay)
                    restartActivity()
                }
                binding.marsThemeChip.id -> {
                    saveThemeToSP(R.style.MarsTheme)
                    restartActivity()
                }
                binding.venusThemeChip.id -> {
                    saveThemeToSP(R.style.VenusTheme)
                    restartActivity()
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

    private fun saveThemeToSP(theme: Int) {
        val sp = requireActivity().getSharedPreferences(SP_DB_THEME, Context.MODE_PRIVATE)
        sp.edit().apply() {
            putInt(SP_DB_THEME_TAG, theme)
            apply()
        }
    }

    private fun restartActivity(){
        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
