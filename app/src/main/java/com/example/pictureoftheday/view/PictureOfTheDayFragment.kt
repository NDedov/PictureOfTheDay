package com.example.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.domain.PODData
import com.example.pictureoftheday.utils.toast
import com.example.pictureoftheday.viewmodel.PictureOfTheDayAppState
import com.example.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding: FragmentPictureOfTheDayBinding
        get() {
            return _binding!!
        }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
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
        viewModel.getLiveData().observe(viewLifecycleOwner) {renderData(view, it)}
        viewModel.getData(GregorianCalendar().time)
        setBottomSheetBehavior(view)
        chipInit()
        wikiFindInit()
    }

    private fun chipInit(){
        binding.chipToday.isChecked = true
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            val dayShift = when (checkedId){
                binding.chipYesterday.id -> -1
                binding.chipDayBeforeYesterday.id -> -2
                else -> {0}
            }
            viewModel.getData(GregorianCalendar().apply {
                add(Calendar.DATE,dayShift) }.time)
        }
    }

    private fun wikiFindInit(){
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    private fun renderData(view: View, appState: PictureOfTheDayAppState) {
        when (appState) {
            is PictureOfTheDayAppState.Success -> {
                val serverResponseData = appState.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    showImageAndDescription(url, view, serverResponseData)
                }
            }
            is PictureOfTheDayAppState.Loading -> {
                binding.imageView.loadGif(R.drawable.loading2)
            }
            is PictureOfTheDayAppState.Error -> {
                toast(appState.error.message)
            }
        }
    }

    private fun showImageAndDescription(
        url: String?,
        view: View,
        serverResponseData: PODData
    ) {
        binding.imageView.load(url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.loading2)
            crossfade(500)
        }
        view.findViewById<TextView>(R.id.bottom_sheet_description).text =
            serverResponseData.explanation
        view.findViewById<TextView>(R.id.bottom_sheet_description_header).text =
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
        val bottomSheet = view.findViewById<ConstraintLayout>(R.id.bottom_sheet_container)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}