package com.example.pictureoftheday.view.pod

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.model.domain.PODData
import com.example.pictureoftheday.utils.toast
import com.example.pictureoftheday.view.podpager.Days
import com.example.pictureoftheday.viewmodel.PictureOfTheDayAppState
import com.example.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class PictureOfTheDayFragment(private val day: Days) : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding: FragmentPictureOfTheDayBinding
        get() {
            return _binding!!
        }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object {
        fun newInstance(day: Days) = PictureOfTheDayFragment(day)
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelInit()
        setBottomSheetBehavior(view)
    }

    private fun viewModelInit(){
        viewModel.getLiveData().observe(viewLifecycleOwner) {renderData(it)}
        val dayShift = when (day){
            Days.Today -> 0
            Days.Yesterday -> -1
            Days.TDBY -> -2
        }
        viewModel.getData(GregorianCalendar().apply {
            add(Calendar.DATE,dayShift) }.time)
    }

    private fun renderData(appState: PictureOfTheDayAppState) {
        when (appState) {
            is PictureOfTheDayAppState.Success -> {
                val serverResponseData = appState.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    showImageAndDescription(url, serverResponseData)
                }
            }
            is PictureOfTheDayAppState.Loading -> {
                binding.imageView.loadGif(R.drawable.loading_mod)
            }
            is PictureOfTheDayAppState.Error -> {
                toast(appState.error.message)
            }
        }
    }

    private fun showImageAndDescription(
        url: String?,
        serverResponseData: PODData
    ) {
        binding.imageView.load(url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.loading_mod)
            crossfade(500)
        }
        binding.bottomSheetLayout.bottomSheetDescription.text =
            serverResponseData.explanation
        binding.bottomSheetLayout.bottomSheetDescriptionHeader.text =
            serverResponseData.title
    }

    private fun ImageView.loadGif(img: Int) {
        this.load(img, ImageLoader.Builder(requireContext())
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder(requireContext()))
                } else {
                    add(GifDecoder())
                }
            }
            .build())
    }

    private fun setBottomSheetBehavior(view: View) {
        val bottomSheet = view.findViewById<ConstraintLayout>(R.id.bottom_sheet_layout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}